package com.test.ordersparser.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Mikhail
 */
@JsonPropertyOrder({"id", "amount", "comment", "filename", "line", "result" })
public class ParsedOrder {

    private String id;
    private BigDecimal amount;
    private String comment;
    @JsonProperty("filename")
    private String fileName;
    private int line;
    private String result;

    public ParsedOrder(String fileName, int lineNumber) {
        this.fileName = fileName;
        this.line = lineNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
