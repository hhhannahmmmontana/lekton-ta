package io.github.hhhannahmmmontana.library.exceptions;

/**
 * Исключение, указывающее на некорректный ввод команды пользователем.
 * Выбрасывается, если имя команды неизвестно или передано неверное количество аргументов.
 */
public class InvalidCommandFormatException extends LibraryCheckedException {
    public InvalidCommandFormatException() {
        super("Invalid command format");
    }
}
