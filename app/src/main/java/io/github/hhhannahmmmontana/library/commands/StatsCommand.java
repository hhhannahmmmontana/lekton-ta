package io.github.hhhannahmmmontana.library.commands;

import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.exceptions.InvalidCommandFormatException;

import java.io.IOException;
import java.io.Writer;

/**
 * Команда для формирования и вывода статистических данных по библиотеке.
 * <p>
 * Подсчитывает общее количество книг, распределение по авторам или годам.
 */
public final class StatsCommand implements Command {
    public static StatsCommand fromStringArg(String arg)
        throws InvalidCommandFormatException {
        try {
            if (arg != null) {
                throw new InvalidCommandFormatException();
            }
            return new StatsCommand();
        } catch (NumberFormatException ex) {
            throw new InvalidCommandFormatException();
        }
    }

    @Override
    public void execute(
        LibraryRepository repository,
        Writer writer
    ) throws IOException {
        var optional = repository.stats();
        if (optional.isPresent()) {
            var stats = optional.get();
            writer.write("oldest book: " + stats.oldestBook() + '\n');
            writer.write("newest book: " + stats.newestBook() + '\n');
            writer.write("top 3 authors:\n");
            var scores = stats.topScores();
            var index = 1;
            for (var score : scores) {
                writer.write(
                    "\t" + index++ + ". "
                    + score.authorsName() + " : "
                    + score.booksAmount() + " books\n"
                );
            }
        } else {
            writer.write("no stats\n");
        }
    }
}
