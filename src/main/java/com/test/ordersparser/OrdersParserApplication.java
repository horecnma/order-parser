package com.test.ordersparser;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ordersparser.services.OrderFilesHandler;

@SpringBootApplication
public class OrdersParserApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(OrdersParserApplication.class, args);

        List<File> files = Arrays.stream(args).map(File::new).collect(Collectors.toList());
        ctx.getBean(OrderFilesHandler.class).handleFiles(files);
        ctx.close();
    }

    @Bean
    public PrintStream outputStream() {
        return System.out;
    }

    @Bean
    public ExecutorService trackDownloadExecutor() {
        return Executors.newFixedThreadPool(3);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
