package Dao;

import Model.Order;
import Model.Product;
import Model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryDaoImplTest {

    FlooringMasteryDao testDao;

    @BeforeEach
    void setUp() throws IOException {

        String testOrderFile = "src\\test\\testOrder.txt";
        String testBackUpFile = "testBackupFile.txt";
        new FileWriter(testOrderFile);
        testDao = new FloorMasteryDaoStubImpl(testOrderFile);
    }

    @Test
    void addGetAllOrders() {

        Order order = new Order();
        order.setDate("06-06-2066");
        order.setCustomerName("Test Name");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));

        Order order2 = new Order();
        order2.setDate("06-06-2066");
        order2.setCustomerName("Test NameTwo");
        order2.setProductType("Tile");
        order2.setState("CA");
        order2.setArea(new BigDecimal(300));
        //Act
        testDao.setOrderSpecifications(order2);
        testDao.addOrder(order2);
        testDao.setOrderSpecifications(order);
        testDao.addOrder(order);

        List<Order> allOrders = testDao.getAllOrders("06-06-2066");
        assertNotNull(allOrders,"List Not Null");
        assertEquals(2,allOrders.size(),"we added two orders");
        assertTrue(allOrders.contains(order),"List has Test Name");
        assertTrue(allOrders.contains(order2),"List has Test NameTwo");
    }

    @Test
    void addGetOrder() {
        Order order = new Order();
        order.setDate("06-06-2067");
        order.setCustomerName("Test Name");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));
        //Act
        testDao.setOrderSpecifications(order);
        testDao.addOrder(order);
        Order retrieveOrder = testDao.getOrder("06-06-2067",String.valueOf(order.getOrderNumber()));
        //Assert
        assertEquals(order.getCustomerName(),retrieveOrder.getCustomerName(),"check name");
        assertEquals(order.getProductType(),retrieveOrder.getProductType(),"check product");
        assertEquals(order.getState(),retrieveOrder.getState(),"check state");
        assertEquals(order.getArea(),retrieveOrder.getArea(),"check area");
        assertNotNull(order.getTotal(),"total not null");
    }

    @Test
    void addRemoveOrder() {
        Order order = new Order();
        order.setDate("06-06-2068");
        order.setCustomerName("Test Name");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));

        Order order2 = new Order();
        order2.setDate("06-06-2068");
        order2.setCustomerName("Test NameTwo");
        order2.setProductType("Tile");
        order2.setState("CA");
        order2.setArea(new BigDecimal(300));
        //Act
        testDao.setOrderSpecifications(order2);
        testDao.addOrder(order2);
        testDao.setOrderSpecifications(order);
        testDao.addOrder(order);

        Order removedOrder = testDao.removeOrder("06-06-2068",String.valueOf(order.getOrderNumber()));

        assertEquals(removedOrder, order, "Removed 06-06-2068 - Test Name");
        List<Order> allOrders = testDao.getAllOrders("06-06-2068");

        assertNotNull(allOrders,"still one order in list");
        assertEquals(1,allOrders.size(),"order list size = 1");
        assertFalse(allOrders.contains(order),"Test Name order is gone");
        assertTrue(allOrders.contains(order2), "Test NameTwo is here");

        removedOrder = testDao.removeOrder("06-06-2068", String.valueOf(order2.getOrderNumber()));

        assertEquals(removedOrder,order2,"Removed Test NameTwo");
        allOrders = testDao.getAllOrders("06-06-2068");
        assertTrue(allOrders.isEmpty());
        Order getOrder = testDao.getOrder("06-06-2068",String.valueOf(order.getOrderNumber()));
        assertNull(getOrder);
        Order getOrder2 = testDao.getOrder("06-06-2068",String.valueOf(order2.getOrderNumber()));
        assertNull(getOrder2);

    }


}