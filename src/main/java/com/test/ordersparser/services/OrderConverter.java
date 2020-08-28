package com.test.ordersparser.services;

import com.test.ordersparser.dto.ParsedOrder;

/**
 * @author Mikhail
 */
public interface OrderConverter {
    String convertOrder(ParsedOrder order);
}
