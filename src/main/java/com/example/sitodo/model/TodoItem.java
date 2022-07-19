package com.example.sitodo.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Boolean finished = Boolean.FALSE;

    protected TodoItem() { }

    public TodoItem(String title) {
        this(null, title);
    }

    public TodoItem(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(id, todoItem.id) && title.equals(todoItem.title) && finished.equals(todoItem.finished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, finished);
    }
}
