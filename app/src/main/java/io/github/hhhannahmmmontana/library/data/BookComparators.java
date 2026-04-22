package io.github.hhhannahmmmontana.library.data;

import io.github.hhhannahmmmontana.library.data.entities.Book;

import java.util.Comparator;

/**
 * Набор готовых компараторов для сортировки объектов {@link Book}.
 */
public final class BookComparators {
    private BookComparators() {}

    public final static Comparator<Book> BY_TITLE =
        Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);

    public final static Comparator<Book> BY_AUTHOR =
        Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);

    public final static Comparator<Book> BY_YEAR =
        Comparator.comparingInt(Book::getYear);
}
