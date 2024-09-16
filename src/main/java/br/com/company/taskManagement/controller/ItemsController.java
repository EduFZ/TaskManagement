package br.com.company.taskManagement.controller;

import br.com.company.taskManagement.dto.ItemsDto;
import br.com.company.taskManagement.entity.Items;
import br.com.company.taskManagement.enums.Priority;
import br.com.company.taskManagement.exception.ExceptionMessage;
import br.com.company.taskManagement.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/items")
public class ItemsController {

    @Autowired
    ItemsService itemsService;

    @GetMapping("/{id}")
    public ResponseEntity<Items> findById(@PathVariable Long id) throws ExceptionMessage {
        return ResponseEntity.ok(itemsService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Items>> findAll(Pageable pageable) {
        return ResponseEntity.ok(itemsService.findAll(pageable));
    }

    @GetMapping("/filters")
    public ResponseEntity<Page<Items>> findByFilters(@RequestParam(required = false) Priority priority,
                                                     @RequestParam(required = false) LocalDateTime creationDate,
                                                     @RequestParam(required = false)LocalDateTime finishDate,
                                                     @RequestParam(required = false) String title, Pageable pageable) {
        return ResponseEntity.ok(itemsService.findByFilters(priority, creationDate, finishDate, title, pageable));
    }

    @PostMapping("createItems")
    public ResponseEntity<?> createLists(@RequestBody ItemsDto itemsDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(itemsService.createItems(itemsDto));
        } catch (ExceptionMessage ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/putItems/{id}")
    public ResponseEntity<?> putLists(@PathVariable Long id, @RequestBody ItemsDto itemsDto) throws ExceptionMessage {
        try {
            return ResponseEntity.ok(itemsService.putItems(id, itemsDto));
        } catch (ExceptionMessage ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            itemsService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/deleteLists/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody Items items) {
        try {
            itemsService.delete(id, items);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ExceptionMessage ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
