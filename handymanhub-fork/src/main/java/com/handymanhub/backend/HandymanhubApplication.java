package com.handymanhub.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication is three annotations bundled into one:
// it tells Spring "scan this package for components", "auto-configure
// beans based on what's on the classpath", and "this is a Spring Boot app".
@SpringBootApplication
public class HandymanhubApplication {

    public static void main(String[] args) {
        // This one line starts the embedded web server, wires up all
        // your @Service/@Repository/@Controller classes, and runs
        // Flyway migrations before the app is ready to receive requests.
        SpringApplication.run(HandymanhubApplication.class, args);
    }
}
