package com.example.sitodo.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Item> items;

    protected TodoList() { }

    public TodoList(List<Item> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoList todoList = (TodoList) o;
        return Objects.equals(id, todoList.id) && Objects.equals(items, todoList.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items);
    }
}
