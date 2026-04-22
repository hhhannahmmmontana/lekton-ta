package io.github.hhhannahmmmontana.library.exceptions;

/**
 * Базовое проверяемое исключение для всех ошибок бизнес-логики библиотеки.
 */
public abstract class LibraryCheckedException extends Exception {
    public LibraryCheckedException(String message) {
        super(message);
    }
}