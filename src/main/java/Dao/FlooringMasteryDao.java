package Dao;

import Model.Order;
import Model.Product;
import Model.Tax;

import java.util.List;

public interface FlooringMasteryDao {

    List<Order> getAllOrders(String date);
    Order addOrder(Order order);
    Order getOrder(String date, String orderNo);
    Order removeOrder(String date, String orderNo);
    Order updateOrder(Order order);
    void exportAllData();
    Order setOrderSpecifications(Order order);


    Tax getTax(String state);
    Product getProduct(String productName);
}
