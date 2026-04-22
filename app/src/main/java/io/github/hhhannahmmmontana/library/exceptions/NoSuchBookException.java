package io.github.hhhannahmmmontana.library.exceptions;

/**
 * Исключение, выбрасываемое при попытке выполнить операцию над несуществующей книгой.
 * В контексте лабораторной - при удалении по неверному ID.
 */
public class NoSuchBookException extends LibraryCheckedException {
    public NoSuchBookException() {
        super("Book does not exist");
    }
}
