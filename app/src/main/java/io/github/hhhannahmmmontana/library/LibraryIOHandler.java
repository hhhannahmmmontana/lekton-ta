package io.github.hhhannahmmmontana.library;

import io.github.hhhannahmmmontana.library.commands.CommandFactory;
import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.exceptions.LibraryCheckedException;
import lombok.Getter;

import java.io.*;

/**
 * Обработчик ввода-вывода библиотеки.
 * <p>
 * Класс отвечает за чтение команд из потока ввода, их выполнение через фабрику
 * и запись результатов или ошибок обратно в поток вывода.
 */
public final class LibraryIOHandler {
    private final static String EXIT_COMMAND = "EXIT";
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final CommandFactory commandFactory = new CommandFactory();
    private final LibraryRepository repository;

    /** Флаг остановки цикла обработки. */
    @Getter
    private boolean isStopped = false;

    /**
     * Создает обработчик на основе стандартных или кастомных потоков.
     * @param in поток ввода (например, System.in)
     * @param out поток вывода (например, System.out)
     * @param repository хранилище данных, с которым будут работать команды
     */
    public LibraryIOHandler(
        InputStream in,
        OutputStream out,
        LibraryRepository repository
    ) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
        this.repository = repository;
    }

    /**
     * Считывает одну строку ввода, распознает команду и выполняет её.
     * <p>
     * В случае возникновения {@link LibraryCheckedException} выводит сообщение
     * об ошибке пользователю. При критической {@link IOException} выбрасывает RuntimeException.
     */
    public void execute() {
        try {
            var input = reader.readLine().strip();
            if (input.equals("EXIT")) {
                isStopped = true;
                return;
            }
            try {
                var command = commandFactory.createCommand(input);
                command.execute(repository, writer);
            } catch (LibraryCheckedException e) {
                writer.write("Error: " + e.getMessage() + '\n');
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
