package io.github.hhhannahmmmontana.library.data.entities;

import lombok.Getter;

import java.util.Objects;

/**
 * Модель книги, содержащая основную информацию.
 */
public final class Book {
    @Getter private final int id;
    @Getter private final String title;
    @Getter private final String author;
    @Getter private final int year;

    /** Техническое поле для быстрого регистронезависимого поиска по названию и автору. */
    private final String searchKey;

    public Book(
        int id,
        String title,
        String author,
        int year
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.searchKey = (title + " " + author).toLowerCase();
    }

    public boolean matches(String pattern) {
        return searchKey.contains(pattern);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Book book) {
            return book.getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id + ";" + title + ';' + author + ';' + year;
    }
}
