package com.epam.task04.main;

import com.epam.task04.lib.view.ConsoleSessionPerformer;

/**
 * Entry point to application.
 */
public class Main {

    public static void main(String[] args) {
        ConsoleSessionPerformer performer = new ConsoleSessionPerformer();
        performer.performSession();
    }
}
