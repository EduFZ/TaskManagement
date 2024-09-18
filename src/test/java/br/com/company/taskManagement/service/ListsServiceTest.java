package br.com.company.taskManagement.service;

import br.com.company.taskManagement.dto.ItemsDto;
import br.com.company.taskManagement.dto.ListsDto;
import br.com.company.taskManagement.entity.Items;
import br.com.company.taskManagement.entity.Lists;
import br.com.company.taskManagement.enums.Priority;
import br.com.company.taskManagement.exception.ExceptionMessage;
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
class ListsServiceTest {

    @InjectMocks
    ListsService service;

    @Mock
    ListsRepository repository;


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

    private ListsDto buildListsDtoMock() {
        return ListsDto.builder()
                .title("Title Test")
                .description("Some Description")
                .itemsDto(new ArrayList<>(Arrays.asList(ItemsDto.builder()
                        .title("Title Test")
                        .description("Some Description")
                        .build())))
                .build();
    }


    @Test
    void shouldReturnListsWhenFindById() throws ExceptionMessage {
        Lists lists = buildListsMock();

        when(repository.findById(anyLong())).thenReturn(Optional.of(lists));

        Lists listsReturned = service.findById(1L);

        assertEquals(lists.getId(), listsReturned.getId());
    }

    @Test
    void shouldReturnPageListsWhenFindAll() {
        Lists lists = buildListsMock();
        List<Lists> listsList = new ArrayList<>(Arrays.asList(lists));

        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(listsList, pageable, listsList.size()));

        Page<Lists> listsReturned = service.findAll(pageable);

        assertEquals(lists.getId(), listsReturned.get().toList().get(0).getId());
    }

    @Test
    void shouldReturnPageListsWhenFindByFilters() {
        Lists lists = buildListsMock();
        List<Lists> listsList = new ArrayList<>(Arrays.asList(lists));

        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByFilters(any(), any(), any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(listsList, pageable, listsList.size()));

        Page<Lists> listsReturned = service.findByFilters(Priority.NORMAL, LocalDateTime.now(), LocalDateTime.now(), "Title Test", pageable);

        assertEquals(lists.getId(), listsReturned.get().toList().get(0).getId());
    }

    @Test
    void shouldReturnListsWhenCreateLists() throws ExceptionMessage {
        ListsDto listsDto = buildListsDtoMock();

        service.createLists(listsDto);

        verify(repository, times(1)).save(any(Lists.class));
    }

    @Test
    void shouldReturnExceptionWhenCreateLists() {
        ListsDto listsDto = buildListsDtoMock();
        listsDto.setTitle("Titl");

        ExceptionMessage ex = assertThrows(ExceptionMessage.class, () -> service.createLists(listsDto));

        assertEquals("O título deve ser maior ou igual à 6 caracteres e até 20 caracteres", ex.getMessage());
    }

    @Test
    void shouldReturnListsWhenPutLists() throws ExceptionMessage {
        ListsDto listsDto = buildListsDtoMock();
        Lists lists = buildListsMock();

        when(repository.findById(anyLong())).thenReturn(Optional.of(lists));
        when(repository.save(any(Lists.class))).thenReturn(lists);

        Lists listsReturned = service.putLists(1L, listsDto);

        verify(repository, times(1)).save(any(Lists.class));
        assertEquals(lists.getId(), listsReturned.getId());
    }

    @Test
    void shouldReturnExceptionWhenPutLists() {
        ListsDto listsDto = buildListsDtoMock();
        listsDto.setTitle("Titl");
        Lists lists = buildListsMock();

        when(repository.findById(anyLong())).thenReturn(Optional.of(lists));

        ExceptionMessage ex = assertThrows(ExceptionMessage.class, () -> {
            service.putLists(1L, listsDto);
        });

        assertEquals("O título deve ser maior ou igual à 6 caracteres e até 20 caracteres", ex.getMessage());
    }

    @Test
    void shouldDeleteListsWhenDeleteById() {
        service.deleteById(1L);

        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteListsWhenDelete() throws ExceptionMessage {
        Lists lists = buildListsMock();

        when(repository.findById(anyLong())).thenReturn(Optional.of(lists));
        service.delete(1L, lists);

        verify(repository, times(1)).delete(any(Lists.class));
    }

    @Test
    void shouldReturnExceptionWhenDelete() {
        Lists lists = buildListsMock();
        ExceptionMessage ex = assertThrows(ExceptionMessage.class, () -> {
            service.delete(1L, lists);
        });

        assertEquals("Lista não encontrada para ser deletada!", ex.getMessage());
    }


}