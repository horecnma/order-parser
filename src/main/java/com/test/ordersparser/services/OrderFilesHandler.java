package com.test.ordersparser.services;

import java.io.File;
import java.util.List;

/**
 * @author Mikhail
 */
public interface OrderFilesHandler {
    void handleFiles(List<File> files);
}
