package com.busraciftlik.todoapp.service;

import com.busraciftlik.todoapp.ex.TodoItemNotFoundException;
import com.busraciftlik.todoapp.model.TodoItemsListFilter;
import com.busraciftlik.todoapp.model.TodoItem;
import com.busraciftlik.todoapp.model.TodoItemDto;
import com.busraciftlik.todoapp.model.TodoItemFormData;
import com.busraciftlik.todoapp.repository.TodoItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoItemRepository todoItemRepository;

    public TodoService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    public String index(Model model) {
        addAttributesForIndex(model, TodoItemsListFilter.ALL);
        return "index";
    }

    public String indexActive(Model model) {
        addAttributesForIndex(model, TodoItemsListFilter.ACTIVE);
        return "index";
    }

    public String indexCompleted(Model model) {
        addAttributesForIndex(model, TodoItemsListFilter.COMPLETED);
        return "index";
    }

    private void addAttributesForIndex(Model model,
                                       TodoItemsListFilter listFilter) {
        model.addAttribute("item", new TodoItemFormData());
        model.addAttribute("filter", listFilter);
        model.addAttribute("todos", getTodoItems(listFilter));
        model.addAttribute("totalNumberOfItems", todoItemRepository.count());
        model.addAttribute("numberOfActiveItems", getNumberOfActiveItems());
        model.addAttribute("numberOfCompletedItems", getNumberOfCompletedItems());
    }

    private List<TodoItemDto> getTodoItems(TodoItemsListFilter filter) {
        return switch (filter) {
            case ALL -> convertToDto(todoItemRepository.findAll());
            case ACTIVE -> convertToDto(todoItemRepository.findAllByCompleted(false));
            case COMPLETED -> convertToDto(todoItemRepository.findAllByCompleted(true));
        };
    }

    private List<TodoItemDto> convertToDto(List<TodoItem> todoItems) {

        ArrayList<TodoItemDto> todoItemDtos = new ArrayList<>();

        for (TodoItem todoItem : todoItems) {
            todoItemDtos.add(TodoItemDto.builder().title(todoItem.getTitle()).id(todoItem.getId()).completed(todoItem.isCompleted()).build());
        }
        return todoItemDtos;
       /* return todoItems
                .stream()
                .map(todoItem -> new TodoItemDto(todoItem.getId(),
                        todoItem.getTitle(),
                        todoItem.isCompleted()))
                .collect(Collectors.toList());*/
    }


    public String addNewTodoItem(TodoItemFormData formData) {
        todoItemRepository.save(new TodoItem(formData.getTitle(), false));
        return "redirect:/";
    }


    public String toggleSelection(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException(id));

        todoItem.setCompleted(!todoItem.isCompleted());
        todoItemRepository.save(todoItem);
        return "redirect:/";
    }

    public String deleteTodoItem(Long id) {
        todoItemRepository.deleteById(id);
        return "redirect:/";
    }

    public String deleteCompletedItems() {
        List<TodoItem> items = todoItemRepository.findAllByCompleted(true);
        for (TodoItem item : items) {
            todoItemRepository.deleteById(item.getId());
        }
        return "redirect:/";
    }

    public String toggleAll() {
        List<TodoItem> todoItems = todoItemRepository.findAll();
        for (TodoItem todoItem : todoItems) {
            todoItem.setCompleted(!todoItem.isCompleted());
            todoItemRepository.save(todoItem);
        }
        return "redirect:/";
    }

    private int getNumberOfActiveItems() {
        return todoItemRepository.countAllByCompleted(false);
    }


    private int getNumberOfCompletedItems() {
        return todoItemRepository.countAllByCompleted(true);
    }

}
