package br.com.company.taskManagement.repository;

import br.com.company.taskManagement.model.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListsRepository  extends JpaRepository<Lists, Long> {
}
