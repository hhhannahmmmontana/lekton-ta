package io.github.hhhannahmmmontana.library.data.entities;

/**
 * Сводная статистика по библиотечному фонду.
 * <p>
 * Содержит информацию о крайних временных точках (самая старая и новая книги),
 * а также список наиболее продуктивных авторов.
 * @param oldestBook самая старая книга в коллекции по году издания
 * @param newestBook самая свежая книга в коллекции по году издания
 * @param topScores  массив лидеров среди авторов (рейтинг по количеству книг)
 */
public record LibraryStats(
    Book oldestBook,
    Book newestBook,
    AuthorScore[] topScores
) {}
