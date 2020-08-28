package com.test.ordersparser.services.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.test.ordersparser.dto.ParsedOrder;
import com.test.ordersparser.services.ParsedOrdersQueue;

/**
 * @author Mikhail
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ParsedOrdersQueueImpl implements ParsedOrdersQueue {
    private final BlockingQueue<ParsedOrder> queue = new ArrayBlockingQueue<>(10);
    private final AtomicInteger parsersCount = new AtomicInteger(0);

    @Override
    public void parserStartedWork() {
        parsersCount.incrementAndGet();
    }

    @Override
    public void parserFinishedWork() {
        parsersCount.decrementAndGet();
    }

    @Override
    public void put(ParsedOrder parsedOrder)
            throws InterruptedException {
        boolean inserted = queue.offer(parsedOrder, 10, TimeUnit.SECONDS);
        if (!inserted) {
            throw new IllegalStateException("Shouldn't happen: queue is not polled");
        }
    }

    @Override
    public ParsedOrder take()
            throws InterruptedException {
        while(true) {
            if (parsersCount.get() == 0 && queue.peek() == null) {
                return null;
            }
            ParsedOrder head = queue.poll(300, TimeUnit.MILLISECONDS);
            if (head != null) {
                return head;
            }
        }
    }
}
