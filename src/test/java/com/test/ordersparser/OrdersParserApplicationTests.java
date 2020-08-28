package com.test.ordersparser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		String resultString = Arrays.stream(lines).sorted().collect(Collectors.joining("\r\n"));
		String expectedResultString = new String(Files.readAllBytes(expectedResult.getFile().toPath()), StandardCharsets.UTF_8).trim();
		assertThat(resultString).isEqualTo(expectedResultString);
    }

    @Test
    void testBig()
            throws IOException {
        List builder = setupOutput2();

        orderFilesHandler.handleFiles(Arrays.asList(new File("D:\\projects\\big.json"), csv.getFile()));

        System.out.println(builder.get(0));
    }

    private List setupOutput2() {
        List<Integer> i = new ArrayList();
        i.add(0);
        Mockito.doAnswer((Answer<Object>) invocationOnMock -> {
            int element = i.get(0) + 1;
            if (element % 50000 == 0) {
                System.out.println(invocationOnMock.getArguments()[0]);
            }
            i.set(0, element);
            return null;
        }).when(outputStream).println(ArgumentMatchers.anyString());
        return i;
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
