package br.com.company.taskManagement.model;

import br.com.company.taskManagement.enums.Priority;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Items {

    @Id
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private Priority priority = Priority.NORMAL;
}
