package se.frihak.servingwebcontent;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServingWebContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }
    
    @Bean
    public ServletContextInitializer servletContextInitializer () {
        ServletContextInitializer servletContextInitializer =
                new ServletContextInitializer () {
                    @Override
                    public void onStartup (ServletContext servletContext) throws ServletException {
                        servletContext.setSessionTrackingModes (Collections.singleton (SessionTrackingMode.COOKIE));
                    }
                };
        return servletContextInitializer;
    }
}
