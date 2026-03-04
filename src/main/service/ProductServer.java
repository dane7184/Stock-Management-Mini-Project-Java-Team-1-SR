package main.service;
import main.model.Product;

interface ProductServer {

    void getTotal();

    void firstPage();

    void laterPage();

    void goTo(int pageNumber);

    void showAllProducts();

    void insertProduct();

    void deleteProduct();

    void saveInsertProduct();

    void searchProductByName();

    void saveAndUpdateProductToDb();

    void unsaveInsertProduct(Product product);

    void showUnsavedProducts();

    void getProductById();

    void clearUnsaveInsertProducts();
}
