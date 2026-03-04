package main;

import main.dao.BackupData;
import main.service.ProductImpl;

import java.io.File;
import java.util.Scanner;


import static main.service.Validate.*;
import main.service.ProductImpl.*;



public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        ProductImpl proImpl = new ProductImpl();
        proImpl.getRow();
        proImpl.getTotal();


        boolean isValid = false;
        System.out.println("\t\t\t\t\t------------ Menu ------------");
        String option;
        Scanner sc = new Scanner(System.in);
        proImpl.showAllProducts();
        while (!isValid) {
            System.out.print("Enter Your Choice : ");
            option = sc.nextLine().trim().toUpperCase();

            switch (option) {

                case "N" -> proImpl.nextPage();
                case "P" -> proImpl.previousPage();
                case "F" -> proImpl.firstPage();
                case "L" -> proImpl.laterPage();
                case "G" -> proImpl.goTo(onlyDigit("page Number"));
                case "W" -> proImpl.insertProduct();
                case "R" -> proImpl.getProductById();
                case "U" -> System.out.println("Update");
                case "D" -> proImpl.deleteProduct();
                case "S" -> proImpl.searchProductByName();
                case "SE" -> proImpl.setRow(onlyDigit("Row Number"));
                case "SA" -> proImpl.saveAndUpdateProductToDb();

                case "BA" -> {
                    boolean success = BackupData.backup();

                    if (success) {
                        System.out.println(ANSI_GREEN +
                                "Backup created successfully." + ANSI_RESET);
                    } else {
                        System.err.println(ANSI_RED + "Backup failed!" + ANSI_RESET);
                    }
                    proImpl.firstPage();
                }

                case "RE" -> {
                    File backupFolder = new File(BackupData.BACKUP_FOLDER);
                    String[] backups = backupFolder.list((dir, name) -> name.endsWith(".backup"));

                    if (backups != null && backups.length > 0) {
                        System.out.println("Available backup versions:");
                        for (String b : backups) {
                            // Extract version from filename: backup_v<version>.backup
                            String version = b.substring(7, b.length() - 7);
                            System.out.print(ANSI_GREEN + "This is version on backUp file" + ANSI_RESET);
                            System.out.println(" - " + version);
                        }
                    } else {
                        System.out.println("No backups available!");
                        break;
                    }

                    System.out.print("Enter version to restore  ex (V1, ...) : ");
                    String version = sc.nextLine().trim();

                    System.out.println(ANSI_RED + "WARNING: This will erase current data!" + ANSI_RESET);
                    System.out.print("Are you sure? (Y/N): ");
                    String confirm = sc.nextLine().trim().toUpperCase();

                    if (confirm.equals("Y")) {

                        boolean restored = BackupData.restore(version);

                        if (restored) {
                            System.out.println(ANSI_GREEN + "Restore successful. continue using the system." + ANSI_RESET);

                        } else {
                            System.out.println(ANSI_RED + "Restore failed!" + ANSI_RESET);
                        }
                    }
                    proImpl.firstPage();
                }

                case "E" -> {
                    if (yesOrNo()) {
                        System.out.println("SYSTEM Exit.......!");
                        isValid = true;
                    }
                    proImpl.firstPage();

                }

                default -> System.out.println(ANSI_RED + "\nInvalid Input\n" + ANSI_RESET);
            }
        }
    }


}