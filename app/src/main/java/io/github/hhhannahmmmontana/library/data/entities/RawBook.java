package io.github.hhhannahmmmontana.library.data.entities;

/**
 * Исходные данные книги для добавления в систему.
 * <p>
 * Представляет собой состояние книги без уникального идентификатора.
 * Используется на этапе валидации и создания новых записей в репозитории.
 * @param title  название книги
 * @param author имя автора
 * @param year   год издания
 */
public record RawBook(String title, String author, int year) {
    public boolean bookEquals(Book book) {
        return this.title.equals(book.getTitle()) && this.author.equals(book.getAuthor());
    }

    public Book toBook(int id) {
        return new Book(id, title, author, year);
    }
}
