package de.blueteawolf.riotstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author BlueTeaWolf
 */

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
        System.out.println("Hello, executed main application with Springboot");

    }


}
