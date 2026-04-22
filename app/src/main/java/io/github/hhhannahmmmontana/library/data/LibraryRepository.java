package io.github.hhhannahmmmontana.library.data;

import io.github.hhhannahmmmontana.library.data.entities.AuthorScore;
import io.github.hhhannahmmmontana.library.data.entities.Book;
import io.github.hhhannahmmmontana.library.data.entities.LibraryStats;
import io.github.hhhannahmmmontana.library.data.entities.RawBook;
import io.github.hhhannahmmmontana.library.exceptions.DuplicateException;
import io.github.hhhannahmmmontana.library.exceptions.NoSuchBookException;

import java.util.*;

/**
 * Репозиторий для управления книжным фондом и расчета статистики.
 * <p>
 * Хранит книги в оперативной памяти и поддерживает кэшируемые статистические показатели
 * (самая старая/новая книга, топ авторов). Кэш инвалидируется автоматически
 * при изменении состава книг.
 */
public final class LibraryRepository {
    /** Лимит для списка наиболее популярных авторов. */
    private final static int TOP_AUTHORS_LIMIT = 3;

    /** Основное хранилище книг. */
    private final ArrayList<Book> books = new ArrayList<>();

    /** Карта счетчиков книг для каждого автора для быстрой статистики. */
    private final HashMap<String, Integer> authorsScores = new HashMap<>();

    /** Кэш самой старой книги. null означает необходимость пересчета. */
    private Book minYearBook = null;

    /** Кэш самой новой книги. null означает необходимость пересчета. */
    private Book maxYearBook = null;

    /** Кэш топа авторов. null означает необходимость пересчета. */
    private AuthorScore[] topAuthors = null;

    /** Счетчик для генерации уникальных идентификаторов книг. */
    private int idCounter = 0;

    /**
     * Добавляет новую книгу в репозиторий.
     * @param newBook данные новой книги
     * @return сгенерированный ID книги
     * @throws DuplicateException если книга с таким названием и автором уже существует
     */
    public int add(RawBook newBook) throws DuplicateException {
        for (var book : books) {
            if (newBook.bookEquals(book)) {
                throw new DuplicateException();
            }
        }
        var book = newBook.toBook(idCounter++);
        books.add(book);
        addToScore(book);
        return book.getId();
    }

    /**
     * Удаляет книгу по идентификатору.
     * При удалении сбрасывает связанные кэши статистики.
     * @param id идентификатор книги
     * @return удаленный объект книги
     * @throws NoSuchBookException если книга с таким ID не найдена
     */
    public Book remove(int id) throws NoSuchBookException {
        var len = books.size();
        Book book = null;
        for (int i = 0; i < len; ++i) {
            if (books.get(i).getId() == id) {
                book = books.remove(i);
                break;
            }
        }
        if (book == null) {
            throw new NoSuchBookException();
        }
        removeFromScore(book);
        return book;
    }

    /**
     * Выполняет поиск книг по совпадению шаблона в названии или авторе.
     * @param pattern поисковый запрос
     * @return список найденных книг
     */
    public List<Book> find(String pattern) {
        var lcPattern = pattern.toLowerCase();
        return books.stream().filter(book -> book.matches(lcPattern)).toList();
    }

    /**
     * Возвращает полный список книг в порядке их добавления.
     * @return список всех книг
     */
    public List<Book> list() {
        return books;
    }

    /**
     * Возвращает отсортированный список книг.
     * @param comparator правило сортировки
     * @return отсортированный список
     */
    public List<Book> list(Comparator<Book> comparator) {
        return books.stream().sorted(comparator).toList();
    }

    /**
     * Возвращает актуальную статистику библиотеки.
     * Использует кэширование: пересчет происходит только если данные изменились.
     * @return {@link Optional} со статистикой или пустой, если библиотека пуста
     */
    public Optional<LibraryStats> stats() {
        if (books.isEmpty()) {
            return Optional.empty();
        }
        if (minYearBook == null) {
            recalcMin();
        }
        if (maxYearBook == null) {
            recalcMax();
        }
        if (topAuthors == null) {
            recalcTops();
        }
        return Optional.of(new LibraryStats(minYearBook, maxYearBook, topAuthors));
    }

    private void recalcMin() {
        minYearBook = books.stream().min(BookComparators.BY_YEAR).orElseThrow();
    }

    private void recalcMax() {
        maxYearBook = books.stream().max(BookComparators.BY_YEAR).orElseThrow();
    }

    private void recalcTops() {
        topAuthors = authorsScores.entrySet().stream()
            .sorted(Comparator
                .comparingInt(Map.Entry<String, Integer>::getValue)
                .reversed()
            )
            .limit(TOP_AUTHORS_LIMIT)
            .map(AuthorScore::fromEntry)
            .toArray(AuthorScore[]::new);
    }

    private void addToScore(Book book) {
        if (minYearBook != null && minYearBook.getYear() < book.getYear()) {
            minYearBook = book;
        }
        if (maxYearBook != null && maxYearBook.getYear() >= book.getYear()) {
            maxYearBook = book;
        }

        var author = book.getAuthor();
        var newScore = authorsScores.getOrDefault(author, 0) + 1;
        authorsScores.put(author, newScore);
        if (topAuthors == null) {
            return;
        }
        if (topAuthors.length < TOP_AUTHORS_LIMIT || topAuthors[topAuthors.length - 1].booksAmount() < newScore) {
            topAuthors = null;
        }
    }

    private void removeFromScore(Book book) {
        if (minYearBook != null && minYearBook.getId() == book.getId()) {
            minYearBook = null;
        }
        if (maxYearBook != null && maxYearBook.getId() == book.getId()) {
            maxYearBook = null;
        }

        var author = book.getAuthor();
        var newScore = authorsScores.get(author) - 1;
        if (newScore == 0) {
            authorsScores.remove(author);
        } else {
            authorsScores.put(author, newScore);
        }
        if (topAuthors == null) {
            return;
        }
        if (topAuthors.length < TOP_AUTHORS_LIMIT || newScore >= topAuthors[TOP_AUTHORS_LIMIT - 1].booksAmount()) {
            topAuthors = null;
        }
    }
}
