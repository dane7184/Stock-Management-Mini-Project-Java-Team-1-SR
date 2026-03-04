package main.model;

import main.service.Validate;
import main.service.Validate.*;

import java.util.Date;
import java.util.Scanner;

public class Product {
    private static String id;
    private static String name;
    private static double price;
    private static int qty;
    private static Date date;

    public Product() {}

    public static void writeProduct(){
        Scanner scanner= new Scanner(System.in);

        System.out.println("=".repeat(45)+" Write Product "+"=".repeat(45));

        String productName= Validate.valName("Product Name");

        double productPrice = Validate.onlyDouble("Product Price");

        int qty = Validate.qty("quantity product");

         String importDate=Validate.formattedDate();


        System.out.println("\n Product Created Successfully!");
        System.out.println();
    }

    public Product(String id, Date date, int qty, double price, String name) {
        this.id = id;
        this.date = date;
        this.qty = qty;
        this.price = price;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", date=" + date +
                '}';
    }
}
