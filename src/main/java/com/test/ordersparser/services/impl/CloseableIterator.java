package com.test.ordersparser.services.impl;

import java.io.Closeable;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Mikhail
 */
public class CloseableIterator<T> implements Iterator<T>, Closeable {
    private final Iterator<T> iterator;
    private final Stream<T> stream;

    public CloseableIterator(Stream<T> stream) {
        this.iterator = stream.iterator();
        this.stream = stream;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void close() {
        stream.close();
    }
}
