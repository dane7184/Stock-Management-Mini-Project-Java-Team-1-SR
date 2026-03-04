package main.model;

import java.util.Date;

public class Product {
    private int id;
    private String name;
    private double price;
    private int qty;
    private Date date;

    public Product() {}

    public Product(int id, String name,  double price, int qty, Date date) {
        this.id = id;
        this.date = date;
        this.qty = qty;
        this.price = price;
        this.name = name;
    }

    public Product(String name, double price, int qty, Date date) {
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
