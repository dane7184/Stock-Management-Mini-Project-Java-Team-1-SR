package main.service;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

import static main.lib.Color.*;

public class Validate {
    static Scanner sc = new Scanner(System.in);
    public static void table(String content) {
        CellStyle text = new CellStyle(CellStyle.HorizontalAlign.center);
        org.nocrala.tools.texttablefmt.Table t = new org.nocrala.tools.texttablefmt.Table(1, BorderStyle.UNICODE_ROUND_BOX, ShownBorders.ALL);
        t.setColumnWidth(0, 50, 200);
        t.addCell(content, text);
        System.out.println(C_BLUE + t.render() + C_RESET);
    }

    public static String onlyLetter(String reason) {
        while (true) {

            System.out.print(C_GREEN + "Enter " + reason + " : " + C_RESET);
            String letter = sc.nextLine().trim();

            if (Pattern.matches("[a-zA-Z]+ {0,1}[a-zA-Z]+", letter)) {
                return letter;
            }
            System.out.println(C_RED + "Invalid input (only 1 space and letter ). Try again" + C_RESET);
        }

    }

    public static String valName(String reason) {
        while (true) {

            System.out.print(C_GREEN + "Enter " + reason + " : " + C_RESET);
            String letter = sc.nextLine().trim();

            if (Pattern.matches("[a-zA-Z]+ {1}[a-zA-Z]+", letter)) {
                return letter;

            }
            System.out.println(C_RED + "Invalid input (only 1 space and it's letter ). Try again" + C_RESET);
        }

    }

    public static boolean yesOrNo() {
        while (true) {

            System.out.print(C_CYAN + "Do you want to continue? (y/n) : " + C_RESET);
            String letter = sc.nextLine();

            if (letter.equals("y")) {
                return true;
            } else if (letter.equals("n")) {
                return false;
            } else {
                System.out.println(C_RED + "Invalid input. Try again.(y/n)" + C_RESET);
            }

        }


    }

    public static int onlyDigit(String reason) {
        while (true) {

            System.out.print(C_GREEN + "Enter " + reason + " : " + C_RESET);
            String Digit = sc.nextLine();

            if (Pattern.matches("[0-9]+", Digit)) {
                return Integer.parseInt(Digit);
            }
            System.out.println(C_RED + "Invalid input. Try again.(onlyDigit)" + C_RESET);
        }

    }

    public static int qty(String reason) {
        while (true) {

            System.out.print(C_GREEN + "Enter " + reason + " : " + C_RESET);
            String Digit = sc.nextLine();

            if (Pattern.matches("^[1-9]{1}[0-9]*", Digit)) {
                return Integer.parseInt(Digit);

            }
            System.out.println(C_RED + "Invalid input. Quantity should start from 1" + C_RESET);
        }


    }


    public static double onlyDouble(String reason) {
        while (true) {

            System.out.print(C_GREEN + "Enter " + reason + " : " + C_RESET);
            String Digit = sc.nextLine();

            if (Pattern.matches("[1-9]{1}[0-9]+", Digit)) {
                return Double.parseDouble(Digit);

            } else if (Pattern.matches("[1-9]{1}[0-9]+.{1}[0-9]{1,2}", Digit)) {
                return Double.parseDouble(Digit);

            } else if (Pattern.matches("[0].{1}[0-9]{1,2}", Digit)) {
                return Double.parseDouble(Digit);

            } else {
                System.out.println(C_RED + "Invalid input. Try again.(Decimal)" + C_RESET);
            }
        }
    }

    public static String formattedDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy HH:mm:ss");
        return now.format(formatter);

    }
}
