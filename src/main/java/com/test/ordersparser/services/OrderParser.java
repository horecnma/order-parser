package com.test.ordersparser.services;

import java.io.File;
import java.io.IOException;

import com.test.ordersparser.dto.ParsedOrder;
import com.test.ordersparser.services.impl.CloseableIterator;

/**
 * @author Mikhail
 */
public interface OrderParser {
    String OK = "OK";

    boolean canHandleFile(File f);

    CloseableIterator<ParsedOrder> parseFile(File file)
            throws IOException;
}
