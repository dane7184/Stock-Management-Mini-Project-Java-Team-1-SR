package main.service;

import main.lib.DbConnection;
import main.model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ProductImpl implements ProductServer {
    private static DbConnection dbCon = new DbConnection();
    List<Product> products = new ArrayList<>();

    @Override
    public void showAllProducts() {
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

    @Override
    public void unsaveInsertProduct(Product product) {
        products.add(product);
        System.out.println("unsave insert product");
    }

    @Override
    public void showUnsavedProducts() {

        if (products.isEmpty()){
            System.out.println("No unsaved products.");
            return;
        }

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

        for (Product product : products) {
            table.addCell(String.valueOf(product.getId()),text);
            table.addCell(product.getName(),text);
            table.addCell(String.valueOf(product.getPrice()),text);
            table.addCell(String.valueOf(product.getQty()),text);
            table.addCell(String.valueOf(product.getDate()),text);
        }

        System.out.println(table.render());
    }

    @Override
    public void insertProduct() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product Price: ");
        double price;
        while (true) {
            System.out.print("Enter product Price: ");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                if (price < 0) {
                    System.out.println("Price cannot be negative");
                    continue;
                }
                break;
            } else {
                System.out.println("Please enter a number.");
                scanner.next();
            }
        }

        System.out.print("Enter product Quantity: ");
        int quantity;
        while (true) {
            System.out.print("Enter product Quantity: ");
            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
                if (quantity < 0) {
                    System.out.println("Quantity can not be negative");
                    continue;
                }
                break;
            } else {
                System.out.println("Please enter an integer.");
                scanner.next();
            }
        }

        Date date = new Date();

        Product product = new Product(name, price, quantity, date);

        unsaveInsertProduct(product);
        showUnsavedProducts();
    }

    @Override
    public void saveInsertProduct() {
        if (products.isEmpty()) {
            System.out.println("No products to save.");
            return;
        }

        String sql = "INSERT INTO tb_product(name, unit_price, stock_qty, import_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbCon.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (Product product : products) {

                ps.setString(1, product.getName());
                ps.setDouble(2, product.getPrice());
                ps.setInt(3, product.getQty());
                ps.setDate(4, new java.sql.Date(product.getDate().getTime()));

                ps.executeUpdate();
            }

            products.clear();
            System.out.println("Save successful!");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveAndUpdateProductToDb() {
        System.out.println("UI for Update Insert To Database And UU Update date to database");
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("UI  : Insert Unsaved To Database");
            System.out.println("UU  : Update Product In Database");
            System.out.println("E   : Exit");
            System.out.print("\nEnter product option: ");
            String option = scanner.nextLine().trim().toUpperCase();
            switch (option) {
                case "UI" -> {
                    showUnsavedProducts();

                    if (products.isEmpty()) {
                        System.out.println("No unsaved products.");
                        break;
                    }

                    System.out.print("Save to database? (Y/N): ");
                    String yN = scanner.nextLine().trim();

                    if (yN.equalsIgnoreCase("Y")) {
                        saveInsertProduct();
                    } else {
                        System.out.println("Products were not saved.");
                    }
                }

                case "UU" -> {
                    System.out.println("Update feature coming soon...");
                }

                case "E" -> {
                    isRunning = false;
                    System.out.println("Exit Save/Update Menu.");
                }

                default -> System.out.println("Invalid option!");
            }
        }
    }
}
