package main;

import java.util.Scanner;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {

        boolean isValid = false;
        System.out.println("\t\t\t\t\t------------ Menu ------------");
        String option;
        Scanner sc = new Scanner(System.in);
        while (!isValid) {
            ProductImpl.showAllProducts();
            System.out.print(ANSI_GREEN + "N." + ANSI_RESET + " Next Page");
            System.out.print(ANSI_GREEN + "\tP. " + ANSI_RESET + "Previous Page");
            System.out.print(ANSI_GREEN + "\tF. " + ANSI_RESET + "First Page");
            System.out.print(ANSI_GREEN + "\tL. " + ANSI_RESET + "Last Page");
            System.out.print(ANSI_GREEN + "\tG. " + ANSI_RESET + "Goto");
            System.out.print("\n\n");
            System.out.print(ANSI_GREEN + "W. " + ANSI_RESET + "Write");
            System.out.print(ANSI_GREEN + "\tR. " + ANSI_RESET + "Read");
            System.out.print(ANSI_GREEN + "\tU. " + ANSI_RESET + "Update");
            System.out.print(ANSI_GREEN + "\tD. " + ANSI_RESET + "Delete");
            System.out.print(ANSI_GREEN + "\tS. " + ANSI_RESET + "Search (Name)");
            System.out.print(ANSI_GREEN + "\tSe. " + ANSI_RESET + "Set Row\n\n");
            System.out.print(ANSI_GREEN + "Sa. " + ANSI_RESET + "Save");
            System.out.print(ANSI_GREEN + "\tUn. " + ANSI_RESET + "Unsave");
            System.out.print(ANSI_GREEN + "\tRe. " + ANSI_RESET + "Restore");
            System.out.print(ANSI_GREEN + "\tE. " + ANSI_RESET + "Exit\n");

            System.out.print("Enter Your Choice : " );
            option = sc.nextLine().trim().toUpperCase();

            switch (option){
                case "N" -> System.out.println("Next Page");
                case "P" -> System.out.println("Previous Page");
                case "F" -> System.out.println("First Page");
                case "L" -> System.out.println("Last Page");
                case "G" -> System.out.println("Goto");

                case "W" -> System.out.println("Write");
                case "R" -> System.out.println("Read");
                case "U" -> System.out.println("Update");
                case "D" -> System.out.println("Restore");
                case "S" -> System.out.println("Search (Name)");
                case "Se" -> System.out.println("Set Row");
                case "Sa" -> System.out.println("Save");
                case "Un" -> System.out.println("Unsave");
                case "Re" -> System.out.println("Restore");
                case "E" -> System.out.println("Exit");
                default -> System.out.println(ANSI_RED + "\nInvalid Input\n" + ANSI_RESET);
            }
        }
    }
}