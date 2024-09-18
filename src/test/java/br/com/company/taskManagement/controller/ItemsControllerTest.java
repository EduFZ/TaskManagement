package br.com.company.taskManagement.controller;

import br.com.company.taskManagement.dto.ItemsDto;
import br.com.company.taskManagement.entity.Items;
import br.com.company.taskManagement.enums.Priority;
import br.com.company.taskManagement.exception.ExceptionMessage;
import br.com.company.taskManagement.service.ItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemsController.class)
class ItemsControllerTest {

    @MockBean
    ItemsService itemsService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;


    private Items buildItemsMock() {
        return Items.builder()
                .id(2L)
                .title("Title Test")
                .description("Some Description")
                .build();
    }

    private ItemsDto buildItemsDtoMock() {
        return ItemsDto.builder()
                .title("Title Test")
                .description("Some Description")
                .build();
    }


    @Test
    void shouldReturnStatusOkWhenFindById() throws Exception {
        Items items = buildItemsMock();

        when(itemsService.findById(anyLong())).thenReturn(items);

        mvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));
    }

    @Test
    void shouldReturnStatusOkWhenFindAll() throws Exception {
        Items items = buildItemsMock();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Items> mockPage = new PageImpl<>(Collections.singletonList(items), pageable, 1);

        when(itemsService.findAll(any(Pageable.class))).thenReturn(mockPage);

        mvc.perform(MockMvcRequestBuilders.get("/items/all")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockPage)));
    }

    @Test
    void shouldReturnStatusOkWhenFindAllByLists() throws Exception {
        Items items = buildItemsMock();

        Page<Items> itemsPage = new PageImpl<>(Collections.singletonList(items));

        when(itemsService.findAllByLists(anyLong(), any(Pageable.class))).thenReturn(itemsPage);

        mvc.perform(get("/items/listItems/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(items.getId()))
                .andExpect(jsonPath("$.content[0].title").value(items.getTitle()));
    }

    @Test
    void shouldReturnStatusOkWhenFindByFilters() throws Exception {
        Items items = buildItemsMock();

        Priority priority = Priority.NORMAL;
        LocalDateTime creationDate = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime finishDate = LocalDateTime.of(2023, 12, 31, 18, 0);
        String title = "Test Title";

        Pageable pageable = PageRequest.of(0, 10);
        Page<Items> mockPage = new PageImpl<>(Collections.singletonList(items), pageable, 1);

        when(itemsService.findByFilters(any(Priority.class), any(), any(), any(), any())).thenReturn(mockPage);

        mvc.perform(MockMvcRequestBuilders.get("/items/filters")
                        .param("priority", priority.toString())
                        .param("creationDate", creationDate.toString())
                        .param("finishDate", finishDate.toString())
                        .param("title", title)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockPage)));
    }

    @Test
    void shouldReturnStatusCreatedWhenCreateLists() throws Exception {
        Items items = buildItemsMock();
        ItemsDto itemsDto = buildItemsDtoMock();

        when(itemsService.createItems(anyLong(), any(ItemsDto.class))).thenReturn(items);

        mvc.perform(post("/items/createItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));
    }

    @Test
    void shouldReturnStatusBadRequestWhenCreateLists() throws Exception {
        ItemsDto itemsDto = buildItemsDtoMock();
        itemsDto.setTitle("Titl");

        when(itemsService.createItems(anyLong(), any(ItemsDto.class))).thenThrow(new ExceptionMessage("Invalid data"));

        mvc.perform(post("/items/createItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid data"));
    }

    @Test
    void shouldReturnStatusInternalServerErrorWhenCreateLists() throws Exception {
        ItemsDto itemsDto = buildItemsDtoMock();

        when(itemsService.createItems(anyLong(), any(ItemsDto.class))).thenThrow(new RuntimeException("Exception"));

        mvc.perform(post("/items/createItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Exception"));
    }

    @Test
    void shouldReturnStatusOkWhenPutLists() throws Exception {
        ItemsDto itemsDto = buildItemsDtoMock();
        Items items = buildItemsMock();

        when(itemsService.putItems(anyLong(), any(ItemsDto.class))).thenReturn(items);

        mvc.perform(put("/items/putItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));
    }

    @Test
    void shouldReturnExceptionMessageWhenPutLists() throws Exception {
        ItemsDto itemsDto = buildItemsDtoMock();
        itemsDto.setTitle("Titl");

        when(itemsService.putItems(anyLong(), any(ItemsDto.class))).thenThrow(new ExceptionMessage("ExceptionMessage"));

        mvc.perform(put("/items/putItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ExceptionMessage"));
    }

    @Test
    void shouldReturnStatusInternalServerErrorWhenPutLists() throws Exception {
        ItemsDto itemsDto = buildItemsDtoMock();

        when(itemsService.putItems(anyLong(), any(ItemsDto.class))).thenThrow(new RuntimeException("Exception"));

        mvc.perform(put("/items/putItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Exception"));
    }

    @Test
    void shouldReturnStatusNoContentWhenDeleteById() throws Exception {
        doNothing().when(itemsService).deleteById(anyLong());

        mvc.perform(delete("/items/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusInternalServerErrorWhenDeleteById() throws Exception {
        doThrow(new RuntimeException("Exception")).when(itemsService).deleteById(1L);

        mvc.perform(delete("/items/delete/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Exception"));
    }

    @Test
    void shouldReturnStatusNoContentWhenDelete() throws Exception {
        Items items = buildItemsMock();

        doNothing().when(itemsService).delete(anyLong(), any(Items.class));

        mvc.perform(delete("/items/deleteItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusBadRequestWhenDelete() throws Exception {
        Items items = buildItemsMock();

        doThrow(new ExceptionMessage("ExceptionMessage")).when(itemsService).delete(anyLong(), any(Items.class));

        mvc.perform(delete("/items/deleteItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ExceptionMessage"));
    }

    @Test
    void shouldReturnStatusInternalServerErrorWhenDelete() throws Exception {
        Items items = buildItemsMock();

        doThrow(new RuntimeException("Exception")).when(itemsService).delete(anyLong(), any(Items.class));

        mvc.perform(delete("/items/deleteItems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Exception"));
    }


}