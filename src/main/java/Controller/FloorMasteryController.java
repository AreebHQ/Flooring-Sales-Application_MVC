package Controller;

import Dao.FlooringMasteryDao;
import Exceptions.*;
import Model.Order;
import Service.FloorMasteryServiceLayer;
import UI.FlooringMasteryView;


import java.util.List;

public class FloorMasteryController {

    FlooringMasteryView view;
    FloorMasteryServiceLayer serviceLayer;


   public FloorMasteryController(FloorMasteryServiceLayer serviceLayer, FlooringMasteryView view){
       this.serviceLayer = serviceLayer;
       this.view = view;
   }


    public void run()
    {
        boolean keepGoing = true;
        int menuSelection = 0;

            while (keepGoing) {
                try {
                    menuSelection = getMenuSelection();

                    switch (menuSelection) {
                        case 1:
                            displayOrders();
                            break;
                        case 2:
                            addOrder();
                            break;
                        case 3:
                            editOrder();
                            break;
                        case 4:
                            removeOrder();
                            break;
                        case 5:
                            exportAllData();
                            break;
                        case 6:
                            keepGoing = false;
                            view.displayExitBanner();
                            break;
                        default:
                            unknownCommand();
                    }

                } catch (InvalidOrderException e) {
                    view.displayDisplayErrorBanner("Error! Invalid Order! \n Information Missing. Please try again.\n\n");
                } catch (InvalidDateException e) {
                    view.displayDisplayErrorBanner("Error! Invalid Date! Make sure date is Future date with MM-DD-YYYY format.\n\n");
                } catch (InvalidProductException e) {
                    view.displayDisplayErrorBanner("Error! Invalid Product! No Such Product Exist!\n\n");
                } catch (InvalidStateException e) {
                    view.displayDisplayErrorBanner("Error! Invalid State! No Such State Exist!\n\n");
                } catch (OrderNotFoundException e) {
                    view.displayDisplayErrorBanner("Error! Invalid Order! \nOrder not found! Please try again.\n\n");
                }
            }

    }

    private void displayOrders() {
        boolean hasError= false;
        do{
            view.displayAllOrdersBanner();
            try {
                String date = view.getOrderDate();
                List<Order> ordersList = serviceLayer.getAllOrders(date);
                view.displayOrders(ordersList);
                hasError = false;

            } catch (InvalidDateException e) {
                hasError = true;
                view.displayDisplayErrorBanner("Error! Invalid Date! Make sure date is Future date with MM-DD-YYYY format");
            }
        } while (hasError);
    }

    private void addOrder()  {

        boolean hasError= false;
        do{
            view.displayNewOrderBanner();
            Order newOrder = view.getNewOrderInfo();
               try {
                    serviceLayer.addOrder(newOrder);
                    view.displayAddSuccessBanner();
                    hasError = false;
               }
              catch (InvalidOrderException e) {
                  hasError = true;
                  view.displayDisplayErrorBanner("Error! Invalid Order! Information Missing. Please try again.");

               } catch (InvalidDateException e) {
                   hasError = true;
                   view.displayDisplayErrorBanner("Error! Invalid Date! Make sure date is Future date with MM-DD-YYYY format.");

               } catch (InvalidProductException e) {
                   hasError = true;
                   view.displayDisplayErrorBanner("Error! Invalid Product! No Such Product Exist!");

               } catch (InvalidStateException e) {
                   hasError = true;
                   view.displayDisplayErrorBanner("Error! Invalid State! No Such State Exist!");
               }

        } while (hasError);

    }

    private void editOrder() throws InvalidOrderException, InvalidDateException, InvalidProductException, InvalidStateException, OrderNotFoundException {

        view.displayEditOrderBanner();
        String date = view.getOrderDate();
        String orderNumber = view.getOrderNo();
        Order order = serviceLayer.getOrder(date, orderNumber);
        if (order != null) {
            view.displayOrder(order);
            view.getEditOrderInfo(order);
            order = serviceLayer.setOrderSpecifications(order);
            view.displayOrder(order);
            if (view.getConfirmationChoice() == 'Y') {
                serviceLayer.updateOrder(order);
                view.displayEditSuccessBanner();}
        } else {
            view.displayOrder(order);
        }

    }

    private void removeOrder() throws InvalidOrderException, InvalidDateException, OrderNotFoundException {
            view.displayRemoveOrderBanner();
            String date = view.getOrderDate();
            String orderNumber = view.getOrderNo();
            Order order = serviceLayer.getOrder(date, orderNumber);
            view.displayOrder(order);
            if (view.getConfirmationChoice() == 'Y') {
            serviceLayer.removeOrder(date,orderNumber);
            view.displayRemoveResult(order);
        }
    }

    private void exportAllData() { serviceLayer.exportAllData();}
    private int getMenuSelection() { return view.printMenuAndGetSelection();}
    private void unknownCommand() {view.displayUnknownCommandBanner();}
}
