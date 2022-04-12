import Controller.FloorMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        //Spring DI using application context file
        ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        FloorMasteryController controller = appContext.getBean("controller", FloorMasteryController.class);
        controller.run();

    }
}
