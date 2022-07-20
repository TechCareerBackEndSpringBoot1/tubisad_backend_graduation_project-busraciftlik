package com.busraciftlik.todoapp.ex;

public class TodoItemNotFoundException extends RuntimeException {
    public TodoItemNotFoundException(Long id) {
        super("Todo item with id:" +id + "not found");
    }
}
