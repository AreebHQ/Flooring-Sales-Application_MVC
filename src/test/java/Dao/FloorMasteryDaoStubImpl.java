package Dao;

import Exceptions.FlooringMasteryPersistenceException;
import Model.Order;
import Model.Product;
import Model.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FloorMasteryDaoStubImpl implements FlooringMasteryDao {
    public Order onlyOrder;

    //Files headers
    public static final String ORDERS_FILE_HEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
    public static final String BACKUP_FILE_HEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate";
    public static String PRODUCT_FILE_HEADER;
    public static String TAX_FILE_HEADER;
    //Delimiter
    public static final String DELIMITER = ",";

    //folder/files
    public final String PRODUCT_FILE ="Data\\Products.txt";
    public final String TAXES_FILE ="Data\\Taxes.txt";
    public final String DATA_EXPORT_FILE ="src\\test\\Backup\\DataExport.txt";
    public static String DAILY_ORDERS_MASTER_FILE;

    //orders map - taxes and products list
    Map<String, List<Order>> orders = new HashMap<>();
    List<Tax> taxes = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    static List<String> dailyOrdersMasterList = new ArrayList<>();

    //order number counter
    static long orderCounter = 0;

    public FloorMasteryDaoStubImpl(String orderFile) {

        this.DAILY_ORDERS_MASTER_FILE = orderFile;
        loadProducts();
        loadTax();
        loadDailyOrdersMasterFile();
        loadDataFromOrderFiles();
    }

    public FloorMasteryDaoStubImpl(){
        this.DAILY_ORDERS_MASTER_FILE = "src\\test\\testOrder.txt";
        loadProducts();
        loadTax();
        loadDailyOrdersMasterFile();
        loadDataFromOrderFiles();

    }


    @Override
    public List<Order> getAllOrders(String date) {

        return orders.get(date);
    }
    /*
    public void removeAllOrders()
    {

        for(Map.Entry<String,List<Order>> entry : orders.entrySet())
        {
            for(int i=0; i<entry.getValue().size(); i++)
            {
                Order order = entry.getValue().get(i);
                entry.getValue().remove(i);
                updateRemoveOrderData(orders.get(order.getDate()), order.getDate());
                writeBackupData();

            }
        }
    } */

    @Override
    public Order addOrder(Order order) {
        //setting key-orderDate
        String date = order.getDate();

        //setting orderNo counter
        order.setOrderNumber((int) ++orderCounter);

        //adding new Order into map - if date already exist then add into the list
        if(orders.containsKey(date))
        {
            orders.get(date).add(order);
        } else {
            // if date doesn't exist then create a new list and add list into map
            ArrayList<Order> newOrder = new ArrayList<>();
            newOrder.add(order);
            orders.put(date,newOrder);
        }
        //saving orders data to file
        writeOrderData(orders.get(date));

        return order;
    }

    //setting calculation data
    @Override
    public Order setOrderSpecifications(Order order) {
        for (Product product : products)
        {
            if(product.getProductType().equals(order.getProductType()))
            {
                BigDecimal materialCost = (order.getArea().multiply(product.getCostPerSqFt())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal laborCost = (order.getArea().multiply(product.getLaborCostPerSqFt())).setScale(2,RoundingMode.HALF_UP);;
                order.setLaborCostPerSqFt(product.getLaborCostPerSqFt());
                order.setCostPerSqFt(product.getCostPerSqFt());
                order.setMaterialCost(materialCost);
                order.setLaborCost(laborCost);
                break;
            }
        }

        for(Tax tax : taxes)
        {
            if(tax.getStateAbbreviation().equals(order.getState()))
            {
                BigDecimal taxAmount = (((order.getMaterialCost().add(order.getLaborCost()).multiply(tax.getTaxRate()))).divide(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP);
                order.setTaxRate(tax.getTaxRate());
                order.setTax(taxAmount);
                break;
            }
        }
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()).setScale(2,RoundingMode.HALF_UP));
        return order;
    }

    @Override
    public Order updateOrder(Order order){

        List<Order> orderList = orders.get(order.getDate());
        for(Order currentOrder : orderList)
        {
            if(currentOrder.getOrderNumber() == order.getOrderNumber())
            {
                currentOrder.setCustomerName(order.getCustomerName());
                currentOrder.setState(order.getState());
                currentOrder.setProductType(order.getProductType());
                currentOrder.setArea(order.getArea());
                break;
            }
        }
        writeBackupData();
        return order;
    }

    @Override
    public Order getOrder(String date, String orderNo) {

        if(orders.get(date)!=null)
        {
            for(int i=0; i<orders.get(date).size(); i++)
            {
                if(orders.get(date).get(i).getOrderNumber() == Integer.parseInt(orderNo))
                {
                    return orders.get(date).get(i);
                }
            }
        }
        return null;
    }

    @Override
    public Order removeOrder(String date, String orderNo) {

        if(orders.get(date)!=null)
        {
            for(int i=0; i<orders.get(date).size(); i++)
            {
                if(orders.get(date).get(i).getOrderNumber() == Integer.parseInt(orderNo))
                {
                    Order removedOrder = orders.get(date).get(i);
                    orders.get(date).remove(i);
                    updateRemoveOrderData(orders.get(date), date);
                    writeBackupData();
                    return removedOrder;
                }
            }
        }
        return null;
    }

    @Override
    public void exportAllData() {

        writeBackupData();
    }

    @Override
    public Tax getTax(String state) {

        for(Tax tax : taxes)
        {
            if(tax.getStateAbbreviation().equals(state))
            {
                return tax;
            }
        }
        return null;
    }

    @Override
    public Product getProduct(String productName) {

        for(Product product : products)
        {
            if(product.getProductType().equals(productName))
            {
                return product;
            }
        }
        return null;
    }

    private void writeBackupData() {
        PrintWriter out = null;
        try{
            out = new PrintWriter(new FileWriter(DATA_EXPORT_FILE));
        } catch (IOException e)
        {
            new FlooringMasteryPersistenceException("Couldn't export backup data to file.", e);
        }
        //Writing header to file
        out.println(BACKUP_FILE_HEADER);
        out.flush();

        String orderAsText;
        for(Map.Entry<String,List<Order>> entry : orders.entrySet())
        {
            List<Order> ordersList = new ArrayList<>(entry.getValue());
            if(ordersList.isEmpty())
            {continue;}
            //writing orders files by days
            writeOrderData(ordersList);

            //writing backup file
            for(Order order : ordersList)
            {
                orderAsText = marshallBackUpOrders(order);
                out.println(orderAsText);
                out.flush();
            }
        }
        out.close();
    }


    private void writeOrderData(List<Order> orderList) {

        //getting mm-dd-yyy into mmddyyy format
        String fileDate = orderList.get(0).getDate().replace("-","");

        String orderFile = "src\\test\\Orders\\Order_"+fileDate+".txt";

        PrintWriter out = null;
        try{
            out = new PrintWriter(new FileWriter(orderFile));
        } catch (IOException e)
        {
            new FlooringMasteryPersistenceException("Couldn't save Orders in file");
        }

        //Writing header to file
        out.println(ORDERS_FILE_HEADER);
        out.flush();

        String orderAsText;
        for(Order order : orderList)
        {
            orderAsText = marshallOrder(order);
            out.println(orderAsText);
            out.flush();
        }
        out.close();
        updateDailyOrdersMasterFile(orderFile);
    }
    private void updateDailyOrdersMasterFile(String orderFileName)
    {
        if(!dailyOrdersMasterList.contains(orderFileName))
        {
            dailyOrdersMasterList.add(orderFileName);
        }
        PrintWriter out = null;
        try { out = new PrintWriter(new FileWriter(DAILY_ORDERS_MASTER_FILE)); }
        catch (IOException e) {
            new FlooringMasteryPersistenceException("Error reading/writing daily orders master file");
        }
        for(String fileName : dailyOrdersMasterList)
        {
            out.println(fileName);
            out.flush();
        }
        out.close();
    }

    private void updateRemoveOrderData(List<Order> orderList, String date) {

        //getting mm-dd-yyy into mmddyyy format
        String fileDate = date.replace("-","");

        String orderFile = "src\\test\\Orders\\Order_"+fileDate+".txt";

        PrintWriter out = null;
        try{
            out = new PrintWriter(new FileWriter(orderFile));
        } catch (IOException e)
        {
            new FlooringMasteryPersistenceException("Couldn't save Orders in file");
        }

        //Writing header to file
        out.println(ORDERS_FILE_HEADER);
        out.flush();

        String orderAsText;
        for(Order order : orderList)
        {
            orderAsText = marshallOrder(order);
            out.println(orderAsText);
            out.flush();
        }
        out.close();
    }


    private void loadDataFromOrderFiles()
    {

        for(int i=0; i<dailyOrdersMasterList.size(); i++)
        {
            Scanner scanner;

            try {
                scanner = new Scanner(new BufferedReader(new FileReader(dailyOrdersMasterList.get(i))));
                String currentLine;
                Order currentOrder;
                //setting order dates
                String []orderDateToken = dailyOrdersMasterList.get(i).split("_");
                String orderDate = orderDateToken[1].replace(".txt","");
                LocalDate orderLocalDate = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("MMddyyyy"));
                orderDate = orderLocalDate.format(DateTimeFormatter.ofPattern("MM-dd-YYYY"));
                //skipping header line
                scanner.nextLine();
                //remaining lines
                while (scanner.hasNextLine()){
                    currentLine = scanner.nextLine();
                    if(!currentLine.isEmpty())
                    {
                        currentOrder = unmarshallOrder(currentLine);
                        setOrderNumber(currentOrder);
                        currentOrder.setDate(orderDate);

                        if(orders.containsKey(orderDate))
                        {
                            orders.get(orderDate).add(currentOrder);
                        } else {
                            List<Order> orderList = new ArrayList<>();
                            orderList.add(currentOrder);
                            orders.put(orderDate,orderList);
                        }
                    }
                }
                scanner.close();

            } catch (FileNotFoundException e)
            {
                new FlooringMasteryPersistenceException( "Could not dailyOrder file");
            }
        }

    }

    private void loadDailyOrdersMasterFile()
    {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(DAILY_ORDERS_MASTER_FILE)));
            String fileNames;
            while (scanner.hasNextLine())
            {
                fileNames = scanner.nextLine();
                if(!fileNames.isEmpty()) {
                    dailyOrdersMasterList.add(fileNames);
                }
            }
        } catch (FileNotFoundException e) {
            new FlooringMasteryPersistenceException("could not load DAILY ORDERS MASTER FILE.");
        }

        scanner.close();
    }

    // loading products
    private void loadProducts()
    {

        Scanner scanner  = null;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e)
        {
            new FlooringMasteryPersistenceException("could not load products data.");
        }
        String currentLine;
        Product currentProduct;
        //first line of file - headers
        PRODUCT_FILE_HEADER = scanner.nextLine();

        while (scanner.hasNextLine())
        {
            currentLine = scanner.nextLine();
            if(!currentLine.isEmpty())
            {
                currentProduct = unmarshallProduct(currentLine);
                products.add(currentProduct);
            }
        }
        scanner.close();
    }

    private void loadTax()
    {
        Scanner scanner = null;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e)
        {
            new FlooringMasteryPersistenceException("Could not load Tax data.", e);
        }
        String currentLine;
        Tax currentTax;
        //first line of file - headers
        TAX_FILE_HEADER = scanner.nextLine();

        while(scanner.hasNextLine())
        {
            currentLine = scanner.nextLine();
            if(!currentLine.isEmpty())
            {
                currentTax = unmarshallTax(currentLine);
                taxes.add(currentTax);
            }
        }
        scanner.close();
    }

    private static void setOrderNumber(Order currentOrder){orderCounter = Math.max(orderCounter,currentOrder.getOrderNumber());}

    //OrdersByDay
    private String marshallOrder(Order order)
    {
        String orderAsText = order.getOrderNumber()+DELIMITER;
        orderAsText+=order.getCustomerName()+DELIMITER;
        orderAsText+=order.getState()+DELIMITER;
        orderAsText+=order.getTaxRate()+DELIMITER;
        orderAsText+=order.getProductType()+DELIMITER;
        orderAsText+=order.getArea()+DELIMITER;
        orderAsText+=order.getCostPerSqFt()+DELIMITER;
        orderAsText+=order.getLaborCostPerSqFt()+DELIMITER;
        orderAsText+=order.getMaterialCost()+DELIMITER;
        orderAsText+=order.getLaborCost()+DELIMITER;
        orderAsText+=order.getTax()+DELIMITER;
        orderAsText+=order.getTotal();

        return orderAsText;
    }
    //OrdersByDay
    private static Order unmarshallOrder(String orderAsText)
    {
        String[] orderToken = orderAsText.split(DELIMITER);
        Order orderFromFile = new Order();
        orderFromFile.setOrderNumber(Integer.parseInt(orderToken[0]));
        orderFromFile.setCustomerName(orderToken[1]);
        orderFromFile.setState(orderToken[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderToken[3]));
        orderFromFile.setProductType(orderToken[4]);
        orderFromFile.setArea(new BigDecimal(orderToken[5]));
        orderFromFile.setCostPerSqFt(new BigDecimal(orderToken[6]));
        orderFromFile.setLaborCostPerSqFt(new BigDecimal(orderToken[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderToken[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderToken[9]));
        orderFromFile.setTax(new BigDecimal(orderToken[10]));
        orderFromFile.setTotal(new BigDecimal(orderToken[11]));

        return orderFromFile;
    }


    //BackUp - Export order file
    private String marshallBackUpOrders(Order order)
    {
        String orderAsText = order.getOrderNumber()+DELIMITER;
        orderAsText+=order.getCustomerName()+DELIMITER;
        orderAsText+=order.getState()+DELIMITER;
        orderAsText+=order.getTaxRate()+DELIMITER;
        orderAsText+=order.getProductType()+DELIMITER;
        orderAsText+=order.getArea()+DELIMITER;
        orderAsText+=order.getCostPerSqFt()+DELIMITER;
        orderAsText+=order.getLaborCostPerSqFt()+DELIMITER;
        orderAsText+=order.getMaterialCost()+DELIMITER;
        orderAsText+=order.getLaborCost()+DELIMITER;
        orderAsText+=order.getTax()+DELIMITER;
        orderAsText+=order.getTotal()+DELIMITER;
        orderAsText+=order.getDate();

        return orderAsText;
    }

    //Product file
    private Product unmarshallProduct(String productAsText)
    {
        String []productToken = productAsText.split(DELIMITER);
        Product productFromFile = new Product();
        productFromFile.setProductType(productToken[0]);
        productFromFile.setCostPerSqFt(new BigDecimal(productToken[1]));
        productFromFile.setLaborCostPerSqFt(new BigDecimal(productToken[2]));

        return productFromFile;
    }

    //State Tax file
    private Tax unmarshallTax(String taxAsText)
    {
        String []taxToken = taxAsText.split(DELIMITER);
        Tax taxFromFile = new Tax();
        taxFromFile.setStateAbbreviation(taxToken[0]);
        taxFromFile.setStateName(taxToken[1]);
        taxFromFile.setTaxRate(new BigDecimal(taxToken[2]));

        return taxFromFile;
    }

}
