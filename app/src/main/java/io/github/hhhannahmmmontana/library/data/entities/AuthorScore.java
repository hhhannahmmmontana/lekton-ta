package io.github.hhhannahmmmontana.library.data.entities;

import java.util.Map;

/**
 * Информация о количестве книг конкретного автора.
 * <p>
 * Используется для представления данных в статистических отчетах.
 * @param authorsName имя автора
 * @param booksAmount количество книг этого автора в библиотеке
 */
public record AuthorScore(String authorsName, int booksAmount) {
    public static AuthorScore fromEntry(Map.Entry<String, Integer> kv) {
        return new AuthorScore(kv.getKey(), kv.getValue());
    }
}