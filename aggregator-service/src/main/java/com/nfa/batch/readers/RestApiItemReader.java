package com.nfa.batch.readers;

import com.nfa.client.Client;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class RestApiItemReader<T> implements ItemReader {

    private final Client client;
    private final Class<T> targetType;
    private Iterator<T> dataIterator;
    private List<T> cachedData;

    public RestApiItemReader(Client client, Class<T> targetType) {
        this.client = client;
        this.targetType = targetType;
    }

    @Override
    public T read() {
        if (fetchDataFromApi() == null) {
            return null;
        }
        if (dataIterator == null) {
            dataIterator = fetchDataFromApi().listIterator();
        }
        return dataIterator.hasNext() ? dataIterator.next() : null;
    }

    private List<T> fetchDataFromApi() {
        if (cachedData == null) {
            cachedData = client.fetchData();
        }
        return cachedData;
    }
}
