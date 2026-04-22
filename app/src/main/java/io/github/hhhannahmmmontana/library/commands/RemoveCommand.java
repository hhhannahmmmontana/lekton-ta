package io.github.hhhannahmmmontana.library.commands;

import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.exceptions.InvalidCommandFormatException;
import io.github.hhhannahmmmontana.library.exceptions.NoSuchBookException;

import java.io.IOException;
import java.io.Writer;

/**
 * Команда для удаления книги из библиотеки по её уникальному идентификатору.
 * @param id уникальный номер книги, которую необходимо удалить
 */
public record RemoveCommand(int id) implements Command {
    public static RemoveCommand fromStringArg(String arg)
        throws InvalidCommandFormatException {
        try {
            if (arg == null) {
                throw new InvalidCommandFormatException();
            }
            var id = Integer.parseInt(arg);
            return new RemoveCommand(id);
        } catch (NumberFormatException ex) {
            throw new InvalidCommandFormatException();
        }
    }

    @Override
    public void execute(
        LibraryRepository repository,
        Writer writer
    ) throws IOException, NoSuchBookException {
        var book = repository.remove(id);
        writer.write(book.toString() + '\n');
    }
}
