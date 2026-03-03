package main.service;

import main.lib.Color;
import main.lib.DbConnection;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static main.Main.ANSI_GREEN;
import static main.Main.ANSI_RESET;
import static main.lib.Color.C_RED;
import static main.lib.Color.C_RESET;

public class ProductImpl implements ProductServer{
    static int startId ;

    static boolean lastPage = false;
    static boolean next = true;
    static int tmpLowerId;
    static int lowerId;
    static int startPage = 1;
    static int totalRow = 0;
    static int total;
    static int row;
    static int totalPage;
    static int id = 0;

    private static DbConnection dbCon = new DbConnection();

    @Override
    public void getTotal() {
        try (Connection connection = dbCon.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(*) as total FROM tb_product");
            while (resultSet.next()) {
                total = resultSet.getInt(1);
                if(total%row==0){

                totalPage = total / row;
                }
                else {
                    totalPage = (total / row) + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAllProducts() {
        totalRow = 0;

        try (Connection connection = dbCon.dataSource().getConnection()) {
            String sign;
            String sql = "";
            if (next) {
                sign = ">=";
            } else {
                sign = "<=";
            }
            if (sign.equals(">=") && !lastPage) {
                sql = "SELECT * FROM tb_product where id >= " + id + " order by id";
            } else if(sign.equals("<=") && !lastPage) {
                sql = "SELECT * FROM (SELECT * FROM tb_product WHERE id < " + id + " ORDER BY id DESC limit " + row + ") ORDER BY id ASC; ";
            }
            else {
                int ls = total%row;
                sql = "SELECT * FROM (SELECT * FROM tb_product WHERE id <= " + id + " ORDER BY id DESC limit " + ls + ") ORDER BY id ASC; ";
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            CellStyle text = new CellStyle(CellStyle.HorizontalAlign.center);
            Table table = new Table(5, BorderStyle.UNICODE_BOX, ShownBorders.ALL);
            table.setColumnWidth(0, 18, 30);
            table.setColumnWidth(1, 18, 30);
            table.setColumnWidth(2, 18, 30);
            table.setColumnWidth(3, 18, 30);
            table.setColumnWidth(4, 18, 30);
            table.addCell("ID", text);
            table.addCell("Name", text);
            table.addCell("Price", text);
            table.addCell("Quantity", text);
            table.addCell("Import Date", text);

            while (resultSet.next()) {
                if (totalRow < row) {
                    if (totalRow == 0) {
                        tmpLowerId = resultSet.getInt(1);
                    }
                    table.addCell(String.valueOf(resultSet.getInt("id")), text);
                    table.addCell(resultSet.getString("name"), text);
                    table.addCell(String.valueOf(resultSet.getString("unit_price")), text);
                    table.addCell(String.valueOf(resultSet.getString("stock_qty")), text);
                    table.addCell(String.valueOf(resultSet.getString("import_date")), text);
                    totalRow++;
                } else if (totalRow == row) {
                    id = resultSet.getInt("id");
                    break;
                }


            }
            table.addCell(" PAGE : " + startPage + "/" + totalPage, text, 2);
            table.addCell("TOTAL RECORD : " + total, text, 3);

            System.out.println(table.render());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextPage() {

        try (Connection connection = dbCon.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(*) as total FROM tb_product");
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        totalRow = 0;
        next = true;
        if (startPage < totalPage) {
            startPage++;
        }
        lowerId = id;
        System.out.println("Lower : " + lowerId);

        showAllProducts();
    }

    public void getRow() {
        try (Connection connection = dbCon.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultRow = statement.executeQuery("SELECT * FROM tb_set_row");
            while (resultRow.next()) {
                row = resultRow.getInt(1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void previousPage() {
        if (startPage > 1) {
            startPage--;
            id = tmpLowerId;
        }
        next = false;
        showAllProducts();
    }

    @Override
    public void firstPage() {
        getRow();
        getTotal();
        try (Connection connection = dbCon.dataSource().getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM tb_product order by id limit 1");
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            System.out.println("First Page : " + id);
            next=true;
            startPage = 1;
            showAllProducts();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void laterPage() {
        try (Connection connection = dbCon.dataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM tb_product order by id asc;");
            while (resultSet.next()) {
                id = resultSet.getInt(1);

            }
            next = false;
            lastPage = true;
            startPage = totalPage;
            showAllProducts();
            lastPage = false;
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void goTo(int pageNumber) {
        if (pageNumber > totalPage) {
            System.out.println("Page number is greater than total page");
            return;
        }
        try (Connection connection = dbCon.dataSource().getConnection()) {
            String sql = "SELECT * FROM tb_product order by id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            int rowSelected = row * pageNumber;
            while (resultSet.next()) {
                if (totalRow < rowSelected) {
                    if (totalRow == 0) {
                        tmpLowerId = resultSet.getInt(1);
                    }
                    totalRow++;
                } else if (totalRow == rowSelected) {
                    id = resultSet.getInt("id");
                    break;
                }
            }
            startPage = pageNumber;
            showAllProducts();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRow(int rowNumber) {
        if (row > total) {
            System.out.println("Row number is greater than total product");
        }
        else {

            try (Connection connection = dbCon.dataSource().getConnection()) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("update tb_set_row set row=" + rowNumber);
                ResultSet rs = statement.executeQuery("select id from tb_product order by id desc ");
                while (rs.next()) {
                    startId = rs.getInt("id");
                }
                startPage=1;
                next=true;

                id = startId;
                getRow();
                getTotal();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        showAllProducts();
    }

    @Override
    public void deleteProduct(){
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

            System.out.print("Press to continue....");
            scanner.nextLine();

            firstPage();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void searchProductByName() {
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
