package br.com.company.taskManagement.repository;

import br.com.company.taskManagement.entity.Lists;
import br.com.company.taskManagement.enums.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ListsRepository  extends JpaRepository<Lists, Long> {
    Page<Lists> findAll(Pageable pageable);

    @Query("SELECT f FROM Items f WHERE (:priority IS NULL OR f.priority = :priority) " +
            "AND (:creationDate IS NULL OR f.creationDate >= :creationDate) " +
            "AND (:finishDate IS NULL OR f.finishDate <= :finishDate) " +
            "AND (:title IS NULL OR f.title LIKE %:title%)")
    Page<Lists> findByFilters(@Param("priority") Priority priority, @Param("creationDate")LocalDateTime creationDate,
            @Param("finishDate") LocalDateTime finishDate, @Param("title") String title, Pageable pageable);
}
