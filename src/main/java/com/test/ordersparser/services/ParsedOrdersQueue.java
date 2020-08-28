package com.test.ordersparser.services;

import com.test.ordersparser.dto.ParsedOrder;

/**
 * @author Mikhail
 */
public interface ParsedOrdersQueue {
    void parserStartedWork();

    void parserFinishedWork();

    void put(ParsedOrder parsedOrder)
            throws InterruptedException;

    ParsedOrder take()
                    throws InterruptedException;
}
