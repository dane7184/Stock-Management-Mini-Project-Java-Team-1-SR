package main.service;

import main.lib.Color;
import main.lib.DbConnection;
import main.model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static main.lib.Color.C_RED;
import static main.lib.Color.C_RESET;

public class ProductImpl {

    private static DbConnection dbCon = new DbConnection();
    //static Product product = new Product();

    public static void showAllProducts() {
        try (Connection connection = dbCon.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tb_product");
            CellStyle text = new CellStyle(CellStyle.HorizontalAlign.center);
            Table table = new Table(5, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
            table.setColumnWidth(0, 18, 30);
            table.setColumnWidth(1, 18, 30);
            table.setColumnWidth(2, 18, 30);
            table.setColumnWidth(3, 18, 30);
            table.setColumnWidth(4, 18, 30);
            table.addCell("ID",text);
            table.addCell("Name",text);
            table.addCell("Price",text);
            table.addCell("Quantity",text);
            table.addCell("Import Date",text);

            while(resultSet.next()) {
                table.addCell(String.valueOf(resultSet.getInt("id")),text);
                table.addCell(resultSet.getString("name"),text);
                table.addCell(String.valueOf(resultSet.getString("unit_price")),text);
                table.addCell(String.valueOf(resultSet.getString("stock_qty")),text);
                table.addCell(String.valueOf(resultSet.getString("import_date")),text);
            }

            System.out.println(table.render());
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void deleteProduct(){
        Scanner scanner =  new Scanner(System.in);
        try (Connection connection = dbCon.dataSource().getConnection()) {
            int searchId = Validate.onlyDigit("ID to Delete");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tb_product where id = " + searchId);
            CellStyle text = new CellStyle(CellStyle.HorizontalAlign.center);
            Table table = new Table(5, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
            table.setColumnWidth(0, 18, 30);
            table.setColumnWidth(1, 18, 30);
            table.setColumnWidth(2, 18, 30);
            table.setColumnWidth(3, 18, 30);
            table.setColumnWidth(4, 18, 30);
            table.addCell("ID",text);
            table.addCell("Name",text);
            table.addCell("Price",text);
            table.addCell("Quantity",text);
            table.addCell("Import Date",text);


            while(resultSet.next()) {
                table.addCell(String.valueOf(resultSet.getInt("id")),text);
                table.addCell(resultSet.getString("name"),text);
                table.addCell(String.valueOf(resultSet.getString("unit_price")),text);
                table.addCell(String.valueOf(resultSet.getString("stock_qty")),text);
                table.addCell(String.valueOf(resultSet.getString("import_date")),text);
            }

            List<Integer> productId = new ArrayList<>();
            ResultSet proId = statement.executeQuery("SELECT id FROM tb_product");
            while (proId.next()) {
                productId.add(proId.getInt("id"));
            }

            if ( !productId.contains(searchId)){
                System.out.println(Color.C_RED + "No Record Was Found With Id : " + searchId + Color.C_RESET);
            }else {
                System.out.println(table.render());
                boolean deleted = true;
                String letter;
                do {
                    System.out.print(Color.C_GREEN + "Are You Sure to delete product id : " + searchId + " ? (y /n ) : " + C_RESET);
                    letter = scanner.nextLine().toLowerCase();
                    if (letter.equals("y")){
                        statement.executeUpdate("DELETE FROM tb_product where id = " + searchId);
                        System.out.println(Color.C_GREEN + "Delete Successfully!" + C_RESET);
                        deleted = false;
                    }else if (letter.equals("n")){
                        deleted = false;
                    }else{
                        System.out.println(C_RED + "Invalid input. Try again.(y/n)" + C_RESET);
                    }
                }while (deleted);
            }

            Validate.yesOrNo();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
