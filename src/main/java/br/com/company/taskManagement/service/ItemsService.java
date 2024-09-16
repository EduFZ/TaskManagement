package br.com.company.taskManagement.service;

import br.com.company.taskManagement.dto.ItemsDto;
import br.com.company.taskManagement.entity.Items;
import br.com.company.taskManagement.enums.Priority;
import br.com.company.taskManagement.exception.ExceptionMessage;
import br.com.company.taskManagement.repository.ItemsRepository;
import br.com.company.taskManagement.validations.TitleSizeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ItemsService {

    @Autowired
    ItemsRepository itemsRepository;

    public Items findById(Long id) throws ExceptionMessage {
        return itemsRepository.findById(id).orElseThrow(() -> new ExceptionMessage("Lista não encontrada!"));
    }

    public Page<Items> findAll(Pageable pageable) {
        return itemsRepository.findAll(pageable);
    }

    public Page<Items> findByFilters(Priority priority, LocalDateTime creationDate, LocalDateTime finishDate, String title, Pageable pageable) {
        return itemsRepository.findByFilters(priority,creationDate, finishDate, title, pageable);
    }

    public Items createItems(ItemsDto itemsDto) throws ExceptionMessage {
        Items items;
        if (TitleSizeValidation.isMinTitleSize(itemsDto.getTitle()) && TitleSizeValidation.isMaxTitleSize(itemsDto.getTitle())) {
            items = itemsDtoToItems(itemsDto);
        } else {
            throw new ExceptionMessage("O título deve ser maior ou igual à 6 caracteres e até 20 caracteres");
        }
        return itemsRepository.save(items);
    }

    public Items putItems(Long id, ItemsDto itemsDto) throws ExceptionMessage {
        Items existingItems = itemsRepository.findById(id).orElseThrow(() -> new ExceptionMessage("Item não encontrado!"));
        if (TitleSizeValidation.isMinTitleSize(itemsDto.getTitle()) && TitleSizeValidation.isMaxTitleSize(itemsDto.getTitle())) {
            existingItems.setTitle(itemsDto.getTitle());
        } else {
            throw new ExceptionMessage("O título deve ser maior ou igual à 6 caracteres e até 20 caracteres");
        }
        existingItems.setDescription(itemsDto.getDescription());
        existingItems.setCreationDate(itemsDto.getCreationDate());
        existingItems.setFinishDate(itemsDto.getFinishDate());
        existingItems.setPriority(itemsDto.getPriority());

        return itemsRepository.save(existingItems);
    }

    public void deleteById(Long id) {
        itemsRepository.deleteById(id);
    }

    public void delete(Long id, Items items) throws ExceptionMessage {
        Items itemsExisting = itemsRepository.findById(id).orElse(null);
        if (itemsExisting != null)
            itemsRepository.delete(items);

        else throw new ExceptionMessage("Item não encontrado para ser deletado!");
    }


    private Items itemsDtoToItems(ItemsDto itemsDto) {
        return Items.builder()
                .title(itemsDto.getTitle())
                .description(itemsDto.getDescription())
                .creationDate(itemsDto.getCreationDate() != null ? itemsDto.getCreationDate() : LocalDateTime.now())
                .priority(itemsDto.getPriority() != null ? itemsDto.getPriority() : Priority.NORMAL)
                .build();
    }

}
