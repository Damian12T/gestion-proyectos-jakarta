package com.espe.gestion.controller;

/**
 * Error de validacion de datos ingresados por el usuario.
 * Su mensaje se muestra tal cual en el formulario.
 */
public class ValidacionException extends Exception {
    private static final long serialVersionUID = 1L;

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
