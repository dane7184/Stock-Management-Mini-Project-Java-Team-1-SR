package main;

import main.service.ProductImpl;
import java.util.Scanner;

import static main.service.ProductImpl.*;
import static main.service.Validate.*;

public class Main {



    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        getRow();
        getTotal();


        boolean isValid = false;
        table("Main");
        String option;
        Scanner sc = new Scanner(System.in);
            ProductImpl.showAllProducts();
        while (!isValid) {


            System.out.print("Enter Your Choice : " );
            option = sc.nextLine().trim().toUpperCase();

            switch (option){
                //seth
                case "N" -> nextPage();
                case "P" -> previousPage();
                case "F" -> firstPage();
                case "L" -> laterPage();
                case "G" -> goTo(onlyDigit("page Number"));
                case "W" -> System.out.println("Write");
                case "R" -> System.out.println("Read");
                case "U" -> System.out.println("Update");
                case "D" -> System.out.println("Restore");
                case "S" -> System.out.println("Search (Name)");
                case "SE" -> setRow(onlyDigit("Row Number"));
                case "SA" -> System.out.println("Save");
                case "UN" -> System.out.println("Unsave");
                case "RE" -> System.out.println("Restore");
                case "E" -> System.out.println("Exit");
                default -> System.out.println(ANSI_RED + "\nInvalid Input\n" + ANSI_RESET);
            }
        }
    }
}