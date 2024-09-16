package br.com.company.taskManagement.entity;

import br.com.company.taskManagement.enums.Priority;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "list_id")
    private List<Items> items;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

}
