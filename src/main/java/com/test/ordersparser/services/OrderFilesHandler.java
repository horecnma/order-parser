package com.test.ordersparser.services;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.ordersparser.dto.ParsedOrder;

/**
 * @author Mikhail
 */
@Service
public class OrderFilesHandler {

    @Autowired
    private List<OrderParser> orderParsers;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private OrderConverter orderConverter;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private PrintStream outputStream;

    public void handleFiles(List<File> files) {
        ParsedOrdersQueue buffer = beanFactory.getBean(ParsedOrdersQueue.class);
        List<Future<?>> futures = new ArrayList<>();
        for (File file : files) {
            OrderParser parser = getParserByFile(file);
            futures.add(startParsing(buffer, file, parser));
        }
        futures.add(startConversion(buffer));
        waitForResults(futures);
    }

    private Future<Void> startParsing(ParsedOrdersQueue resultQueue, File file, OrderParser parser) {
        resultQueue.parserStartedWork();
        return executorService.submit(() -> {
            try (CloseableIterator<ParsedOrder> iterator = parser.parseFile(file)) {
                while (iterator.hasNext()) {
                    resultQueue.put(iterator.next());
                }
            } finally {
                resultQueue.parserFinishedWork();
            }
            return null;
        });
    }

    private Future<Void> startConversion(ParsedOrdersQueue buffer) {
        return executorService.submit(() -> {
            while (true) {
                ParsedOrder order = buffer.take();
                if (order == null) {
                    break;
                }
                outputStream.println(orderConverter.convertOrder(order));
            }
            return null;
        });
    }

    private void waitForResults(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private OrderParser getParserByFile(File file) {
        for (OrderParser parser : orderParsers) {
            if (parser.canHandleFile(file)) {
                return parser;
            }
        }
        throw new IllegalArgumentException("Can't find suitable parser for file " + file.getAbsolutePath());
    }
}
