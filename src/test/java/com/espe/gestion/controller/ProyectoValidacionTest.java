package com.espe.gestion.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.espe.gestion.model.Proyecto;

class ProyectoValidacionTest {

    private final ProyectoController controlador = new ProyectoController();

    private Proyecto proyecto(String nombre, String presupuesto, Long cliente, Long jefe) {
        return new Proyecto(null, nombre, controlador.decimalOpcional(presupuesto), cliente, jefe);
    }

    private String errorDe(String nombre, String presupuesto, Long cliente, Long jefe) {
        return assertThrows(ValidacionException.class,
                () -> controlador.validar(proyecto(nombre, presupuesto, cliente, jefe), presupuesto)).getMessage();
    }

    @Test
    void aceptaUnProyectoValido() {
        assertDoesNotThrow(() -> controlador.validar(proyecto("Portal", "12345.67", 1L, 2L), "12345.67"));
    }

    @ParameterizedTest
    @CsvSource({
        "'',        El presupuesto es obligatorio.",
        "abc,       El presupuesto debe ser un número válido.",
        "0,         El presupuesto debe ser mayor a cero.",
        "-50,       El presupuesto debe ser mayor a cero.",
        "10.555,    El presupuesto admite como máximo 2 decimales.",
        "99999999999999, El presupuesto supera el máximo permitido."
    })
    void rechazaPresupuestosInvalidos(String presupuesto, String mensaje) {
        assertEquals(mensaje, errorDe("Portal", presupuesto, 1L, 1L));
    }

    @Test
    void aceptaCerosFinalesComoDosDecimales() {
        assertDoesNotThrow(() -> controlador.validar(proyecto("Portal", "10.500", 1L, 1L), "10.500"));
    }

    @Test
    void exigeNombreClienteYJefe() {
        assertEquals("El nombre del proyecto es obligatorio.", errorDe("", "10", 1L, 1L));
        assertEquals("Debe seleccionar un cliente.", errorDe("Portal", "10", null, 1L));
        assertEquals("Debe seleccionar un jefe de proyecto.", errorDe("Portal", "10", 1L, null));
    }

    @Test
    void decimalOpcionalDevuelveNullSiNoEsNumero() {
        assertNull(controlador.decimalOpcional("1,5"));
        assertEquals(new BigDecimal("1.5"), controlador.decimalOpcional("1.5"));
    }
}
