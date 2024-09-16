package br.com.company.taskManagement.repository;

import br.com.company.taskManagement.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
    Page<Items> findAll(Pageable pageable);
}
