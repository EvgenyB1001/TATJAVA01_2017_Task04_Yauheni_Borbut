package com.epam.task04.lib.view;

import com.epam.task04.lib.controller.CommandController;

import java.util.Scanner;

/**
 * Class provides interface to interact with user
 */
public class ConsoleSessionPerformer {

    private static final String WELCOME_TEXT = "Hello! Type in command to perform";

    /**
     * Method gets line with request from user and resend it
     * to controller to perform request
     */
    public void performSession() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(WELCOME_TEXT);
        String line = scanner.nextLine();

        /*
         * Examples of request from user
         * String exampleRequest1 = "add Matrix,film,03:12:2007";
         * String exampleRequest2 = "find_by_category disk";
         * String exampleRequest3 = "find_by_title Matrix";
         * String exampleRequest4 = "find_by_date 13:11:2001";
         */
        CommandController controller = CommandController.getInstance();
        controller.init();
        String response1 = controller.executeRequest(line);
        System.out.println(response1);
        controller.destroy();
    }
}
