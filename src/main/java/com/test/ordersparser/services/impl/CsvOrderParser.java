package com.test.ordersparser.services.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.test.ordersparser.dto.ParsedOrder;
import com.test.ordersparser.services.OrderParser;

/**
 * @author Mikhail
 */
@Service
public class CsvOrderParser implements OrderParser {
    public static final CSVParser CSV_PARSER = new CSVParserBuilder().withSeparator(',').build();
    public static final int INDEX_ID = 0;
    public static final int INDEX_AMOUNT = 1;
    public static final int INDEX_CURRENCY = 2;
    public static final int INDEX_COMMENT = 3;

    @Override
    public boolean canHandleFile(File f) {
        return f.getName().toLowerCase().endsWith(".csv");
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
            String[] values = CSV_PARSER.parseLine(line);
            return toParsedOrder(values, line, fileName, lineNumber);
        } catch (Exception e) {
            ParsedOrder result = new ParsedOrder(fileName, lineNumber);
            result.setResult(String.format("Can't parse csv line %s '%s'. Error: %s", lineNumber, line, e.getMessage()));
            return result;
        }
    }

    private ParsedOrder toParsedOrder(String[] values, String line, String fileName, int lineNumber) {
        ParsedOrder result = new ParsedOrder(fileName, lineNumber);
        result.setId(values[INDEX_ID]);
        result.setComment(values[INDEX_COMMENT]);
        Optional<BigDecimal> amount = toBigDecimal(values[INDEX_AMOUNT]);
        if (amount.isPresent()) {
            result.setAmount(amount.get());
            result.setResult(OrderParser.OK);
        } else {
            result.setAmount(null);
            result.setResult(String.format("Wrong amount value %s in line %s '%s'.", values[INDEX_AMOUNT], line, lineNumber));
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
}
