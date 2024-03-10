package com.simplesdental.testepratico.profissionais.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String resource;
    private final Long id;

    public ResourceNotFoundException(String resource, Long id, String message) {
        super(message);
        this.resource = resource;
        this.id = id;
    }

    public static ResourceNotFoundException forResource(String resource, Long id) {
        return new ResourceNotFoundException(resource, id, String.format("Não foi encontrado %s com id %d", resource, id));
    }
}
