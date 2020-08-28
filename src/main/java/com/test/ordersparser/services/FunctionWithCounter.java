package com.test.ordersparser.services;

import java.util.function.Function;

import com.test.ordersparser.dto.ParsedOrder;

/**
 * @author Mikhail
 */
abstract class FunctionWithCounter implements Function<String, ParsedOrder> {
    protected int count = 1;
}
