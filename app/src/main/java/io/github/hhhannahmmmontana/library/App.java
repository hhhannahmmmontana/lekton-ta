package io.github.hhhannahmmmontana.library;

import io.github.hhhannahmmmontana.library.data.LibraryRepository;

public final class App {
    public static void main(String[] args) {
        var repository = new LibraryRepository();
        var app = new LibraryIOHandler(System.in, System.out, repository);
        while (!app.isStopped()) {
            app.execute();
        }
    }
}
