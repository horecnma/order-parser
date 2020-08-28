package com.test.ordersparser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ordersparser.dto.ParsedOrder;

/**
 * @author Mikhail
 */
@Service
public class OrderConverter {
    @Autowired
    private ObjectMapper objectMapper;

    public String convertOrder(ParsedOrder order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Shouldn't happen", e);
        }
    }
}
