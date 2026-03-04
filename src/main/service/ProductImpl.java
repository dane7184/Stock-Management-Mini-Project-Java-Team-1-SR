package main.service;

import main.lib.Color;
import main.lib.DbConnection;
import main.model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.util.*;

import static main.Main.ANSI_GREEN;
import static main.Main.ANSI_RESET;
import static main.lib.Color.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

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
    static Map<Integer, Product> unsaveUpdate = new HashMap<>();
    List<Product> products = new ArrayList<>();

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
                System.out.println(C_RED + "No Record Was Found With Id : " + searchId + C_RESET);
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
                    saveUpdateProduct();
                }

                case "E" -> {
                    isRunning = false;
                    System.out.println("Exit Save/Update Menu.");
                }

                default -> System.out.println("Invalid option!");
            }
        }
    }

    @Override
    public void updateProduct() {
        Scanner scanner = new Scanner(System.in);
        String query = "SELECT * FROM tb_product WHERE id = ?";
        int updateId = Validate.onlyDigit("ID to update");
        Product productToUpdate = null;

        if (unsaveUpdate.containsKey(updateId)){
            System.out.println(C_GREEN + "ID already exists in unsave update. Wanna re-update?" + C_RESET);
            boolean isNo = Validate.yesOrNo();
            if (!isNo) {
                return;
            }
            productToUpdate = unsaveUpdate.get(updateId);

        } else {
            try (Connection connection = dbCon.dataSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, updateId);
                ResultSet resultSet = ps.executeQuery();

                    if (!resultSet.next()) {
                        System.out.println(C_RED + "No Record Was Found With Id: " + updateId + C_RESET);
                        return;
                    }

                productToUpdate = new Product(
                        updateId,
                        resultSet.getString("name"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getInt("stock_qty"),
                        resultSet.getDate("import_date")
                );
            } catch (Exception e){
                e.printStackTrace();
            }
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

        table.addCell(String.valueOf(updateId),text);
        table.addCell(productToUpdate.getName(),text);
        table.addCell(String.valueOf(productToUpdate.getPrice()),text);
        table.addCell(String.valueOf(productToUpdate.getQty()),text);
        table.addCell(String.valueOf(productToUpdate.getDate()),text);
        System.out.println(table.render());

        boolean isRunning = true;
        do {
            System.out.printf("%-5s %15s %10s %15s %10s", "1. Name", "2. Unit Price", "3. Qty", "4. All field", "5. Exit\n");
            System.out.print("Choose an option to update: ");
            String userChoice = scanner.nextLine();
            switch (userChoice){
                case "1" -> {
                    System.out.print("Enter new product name: ");
                    productToUpdate.setName(scanner.nextLine());
                    System.out.println(Color.C_GREEN + "Updated successfully!" + C_RESET);
                }
                case "2" -> {
                    productToUpdate.setPrice(Validate.onlyDouble("new product price"));
                    System.out.println(Color.C_GREEN + "Updated successfully!" + C_RESET);
                }
                case "3" -> {
                    productToUpdate.setQty(Validate.qty("new product quantity"));
                    System.out.println(Color.C_GREEN + "Updated successfully!" + C_RESET);
                }
                case "4" -> {
                    System.out.print("Enter new product name: ");
                    productToUpdate.setName(scanner.nextLine());
                    productToUpdate.setPrice(Validate.onlyDouble("new product price"));
                    productToUpdate.setQty(Validate.qty("new product quantity"));
                    System.out.println(Color.C_GREEN + "Updated successfully!" + C_RESET);
                }
                case "5" -> {
                    isRunning = false;
                    unsaveUpdateProduct(productToUpdate);
                }
                default -> System.out.println(C_RED + "Invalid Input! Please Choose Between 1 - 5" + C_RESET);
            }
        }while (isRunning);
        System.out.print("Press to continue....");
        scanner.nextLine();

        firstPage();
    }

    @Override
    public void showUnsaveUpdate() {

        if (unsaveUpdate.isEmpty()){
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

        for (Product product : unsaveUpdate.values()) {
            table.addCell(String.valueOf(product.getId()),text);
            table.addCell(product.getName(),text);
            table.addCell(String.valueOf(product.getPrice()),text);
            table.addCell(String.valueOf(product.getQty()),text);
            table.addCell(String.valueOf(product.getDate()),text);
        }

        System.out.println(table.render());
    }

    @Override
    public void unsaveUpdateProduct(Product product) {
        unsaveUpdate.put(product.getId(), product);
        System.out.println("unsave update product");
    }

    @Override
    public void saveUpdateProduct() {
        if (unsaveUpdate.isEmpty()) {
            System.out.println("No products to save.");
            return;
        }

        String sql = "UPDATE tb_product SET name = ?, unit_price = ?, stock_qty = ?, import_date = ? WHERE id = ?";

        try (Connection connection = dbCon.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (Product product : unsaveUpdate.values()) {
                ps.setString(1, product.getName());
                ps.setDouble(2, product.getPrice());
                ps.setInt(3, product.getQty());

                if (product.getDate() != null) {
                    ps.setDate(4, new java.sql.Date(product.getDate().getTime()));
                } else {
                    ps.setNull(4, java.sql.Types.DATE);
                }
                ps.setInt(5, product.getId());

                ps.executeUpdate();
            }

            unsaveUpdate.clear();
            System.out.println("Save successful!");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
