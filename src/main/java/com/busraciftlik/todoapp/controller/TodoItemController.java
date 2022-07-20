package com.busraciftlik.todoapp.controller;

import com.busraciftlik.todoapp.model.TodoItemFormData;
import com.busraciftlik.todoapp.repository.TodoItemRepository;
import com.busraciftlik.todoapp.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class TodoItemController {

    private final TodoItemRepository todoItemRepository;
    private final TodoService todoService;

    public TodoItemController(TodoItemRepository todoItemRepository, TodoService todoService) {
        this.todoItemRepository = todoItemRepository;
        this.todoService = todoService;
    }

    @GetMapping
    public String index(Model model) {
        return todoService.index(model);
    }

    @GetMapping("/active")
    public String indexActive(Model model) {
        return todoService.indexActive(model);
    }

    @GetMapping("/completed")
    public String indexCompleted(Model model) {
        return todoService.indexCompleted(model);
    }

    @PostMapping
    public String addNewTodoItem(@Valid @ModelAttribute("item") TodoItemFormData formData) {
        return todoService.addNewTodoItem(formData);
    }


    @PutMapping("/{id}/toggle")
    public String toggleSelection(@PathVariable("id") Long id) {
        return todoService.toggleSelection(id);
    }

    @DeleteMapping("/{id}")
    public String deleteTodoItem(@PathVariable("id") Long id) {
        return todoService.deleteTodoItem(id);
    }

    @DeleteMapping("/completed")
    public String deleteCompletedItems() {
        return todoService.deleteCompletedItems();
    }

    @PutMapping("/toggle-all")
    public String toggleAll() {
        return todoService.toggleAll();
    }

}
