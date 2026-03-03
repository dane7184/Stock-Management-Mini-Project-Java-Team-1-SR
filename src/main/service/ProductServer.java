package main.service;

interface ProductServer {

    void getTotal();

    void showAllProducts();

    void firstPage();

    void laterPage();

    void goTo(int pageNumber);

    void deleteProduct();

    void searchProductByName();
}
