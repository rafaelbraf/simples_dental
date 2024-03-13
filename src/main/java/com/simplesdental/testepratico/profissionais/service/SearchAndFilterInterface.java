package com.simplesdental.testepratico.profissionais.service;

import java.util.List;

public interface SearchAndFilterInterface<T> {

    List<T> searchAndFilter(String query, List<String> fields);

    String[] extractObjectAttributes(T entity);

}
