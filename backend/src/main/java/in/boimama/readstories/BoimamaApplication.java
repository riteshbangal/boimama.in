package in.boimama.readstories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoimamaApplication {

    private static final Logger logger = LoggerFactory.getLogger(BoimamaApplication.class);

    public static void main(String[] args) {
        logger.debug("Hello Boimama.in!");
        SpringApplication.run(BoimamaApplication.class, args);
    }
}