package io.github.hhhannahmmmontana.library;

import io.github.hhhannahmmmontana.library.data.LibraryRepository;
import io.github.hhhannahmmmontana.library.data.entities.RawBook;
import io.github.hhhannahmmmontana.library.exceptions.DuplicateException;
import io.github.hhhannahmmmontana.library.exceptions.NoSuchBookException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор интеграционных и компонентных тестов для проверки логики библиотеки.
 * <p>
 * Тестирует ключевые компоненты: {@link LibraryRepository} (бизнес-логика)
 * и {@link LibraryIOHandler} (взаимодействие с пользователем).
 */
public final class AppTest {
    /** Репозиторий, пересоздаваемый перед каждым тестом для чистоты окружения. */
    private LibraryRepository repository;

    @BeforeEach
    void setup() {
        repository = new LibraryRepository();
    }

    /**
     * Проверка корректности обработки ошибок репозитория.
     * <p>
     * Сценарий:<br/>
     * 1. Успешное добавление книги.<br/>
     * 2. Выброс {@link DuplicateException} при попытке добавить дубликат.<br/>
     * 3. Успешное удаление.<br/>
     * 4. Выброс {@link NoSuchBookException} при повторном удалении того же ID.
     */
    @Test
    void testErrors() {
        assertDoesNotThrow(() -> {
            var rawBook = new RawBook("Title A", "Author A", 1970);
            var id = repository.add(rawBook);
            assertThrows(DuplicateException.class, () -> {
                var rawDuplicate = new RawBook("Title A", "Author A", 1970);
                repository.add(rawDuplicate);
            });
            repository.remove(id);
            assertThrows(NoSuchBookException.class, () -> repository.remove(id));
        });
    }

    /**
     * Проверка корректности расчета и инвалидации кэша статистики.
     * <p>
     * Сценарий:<br/>
     * 1. Наполнение репозитория книгами.<br/>
     * 2. Проверка соответствия самой старой/новой книги и топа авторов.<br/>
     * 3. Удаление книги и проверка автоматического пересчета кэшированных данных.
     */
    @Test
    void testStatistics() {
        assertDoesNotThrow(() -> {
            var bookAA = new RawBook("Title A", "Author A", 1970);
            var bookBB = new RawBook("Title B", "Author B", 1971);
            var bookCA = new RawBook("Title C", "Author A", 1972);
            repository.add(bookAA);
            repository.add(bookBB);
            var bookCAId = repository.add(bookCA);

            var stats = repository.stats();
            assertTrue(stats.isPresent());
            assertEquals(bookAA.year(), stats.get().oldestBook().getYear());
            assertEquals(bookCA.year(), stats.get().newestBook().getYear());
            assertEquals(2, stats.get().topScores().length);
            assertEquals(2, stats.get().topScores()[0].booksAmount());
            assertEquals(bookAA.author(), stats.get().topScores()[0].authorsName());

            repository.remove(bookCAId);
            stats = repository.stats();
            assertTrue(stats.isPresent());
            assertEquals(bookBB.year(), stats.get().newestBook().getYear());
            assertEquals(2, stats.get().topScores().length);
            assertEquals(1, stats.get().topScores()[0].booksAmount());
        });
    }

    /**
     * Проверка работы обработчика ввода-вывода (IO) и парсинга команд.
     * <p>
     * Имитирует поток ввода пользователя, проверяет фильтрацию некорректных форматов
     * и корректное завершение работы по команде EXIT.
     */
    @Test
    void testIO() {
        String simulatedInput =
            """
            ADD Title A;Author A;1970
            ADD Invalid format
            EXIT
            """;

        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LibraryIOHandler handler = new LibraryIOHandler(in, out, repository);
        final var MAX_ITER = 3;
        var iter = 0;
        while (!handler.isStopped()) {
            handler.execute();
            assertTrue(iter++ < MAX_ITER);
        }
        String consoleOutput = out.toString();
        var books = repository.list();
        assertEquals(1, books.size());
        assertEquals("Author A", repository.list().getFirst().getAuthor());
        assertTrue(consoleOutput.toLowerCase().contains("error"));
    }
}