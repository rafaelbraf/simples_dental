package com.simplesdental.testepratico.profissionais.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilterUtils<T> {

    public List<T> filterFields(List<T> objects, List<String> fields) {
        return objects.stream()
                .map(object -> filterFieldsOfObject(object, fields))
                .collect(Collectors.toList());
    }

    private T filterFieldsOfObject(T object, List<String> fields) {
        try {
            T objectWithFieldsFiltered = (T) object.getClass().newInstance();

            for (String field : fields) {
                Field fieldObject = object.getClass().getDeclaredField(field);
                fieldObject.setAccessible(true);

                Object value = fieldObject.get(object);
                fieldObject.set(objectWithFieldsFiltered, value);
            }

            return objectWithFieldsFiltered;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
