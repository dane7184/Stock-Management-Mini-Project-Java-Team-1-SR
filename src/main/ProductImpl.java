package main;

import org.nocrala.tools.texttablefmt.BorderStyle;
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

            Table table = new Table(5, BorderStyle.UNICODE_HEAVY_BOX_WIDE, ShownBorders.ALL);

            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Price");
            table.addCell("Quantity");
            table.addCell("Import Date");

            while(resultSet.next()) {
                table.addCell(String.valueOf(resultSet.getInt("id")));
                table.addCell(resultSet.getString("name"));
                table.addCell(String.valueOf(resultSet.getString("unit_price")));
                table.addCell(String.valueOf(resultSet.getString("stock_qty")));
                table.addCell(String.valueOf(resultSet.getString("import_date")));
            }

            System.out.println(table.render());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
