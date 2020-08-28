package com.test.ordersparser.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ordersparser.dto.ParsedOrder;
import com.test.ordersparser.services.OrderConverter;

/**
 * @author Mikhail
 */
@Service
public class OrderConverterImpl implements OrderConverter {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertOrder(ParsedOrder order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Shouldn't happen", e);
        }
    }
}
