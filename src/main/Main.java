package main;

import main.service.ProductImpl;
import java.util.Scanner;

import static main.service.Validate.*;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        ProductImpl proImlp = new  ProductImpl();
        proImlp.getRow();
        proImlp.getTotal();

        ProductImpl productImpl = new ProductImpl();

        boolean isValid = false;
        System.out.println("\t\t\t\t\t------------ Menu ------------");
        String option;
        Scanner sc = new Scanner(System.in);
        proImlp.showAllProducts();
        while (!isValid) {
            System.out.print("Enter Your Choice : " );
            option = sc.nextLine().trim().toUpperCase();

            switch (option){
                //seth
                case "N" -> proImlp.nextPage();
                case "P" -> proImlp.previousPage();
                case "F" -> proImlp.firstPage();
                case "L" -> proImlp.laterPage();
                case "G" -> proImlp.goTo(onlyDigit("page Number"));
                case "W" -> proImlp.insertProduct();
                case "R" -> proImlp.getProductById();
                case "U" -> System.out.println("Update");
                case "D" -> proImlp.deleteProduct();
                case "S" -> proImlp.searchProductByName();
                case "SE" -> proImlp.setRow(onlyDigit("Row Number"));
                case "SA" -> productImpl.saveAndUpdateProductToDb();
                case "UN" -> System.out.println("Unsave");
                case "RE" -> System.out.println("Restore");
                case "E" ->{
                System.out.println("SYSTEM Exit.......!");
                        isValid=true;
                }
                default -> System.out.println(ANSI_RED + "\nInvalid Input\n" + ANSI_RESET);
            }
        }
    }
}