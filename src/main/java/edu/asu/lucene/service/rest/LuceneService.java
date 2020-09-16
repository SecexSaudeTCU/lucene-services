package edu.asu.lucene.service.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import riskdata.microservices.IndiceServiceController;
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})

@SpringBootApplication
@ComponentScan(basePackageClasses = {IndiceServiceController.class, LuceneService.class}, basePackages = "riskdata.*")
public class LuceneService {

    public static void main(String[] args) {
        SpringApplication.run(LuceneService.class, args);
    }
}
