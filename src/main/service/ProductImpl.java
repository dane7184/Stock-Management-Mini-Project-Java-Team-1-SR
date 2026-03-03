package main.service;

import main.lib.DbConnection;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static main.Main.ANSI_GREEN;
import static main.Main.ANSI_RESET;

public class ProductImpl {


    //static Product product = new Product();
    static int startId ;

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

    public static void getTotal() {
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

    public static void showAllProducts() {
        totalRow = 0;

        try (Connection connection = dbCon.dataSource().getConnection()) {
            String sign;
            String sql = "";
            if (next) {
                sign = ">=";
            } else {
                sign = "<=";
            }
            if (sign.equals(">=")) {
                sql = "SELECT * FROM tb_product where id >= " + id + " order by id";
            } else {
                sql = "SELECT * FROM (SELECT * FROM tb_product WHERE id < " + id + " ORDER BY id DESC limit " + row + ") ORDER BY id ASC; ";
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

    public static void nextPage() {

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

    public static void getRow() {
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

    public static void previousPage() {
        if (startPage > 1) {
            startPage--;
            id = tmpLowerId;
        }
        next = false;
        showAllProducts();


    }

    public static void firstPage() {
        try (Connection connection = dbCon.dataSource().getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM tb_product order by id limit 1");
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            System.out.println("First Page : " + id);
            startPage = 1;
            showAllProducts();
        } catch (Exception e) {
        }

    }

    public static void laterPage() {
        try (Connection connection = dbCon.dataSource().getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM tb_product order by id asc ");
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            startPage = totalPage;
            showAllProducts();
        } catch (Exception e) {
        }

    }

    public static void goTo(int pageNumber) {
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

    public static void setRow(int rowNumber) {
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

                id = startId;
                getRow();
                getTotal();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        showAllProducts();

    }


}
