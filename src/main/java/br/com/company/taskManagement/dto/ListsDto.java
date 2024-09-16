package br.com.company.taskManagement.dto;

import br.com.company.taskManagement.entity.Items;
import br.com.company.taskManagement.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListsDto {

    private String title;
    private String description;
    private List<ItemsDto> itemsDto;
    private LocalDateTime creationDate;
    private Priority priority;

}
