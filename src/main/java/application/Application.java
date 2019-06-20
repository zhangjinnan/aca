package application;

import controller.AcaController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by zhenzl1 on 2019/6/18.
 */
@SpringBootApplication
public class Application {
        public static void main(String[] args) {
            SpringApplication.run(AcaController.class, args);
        }
}
