package main.service;

import main.lib.DbConnection;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.util.Scanner;

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
    public static void searchProductByName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        try (Connection connection = dbCon.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT id, name, unit_price, stock_qty, import_date " +
                             "FROM tb_product " +
                             "WHERE name ILIKE ? " +
                             "ORDER BY id"
             )) {

            ps.setString(1, "%" + name.trim() + "%");

            try (ResultSet resultSet = ps.executeQuery()) {
                CellStyle text = new CellStyle(CellStyle.HorizontalAlign.center);
                Table table = new Table(5, BorderStyle.UNICODE_BOX, ShownBorders.ALL);

                table.setColumnWidth(0, 18, 30);   // ID
                table.setColumnWidth(1, 18, 30);  // Name
                table.setColumnWidth(2, 18, 30);  // Price
                table.setColumnWidth(3, 18, 30);  // Qty
                table.setColumnWidth(4, 18, 30);  // Import Date

                table.addCell("ID", text);
                table.addCell("Name", text);
                table.addCell("Price", text);
                table.addCell("Quantity", text);
                table.addCell("Import Date", text);

                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    table.addCell(String.valueOf(resultSet.getInt("id")), text);
                    table.addCell(resultSet.getString("name"), text);
                    table.addCell(String.valueOf(resultSet.getDouble("unit_price")), text);
                    table.addCell(String.valueOf(resultSet.getInt("stock_qty")), text);
                    table.addCell(String.valueOf(resultSet.getDate("import_date")), text);
                }

                if (!found) {
                    System.out.println("No products found matching: " + name.trim());
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    return;
                }

                System.out.println(table.render());
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
