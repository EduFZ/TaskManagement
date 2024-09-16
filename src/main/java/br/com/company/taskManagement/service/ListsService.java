package br.com.company.taskManagement.service;

import br.com.company.taskManagement.dto.ItemsDto;
import br.com.company.taskManagement.dto.ListsDto;
import br.com.company.taskManagement.entity.Items;
import br.com.company.taskManagement.entity.Lists;
import br.com.company.taskManagement.enums.Priority;
import br.com.company.taskManagement.exception.ExceptionMessage;
import br.com.company.taskManagement.repository.ListsRepository;
import br.com.company.taskManagement.validations.TitleSizeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListsService {

    @Autowired
    ListsRepository listsRepository;

    public Lists findById(Long id) throws ExceptionMessage {
        return listsRepository.findById(id).orElseThrow(() -> new ExceptionMessage("Lista não encontrada!"));
    }

    public Page<Lists> findAll(Pageable pageable) {
        return listsRepository.findAll(pageable);
    }

    public Page<Lists> findByFilters(Priority priority, LocalDateTime creationDate, String title, Pageable pageable) {
        return listsRepository.findByFilters(priority,creationDate, title, pageable);
    }

    public Lists createLists(ListsDto listsDto) throws ExceptionMessage {
        Lists lists;
        if (TitleSizeValidation.isMinTitleSize(listsDto.getTitle()) && TitleSizeValidation.isMaxTitleSize(listsDto.getTitle())) {
            lists = listsDtoToLists(listsDto);
        } else {
            throw new ExceptionMessage("O título deve ser maior ou igual à 6 caracteres e até 20 caracteres");
        }
        return listsRepository.save(lists);
    }

    public Lists putLists(Long id, ListsDto listsDto) throws ExceptionMessage {
        Lists existingLists = listsRepository.findById(id).orElseThrow(() -> new ExceptionMessage("Lista não encontrada!"));
        if (TitleSizeValidation.isMinTitleSize(listsDto.getTitle()) && TitleSizeValidation.isMaxTitleSize(listsDto.getTitle())) {
            existingLists.setTitle(listsDto.getTitle());
        } else {
           throw new ExceptionMessage("O título deve ser maior ou igual à 6 caracteres e até 20 caracteres");
        }
        existingLists.setDescription(listsDto.getDescription());
        existingLists.setCreationDate(listsDto.getCreationDate());
        existingLists.setItems(itemsDtoListToItemsList(listsDto.getItemsDto()));
        existingLists.setPriority(listsDto.getPriority());

        return listsRepository.save(existingLists);
    }

    public void deleteById(Long id) {
        listsRepository.deleteById(id);
    }

    public void delete(Long id, Lists lists) throws ExceptionMessage {
        Lists list = listsRepository.findById(id).orElse(null);
        if (list != null)
            listsRepository.delete(lists);

        else throw new ExceptionMessage("Lista não encontrada para ser deletada");
    }

    private Lists listsDtoToLists(ListsDto listsDto) {
        return Lists.builder()
                .title(listsDto.getTitle())
                .description(listsDto.getDescription())
                .items(itemsDtoListToItemsList(listsDto.getItemsDto()))
                .creationDate(listsDto.getCreationDate() != null ? listsDto.getCreationDate() : LocalDateTime.now())
                .priority(listsDto.getPriority() != null ? listsDto.getPriority() : Priority.NORMAL)
                .build();
    }

    private List<Items> itemsDtoListToItemsList(List<ItemsDto> itemsDto) {
        List<Items> itemsList = new ArrayList<>();
        itemsDto.forEach(items -> {
            Items itm = Items.builder()
                    .title(items.getTitle())
                    .description(items.getDescription())
                    .creationDate(items.getCreationDate() != null ? items.getCreationDate() : LocalDateTime.now())
                    .priority(items.getPriority() != null ? items.getPriority() : Priority.NORMAL)
                    .build();
            itemsList.add(itm);
        });
        return itemsList;
    }

}
