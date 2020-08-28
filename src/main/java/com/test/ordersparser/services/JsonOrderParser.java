package com.test.ordersparser.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ordersparser.dto.ParsedOrder;

/**
 * @author Mikhail
 */
@Service
public class JsonOrderParser implements OrderParser {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean canHandleFile(File f) {
        return f.getName().toLowerCase().endsWith(".json");
    }

    @Override
    public CloseableIterator<ParsedOrder> parseFile(File file)
            throws IOException {
        Stream<ParsedOrder> stream = Files.lines(file.toPath())
                                          .map(new FunctionWithCounter() {
                                              @Override
                                              public ParsedOrder apply(String line) {
                                                  return parseLine(line, file.getName(), count++);
                                              }
                                          });
        return new CloseableIterator<>(stream);
    }

    private ParsedOrder parseLine(String line, String fileName, int lineNumber) {
        try {
            InputJsonObject values = objectMapper.readValue(line, InputJsonObject.class);
            return toParsedOrder(values, line, fileName, lineNumber);
        } catch (Exception e) {
            ParsedOrder result = new ParsedOrder(fileName, lineNumber);
            result.setResult(String.format("Can't parse json line %s '%s'. Error: %s", lineNumber, line, e.getMessage()));
            return result;
        }
    }

    private ParsedOrder toParsedOrder(InputJsonObject values, String line, String fileName, int lineNumber) {
        ParsedOrder result = new ParsedOrder(fileName, lineNumber);
        result.setId(values.getOrderId());
        result.setComment(values.getComment());
        Optional<BigDecimal> amount = toBigDecimal(values.getAmount());
        if (amount.isPresent()) {
            result.setAmount(amount.get());
            result.setResult(OrderParser.OK);
        } else {
            result.setAmount(null);
            result.setResult(String.format("Wrong amount value %s in line %s '%s'.", values.getAmount(), line, lineNumber));
        }
        return result;
    }

    private Optional<BigDecimal> toBigDecimal(String amount) {
        try {
            return Optional.of(new BigDecimal(amount));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static class InputJsonObject {
        private String orderId;
        private String amount;
        private String currency;
        private String comment;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

    }

}
