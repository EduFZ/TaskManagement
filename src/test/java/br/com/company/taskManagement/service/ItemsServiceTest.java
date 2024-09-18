package br.com.company.taskManagement.service;

import br.com.company.taskManagement.dto.ItemsDto;
import br.com.company.taskManagement.entity.Items;
import br.com.company.taskManagement.entity.Lists;
import br.com.company.taskManagement.enums.Priority;
import br.com.company.taskManagement.exception.ExceptionMessage;
import br.com.company.taskManagement.repository.ItemsRepository;
import br.com.company.taskManagement.repository.ListsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemsServiceTest {

    @InjectMocks
    ItemsService service;

    @Mock
    ItemsRepository repository;

    @Mock
    ListsRepository listsRepository;


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

    private Lists buildListsMock() {
        return Lists.builder()
                .id(1L)
                .title("Title Test")
                .description("Some Description")
                .items(new ArrayList<>(Arrays.asList(Items.builder()
                        .id(2L)
                        .title("Title Test")
                        .description("Some Description")
                        .build())))
                .build();
    }


    @Test
    void shouldReturnItemsWhenFindById() throws ExceptionMessage {
        Items items = buildItemsMock();

        when(repository.findById(anyLong())).thenReturn(Optional.of(items));

        Items itemsReturned = service.findById(1L);

        assertEquals(items.getId(), itemsReturned.getId());
    }

    @Test
    void shouldReturnPageItemsWhenFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Items items = buildItemsMock();
        List<Items> itemsList = new ArrayList<>(Arrays.asList(items));

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(itemsList, pageable, itemsList.size()));

        Page<Items> pageItems = service.findAll(pageable);

        assertEquals(items.getId(), pageItems.getContent().get(0).getId());
    }

    @Test
    void shouldReturnPageAllItemsListsWhenFindAllByLists() throws ExceptionMessage {
        Pageable pageable = PageRequest.of(0, 10);
        Lists lists = buildListsMock();

        when(listsRepository.findById(anyLong())).thenReturn(Optional.of(lists));

        Page<Items> pageItems = service.findAllByLists(1L, pageable);

        assertEquals(lists.getItems().get(0).getId(), pageItems.getContent().get(0).getId());
    }

    @Test
    void shouldReturnPageItemsWhenFindByFilters() {
        Items items = buildItemsMock();
        List<Items> itemsList = new ArrayList<>(Arrays.asList(items));

        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByFilters(any(), any(), any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(itemsList, pageable, itemsList.size()));

        Page<Items> itemsReturned = service.findByFilters(Priority.NORMAL, LocalDateTime.now(), LocalDateTime.now(), "Title Test", pageable);

        assertEquals(items.getId(), itemsReturned.get().toList().get(0).getId());
    }

    @Test
    void shouldReturnItemsWhenCreateItems() throws ExceptionMessage {
        Lists lists = buildListsMock();
        ItemsDto itemsDto = buildItemsDtoMock();

        when(listsRepository.findById(anyLong())).thenReturn(Optional.of(lists));

        Items itemsReturned = service.createItems(1L, itemsDto);

        assertEquals(2, lists.getItems().size());
        assertEquals(itemsDto.getTitle(), itemsReturned.getTitle());
    }

    @Test
    void shouldReturnExceptionWhenCreateItems() {
        Lists lists = buildListsMock();
        ItemsDto itemsDto = buildItemsDtoMock();
        itemsDto.setTitle("Titl");

        when(listsRepository.findById(anyLong())).thenReturn(Optional.of(lists));

        ExceptionMessage ex = assertThrows(ExceptionMessage.class, () -> {
            service.createItems(1L, itemsDto);
        });

        assertEquals("O título deve ser maior ou igual à 6 caracteres e até 20 caracteres", ex.getMessage());
    }

    @Test
    void shouldReturnItemsWhenPutItems() throws ExceptionMessage {
        ItemsDto itemsDto = buildItemsDtoMock();
        Items items = buildItemsMock();

        when(repository.findById(anyLong())).thenReturn(Optional.of(items));
        when(repository.save(any(Items.class))).thenReturn(items);

        Items itemsReturned = service.putItems(1L, itemsDto);

        assertEquals(itemsDto.getTitle(), itemsReturned.getTitle());
    }

    @Test
    void shouldDeleteListsWhenDeleteById() {
        service.deleteById(1L);

        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteListsWhenDelete() throws ExceptionMessage {
        Items items = buildItemsMock();

        when(repository.findById(anyLong())).thenReturn(Optional.of(items));
        service.delete(1L, items);

        verify(repository, times(1)).delete(any(Items.class));
    }


}