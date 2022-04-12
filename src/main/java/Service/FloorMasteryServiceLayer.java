package Service;

import Exceptions.*;
import Model.Order;

import java.util.List;

public interface FloorMasteryServiceLayer {
    List<Order> getAllOrders(String date) throws InvalidDateException;
    Order addOrder(Order order) throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException;
    Order getOrder(String date, String orderNo) throws InvalidDateException, OrderNotFoundException;
    Order removeOrder(String date, String orderNo) throws InvalidDateException, InvalidOrderException;
    Order updateOrder(Order order) throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException;
    void exportAllData();
    Order setOrderSpecifications(Order order);

}
