package br.com.company.taskManagement.controller;

import br.com.company.taskManagement.dto.ListsDto;
import br.com.company.taskManagement.entity.Lists;
import br.com.company.taskManagement.enums.Priority;
import br.com.company.taskManagement.exception.ExceptionMessage;
import br.com.company.taskManagement.service.ListsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/lists")
public class ListsController {

    @Autowired
    ListsService listsService;

    @GetMapping("/{id}")
    public ResponseEntity<Lists> findById(@PathVariable Long id) throws ExceptionMessage {
        return ResponseEntity.ok(listsService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Lists>> findAll(Pageable pageable) {
        return ResponseEntity.ok(listsService.findAll(pageable));
    }

    @GetMapping("/filters")
    public ResponseEntity<Page<Lists>> findByFilters(@RequestParam(required = false) Priority priority,
                                                     @RequestParam(required = false)LocalDateTime creationDate,
                                                     @RequestParam(required = false) String title, Pageable pageable) {
        return ResponseEntity.ok(listsService.findByFilters(priority, creationDate, title, pageable));
    }

    @PostMapping("createLists")
    public ResponseEntity<?> createLists(@RequestBody ListsDto listsDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(listsService.createLists(listsDto));
        } catch (ExceptionMessage ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/putLists/{id}")
    public ResponseEntity<?> putLists(@PathVariable Long id, @RequestBody ListsDto listsDto) throws ExceptionMessage {
        try {
            return ResponseEntity.ok(listsService.putLists(id, listsDto));
        } catch (ExceptionMessage ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            listsService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/deleteLists/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody Lists lists) {
        try {
            listsService.delete(id, lists);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ExceptionMessage ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
