package main.service;

import main.model.Product;

public interface ProductServer {

    void showAllProducts();

    void unsaveInsertProduct(Product product);

    void showUnsavedProducts();

    void insertProduct();

    void saveInsertProduct();

    void saveAndUpdateProductToDb();
}
