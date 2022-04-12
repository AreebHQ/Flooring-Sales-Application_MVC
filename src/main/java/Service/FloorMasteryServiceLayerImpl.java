package Service;

import Dao.FlooringMasteryDao;
import Exceptions.*;
import Model.Order;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class FloorMasteryServiceLayerImpl implements FloorMasteryServiceLayer {

    FlooringMasteryDao dao;


    public FloorMasteryServiceLayerImpl(FlooringMasteryDao dao)
    {
        this.dao = dao;
    }

    public void validateData(Order order) throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException {

        validateDate(order.getDate());
        LocalDate currentDate = LocalDate.now();
        LocalDate orderDate = LocalDate.parse(order.getDate(), DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        if(orderDate.isBefore(currentDate))
        {throw new InvalidDateException("Invalid Date! Order Date Must Be In Future.");}

        String []dateToken = order.getDate().split("-");
        if(dateToken.length < 2) { throw new InvalidDateException("Invalid Date! Order Date Must Be In MM-DD-YYYY Format.");}

        if(order.getCustomerName().isBlank()|| order.getState().trim().length()==0|| order.getProductType().trim().length()==0|| order.getDate().trim().isEmpty())
        {throw new InvalidOrderException("Error! All fields are required!");}

        if(dao.getProduct(order.getProductType())==null)
        {throw new InvalidProductException("Invalid Product! No Such Product Found!");}

        if(dao.getTax(order.getState())==null)
        {throw new InvalidStateException("Invalid State! No Such State Found!");}

    }

    public void validateDate(String date) throws InvalidDateException {
        if(date.isBlank()) {throw new InvalidDateException("Invalid Date! Order Date Must Be In MM-DD-YYYY Format.");}

        String []dateToken = date.split("-");
        if(dateToken.length < 2) { throw new InvalidDateException("Invalid Date! Order Date Must Be In MM-DD-YYYY Format.");}
    }

    @Override
    public List<Order> getAllOrders(String date) throws InvalidDateException {

        validateDate(date);
       return dao.getAllOrders(date);

    }

    @Override
    public Order addOrder(Order order) throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException {

       validateData(order);
       setOrderSpecifications(order);
       Order newOrder = dao.addOrder(order);
       return newOrder;
    }

    @Override
    public Order getOrder(String date, String orderNo) throws InvalidDateException, OrderNotFoundException {
        validateDate(date);
        Order order = dao.getOrder(date,orderNo);
        if(order == null){throw new OrderNotFoundException("Error! No Such Order Exist!");}
        return order;
    }

    @Override
    public Order removeOrder(String date, String orderNo) throws InvalidOrderException {


        if(dao.getOrder(date,orderNo) == null){throw new InvalidOrderException("Error! Order couldn't be removed!");}
        return dao.removeOrder(date,orderNo);
    }

    @Override
    public Order updateOrder(Order order) throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException {
        validateData(order);
        Order updateOrder = dao.updateOrder(order);
        if(order == null){throw new InvalidOrderException("Error! Order couldn't be updated!");}
        return updateOrder;
    }

    @Override
    public void exportAllData() {

        dao.exportAllData();
    }

    @Override
    public Order setOrderSpecifications(Order order) {

      return dao.setOrderSpecifications(order);

    }

}
