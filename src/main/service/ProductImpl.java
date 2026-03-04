package main.service;

import main.lib.DbConnection;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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



}
