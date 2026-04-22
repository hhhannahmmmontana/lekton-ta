package io.github.hhhannahmmmontana.library.commands;

import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.data.entities.RawBook;
import io.github.hhhannahmmmontana.library.exceptions.DuplicateException;
import io.github.hhhannahmmmontana.library.exceptions.InvalidCommandFormatException;

import java.io.IOException;
import java.io.Writer;

/**
 * Команда для добавления новой книги в библиотеку.
 * @param rawBook объект с исходными данными книги (без ID), полученными из ввода
 */
public record AddCommand(RawBook rawBook) implements Command {
    public static AddCommand fromStringArg(String arg)
        throws InvalidCommandFormatException {
        try {
            if (arg == null) {
                throw new InvalidCommandFormatException();
            }
            var info = arg.split(";");
            if (info.length != 3) {
                throw new InvalidCommandFormatException();
            }
            return new AddCommand(
                new RawBook(
                    info[0],
                    info[1],
                    Integer.parseInt(info[2])
                )
            );
        } catch (NumberFormatException ex) {
            throw new InvalidCommandFormatException();
        }
    }

    @Override
    public void execute(
        LibraryRepository repository,
        Writer writer
    ) throws IOException, DuplicateException {
        var id = repository.add(rawBook);
        writer.write(id + "\n");
    }
}
