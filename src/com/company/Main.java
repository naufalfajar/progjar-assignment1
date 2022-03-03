package com.company;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        loop: while(true) {
            Scanner read = new Scanner(System.in);
            System.out.println(
                    """
                            Bismillah Jago Progjar
                            1. Open a webpage given a URI and shows the text
                            2. Show a list of clickable links
                            3. Download a file regardless its size
                            4. Download a file in parallel
                            5. Follow redirections
                            6. Show respective HTTP error message
                            7. Open a webpage that is protected by HTTP basic auth
                            8. Quit"""
            );
            String choice = read.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.println("Type URI address");
                    String input = read.nextLine();
                    Task task = new Task();
                    task.no1(input);
                }
                case "2" -> {
                    System.out.println("Type URI address");
                    String input = read.nextLine();
                    Task task = new Task();
                    task.no2(input);
                }
                case "3" -> {
                    System.out.println("Type URI address");
                    String input = read.nextLine();
                    Task task = new Task();
                    task.no3(input);
                }
                case "4" -> System.out.println("Not yet implemented!");
                case "5" -> {
                    System.out.println("Type URI address");
                    String input = read.nextLine();
                    Task task = new Task();
                    task.no5(input);
                }
                case "6" -> {
                    System.out.println("Type URI address");
                    String input = read.nextLine();
                    Task task = new Task();
                    task.no6(input);
                }
                case "7" -> System.out.println("Hallo");
                case "8" -> {break loop;}
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}

