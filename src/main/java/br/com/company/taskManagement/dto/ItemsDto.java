package br.com.company.taskManagement.dto;

import br.com.company.taskManagement.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsDto {

    private String title;
    private String description;
    private LocalDateTime creationDate;
    private Priority priority;

}
