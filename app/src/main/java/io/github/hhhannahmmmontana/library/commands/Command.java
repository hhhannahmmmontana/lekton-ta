package io.github.hhhannahmmmontana.library.commands;

import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.exceptions.LibraryCheckedException;

import java.io.IOException;
import java.io.Writer;

/**
 * Базовый интерфейс для реализации команд управления библиотекой.
 * <p>
 * Каждая реализация представляет собой конкретное действие (например, добавление книги,
 * поиск или удаление), которое может быть выполнено над репозиторием.
 */
public interface Command {
    /**
     * Выполняет бизнес-логику команды.
     *
     * @param repository репозиторий библиотеки, предоставляющий доступ к данным
     * @param writer объект для вывода результатов выполнения команды пользователю
     * @throws IOException если возникла ошибка ввода-вывода при записи ответа
     * @throws LibraryCheckedException если выполнение команды нарушает правила бизнес-логики библиотеки
     */
    void execute(
        LibraryRepository repository,
        Writer writer
    ) throws IOException, LibraryCheckedException;
}
