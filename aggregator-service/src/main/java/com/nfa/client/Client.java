package com.nfa.client;

import java.util.List;

public interface Client<T> {

    List<T> fetchData();
}
