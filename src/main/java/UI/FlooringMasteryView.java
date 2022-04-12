package UI;

import Model.Order;
import java.math.BigDecimal;
import java.util.List;


public class FlooringMasteryView {

    UserIO io;


    public FlooringMasteryView(UserIO io){
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("<< Flooring Program >>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return io.readInt("Please select from the above choices:",1,6);
    }

    public Order getNewOrderInfo() {

        String date = io.readString("Enter [future] order date in MM-DD-YYYY format:");
        String customerName = io.readString("Enter Customer Name:");
        String state = io.readString("Enter state abbreviation:");
        String product = io.readString("Enter product name:");
        BigDecimal area = io.readBigDecimalArea("Enter area in sqft. Minimum 100-sqft.");

        Order newOrder = new Order();
        newOrder.setDate(date);
        newOrder.setCustomerName(customerName);
        newOrder.setState(state);
        newOrder.setProductType(product);
        newOrder.setArea(area);

        return newOrder;
    }


    public Order getEditOrderInfo(Order order) {

        String customerName = io.readString("Enter Customer Name:");
        String state = io.readString("Enter state abbreviation:");
        String product = io.readString("Enter product name:");
        BigDecimal area = io.readBigDecimal("Enter area in sqft. Minimum 100-sqft.");
       if(area.doubleValue() > 0 && area.doubleValue() < 99){
           while(area.doubleValue() < 99){
            area = io.readBigDecimal("Error - Minimum area 100-sqft required. Enter Area again:");}
       }
        if(customerName.isEmpty()){customerName = order.getCustomerName();}
        if(state.isEmpty()){state = order.getState();}
        if(product.isEmpty()){product = order.getProductType();}
        if(area.doubleValue()==0){area = order.getArea();}

        order.setCustomerName(customerName);
        order.setState(state);
        order.setProductType(product);
        order.setArea(area);

        return order;
    }


    public void displayEditResult(Order order) {
        if(order != null){
            io.print("Dvd successfully Updated.");
        }else{
            io.print("No such Dvd found. Try with full dvd name");
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayRemoveResult(Order order) {
        if(order != null){
            io.print("Order No." + order.getOrderNumber() +" successfully removed.");
        }else{
            io.print("No such Order found. Try again with correct DATE and ORDER No.");
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayOrders(List<Order> ordersList)
    {
        if(ordersList != null){
            //using Lambda function
            ordersList.stream().forEach((order -> {
                String orderInfo = String.format("#%s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s.",
                        order.getOrderNumber(),
                        order.getCustomerName(),
                        order.getState(),
                        order.getTaxRate(),
                        order.getProductType(),
                        order.getArea(),
                        order.getCostPerSqFt(),
                        order.getLaborCostPerSqFt(),
                        order.getMaterialCost(),
                        order.getLaborCost(),
                        order.getTax(),
                        order.getTotal());
                io.print(orderInfo);
            }));
        }
        else
        {
            io.print("No Order found.");
        }
        io.readString("Please hit enter to continue.");
    }


    public void displayOrder(Order order)
    {
        if(order != null){

                String orderInfo = String.format("#%s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s, \t %s.",
                        order.getOrderNumber(),
                        order.getCustomerName(),
                        order.getState(),
                        order.getTaxRate(),
                        order.getProductType(),
                        order.getArea(),
                        order.getCostPerSqFt(),
                        order.getLaborCostPerSqFt(),
                        order.getMaterialCost(),
                        order.getLaborCost(),
                        order.getTax(),
                        order.getTotal());
                io.print(orderInfo);
            }
        else
        {
            io.print("No Order found.");
        }
        io.readString("Please hit enter to continue.");
    }



    //Get User Choices
    public String getOrderDate() { return io.readString("Please enter Order date in MM-DD-YYYY format:");}
    public String getOrderNo() { return io.readString("Please enter Order number:");}
    public int getRedoChoice() { return io.readInt("Please enter 1 to continue, or any other value to go back to Main Menu");}
    public char getConfirmationChoice() { return io.readChar("Please enter 'Y' to Save Changes or 'N' to Cancel");}
    // Display Banners
    public void displayNewOrderBanner(){io.print("===========||New Order||===========");}
    public void displayAddSuccessBanner(){io.readString("===========||Success! New Order Added||===========");}
    public void displayAllOrdersBanner() { io.print("===|| Display All Orders ||===");}
    public void displayOrderBanner () { io.print("===|| Display Order ||==="); }
    public void displayEditOrderBanner () { io.print("===|| Edit Order ||==="); }
    public void displayEditSuccessBanner(){io.readString("===========||Success! Order Updated||===========");}
    public void displayRemoveOrderBanner () { io.print("===|| Remove Order ||==="); }
    public void displayExitBanner() { io.print("Good Bye!!!");}
    public void displayUnknownCommandBanner() { io.print("Unknown Command!!!"); }
    public void displayDisplayErrorBanner (String msg) { io.print("=== Error ==="); io.print(msg); }

}
