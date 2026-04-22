package io.github.hhhannahmmmontana.library.commands;

import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.exceptions.InvalidCommandFormatException;

import java.io.IOException;
import java.io.Writer;

/**
 * Команда для поиска книг по названию и автору.
 * @param pattern строка для поиска
 */
public record FindCommand(String pattern) implements Command {
    public static FindCommand fromStringArg(String arg)
        throws InvalidCommandFormatException {
        try {
            if (arg == null) {
                throw new InvalidCommandFormatException();
            }
            return new FindCommand(arg);
        } catch (NumberFormatException ex) {
            throw new InvalidCommandFormatException();
        }
    }

    @Override
    public void execute(
        LibraryRepository repository,
        Writer writer
    ) throws IOException {
        var books = repository.find(pattern);
        if (books.isEmpty()) {
            writer.write("empty list\n");
        }
        for (var book : books) {
            writer.write(book.toString() + '\n');
        }
    }
}
