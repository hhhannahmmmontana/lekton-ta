package io.github.hhhannahmmmontana.library.commands;

import io.github.hhhannahmmmontana.library.data.BookComparators;
import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.data.entities.Book;
import io.github.hhhannahmmmontana.library.exceptions.InvalidCommandFormatException;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Команда для вывода списка всех книг с определенной сортировкой.
 * @param sortingType тип сортировки (например, по автору, названию или году)
 */
public record ListCommand(SortingType sortingType) implements Command {
    public enum SortingType {
        TITLE, AUTHOR, YEAR, NONE;

        public SortingType fromString(String str) {
            return SortingType.valueOf(str.toUpperCase());
        }
    }

    public static ListCommand fromStringArg(String arg)
        throws InvalidCommandFormatException {
        try {
            SortingType sortingType;
            if (arg != null) {
                sortingType = SortingType.valueOf(arg);
            } else {
                sortingType = SortingType.NONE;
            }
            return new ListCommand(sortingType);
        } catch (IllegalArgumentException ex) {
            throw new InvalidCommandFormatException();
        }
    }

    @Override
    public void execute(
        LibraryRepository repository,
        Writer writer
    ) throws IOException {
        var comparator = switch (sortingType) {
            case TITLE -> BookComparators.BY_TITLE;
            case AUTHOR -> BookComparators.BY_AUTHOR;
            case YEAR -> BookComparators.BY_YEAR;
            case NONE -> null;
        };
        List<Book> list;
        if (comparator == null) {
            list = repository.list();
        } else {
            list = repository.list(comparator);
        }
        if (list.isEmpty()) {
            writer.write("empty list\n");
        }
        for (var l : list) {
            writer.write(l.toString() + '\n');
        }
    }
}
