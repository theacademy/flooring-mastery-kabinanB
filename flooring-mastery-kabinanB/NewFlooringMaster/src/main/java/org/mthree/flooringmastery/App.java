package org.mthree.flooringmastery;

import org.mthree.flooringmastery.controller.FlooringMasteryController;
import org.mthree.flooringmastery.ui.FlooringMasteryView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringMasteryController controller =
                ctx.getBean("controller", FlooringMasteryController.class);
        controller.run();

    }
}
