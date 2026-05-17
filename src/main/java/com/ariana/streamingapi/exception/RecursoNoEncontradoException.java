package com.ariana.streamingapi.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}

//ponemos runtimeexception porque exception a secas obliga a lanzar throws, estas otras se lanzan libremente
//y el restcontrolleradvice las captura en toda la app