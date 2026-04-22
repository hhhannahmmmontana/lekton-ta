package io.github.hhhannahmmmontana.library.exceptions;

/**
 * Исключение, выбрасываемое при попытке добавить книгу, которая уже существует в репозитории.
 * Дубликат определяется по совпадению названия и автора.
 */
public class DuplicateException extends LibraryCheckedException {
    public DuplicateException() {
        super("Book already exists");
    }
}
