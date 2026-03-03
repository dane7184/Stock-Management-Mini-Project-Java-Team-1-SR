package main.service;
import main.model.Product;
interface ProductServer {

    void getTotal();

    void showAllProducts();

    void firstPage();
    void unsaveInsertProduct(Product product);

    void laterPage();
    void showUnsavedProducts();

    void goTo(int pageNumber);
    void insertProduct();

    void deleteProduct();
    void saveInsertProduct();

    void searchProductByName();
    void saveAndUpdateProductToDb();
}
