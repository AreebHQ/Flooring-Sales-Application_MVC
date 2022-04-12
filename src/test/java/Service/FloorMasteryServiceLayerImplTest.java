package Service;

import Dao.FloorMasteryDaoStubImpl;
import Dao.FlooringMasteryDao;
import Exceptions.*;
import Model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FloorMasteryServiceLayerImplTest {


    private final FloorMasteryServiceLayer testService;
    String testFile  = "src\\test\\testOrder.txt";

    public FloorMasteryServiceLayerImplTest() throws IOException {
        //FlooringMasteryDao dao = new FloorMasteryDaoStubImpl();
        //testService = new FloorMasteryServiceLayerImpl(dao);

        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContextTest.xml");
        testService = ctx.getBean("serviceLayer", FloorMasteryServiceLayer.class);
    }


    @BeforeEach
    void setUp() throws IOException {

    }

    @Test
    void testValidOrder()
    {
        Order order = new Order();
        order.setDate("06-06-2026");
        order.setCustomerName("TestOrder");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));

        try {
            testService.addOrder(order);
        } catch (InvalidOrderException | InvalidDateException | InvalidProductException | InvalidStateException e) {
            fail("this was a valid test",e);
        }
    }


    @Test
    void testInvalidOrder()
    {
        Order order = new Order();
        order.setDate("06-06-2024");
        order.setCustomerName("");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));

        try {
            testService.addOrder(order);
            fail("This was a invalid order. Do better!!");
        } catch (InvalidOrderException e) {
           return;
        } catch (InvalidProductException | InvalidStateException | InvalidDateException e) {
            fail("This was a wrong Exception. Do better!!");
        }
    }

    @Test
    void validateData() {
    }

    @Test
    void validateDate() {
    }

    @Test
    void getAllOrders() throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException {
        Order order = new Order();
        order.setDate("06-06-2088");
        order.setCustomerName("New Customer");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));
        testService.addOrder(order);

        assertEquals(1,testService.getAllOrders("06-06-2088").size(),"only one order - is 1 length");
        assertTrue(testService.getAllOrders("06-06-2088").contains(order));
        testService.removeOrder(order.getDate(),String.valueOf(order.getOrderNumber()));
    }

    @Test
    void addOrder() {
    }

    @Test
    void getOrder() throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException, OrderNotFoundException {
        Order order = new Order();
        order.setDate("06-06-2028");
        order.setCustomerName("New Customer");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));
        testService.addOrder(order);

        Order getorder = testService.getOrder("06-06-2028",String.valueOf(order.getOrderNumber()));

        assertNotNull(getorder);
        assertEquals(order,getorder);

        Order notInDao = null;
        try {
            notInDao = testService.getOrder("06-06-2038","55");
        } catch (OrderNotFoundException e) {
            return;
        }

        assertNull(notInDao);
        testService.removeOrder(order.getDate(),String.valueOf(order.getOrderNumber()));
    }

    @Test
    void removeOrder() throws InvalidDateException, InvalidProductException, InvalidStateException, InvalidOrderException {

        Order order = new Order();
        order.setDate("06-06-2028");
        order.setCustomerName("New order");
        order.setProductType("Wood");
        order.setState("CA");
        order.setArea(new BigDecimal(250));
        testService.addOrder(order);

        Order removedOrder = testService.removeOrder("06-06-2028",String.valueOf(order.getOrderNumber()));
        assertNotNull(removedOrder);
        assertEquals(order,removedOrder);

        Order notInDao = null;
        try {
            notInDao = testService.removeOrder("06-06-3028","60");
        } catch (InvalidOrderException e) {
            return;
        }
        assertNull(notInDao);


    }

    @Test
    void updateOrder() {
    }

    @Test
    void exportAllData() {
    }

    @Test
    void setOrderSpecifications() {
    }
}