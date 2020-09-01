package com.test.ordersparser;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;

import com.test.ordersparser.services.OrderFilesHandler;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrdersParserApplicationTests {
    @Autowired
    private OrderFilesHandler orderFilesHandler;
    @Value("orders.json")
    private Resource json;
    @Value("orders.csv")
    private Resource csv;
    @Value("expectedResult.json")
    private Resource expectedResult;

    @MockBean
    private PrintStream outputStream;

    @Test
    void test()
            throws IOException {
        StringBuilder builder = setupOutput();

        orderFilesHandler.handleFiles(Arrays.asList(json.getFile(), csv.getFile()));

        String[] lines = builder.toString().split("\n");
        String resultString = Arrays.stream(lines).sorted().collect(Collectors.joining("\n"));
        String expectedResultString  = Files.readAllLines(expectedResult.getFile().toPath()).stream().collect(Collectors.joining("\n"));
        assertThat(resultString).isEqualTo(expectedResultString);
    }

    private StringBuilder setupOutput() {
        StringBuilder b = new StringBuilder();
        Mockito.doAnswer((Answer<Object>) invocationOnMock -> {
            b.append(invocationOnMock.getArguments()[0]).append("\n");
            return null;
        }).when(outputStream).println(ArgumentMatchers.anyString());
        return b;
    }
}
