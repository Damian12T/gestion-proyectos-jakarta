package com.espe.gestion.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ControladorBaseTest {

    // Referencia fuerte para que el nivel configurado no se pierda
    private static final Logger LOG = Logger.getLogger(ControladorBase.class.getName());

    @BeforeAll
    static void silenciarLogDeErroresEsperados() {
        LOG.setLevel(Level.OFF);
    }

    // Subclase minima: ControladorBase es abstracta
    private final ControladorBase controlador = new ControladorBase() {
        private static final long serialVersionUID = 1L;
    };

    @Test
    void idOpcionalSoloAceptaEnterosPositivos() {
        assertEquals(5L, controlador.idOpcional("5"));
        assertNull(controlador.idOpcional("0"));
        assertNull(controlador.idOpcional("-3"));
        assertNull(controlador.idOpcional("abc"));
        assertNull(controlador.idOpcional(""));
        assertNull(controlador.idOpcional("1 OR 1=1"));
    }

    @Test
    void parseIdLanzaValidacionConIdInvalido() {
        ValidacionException e = assertThrows(ValidacionException.class, () -> controlador.parseId("x"));
        assertEquals("Identificador no válido.", e.getMessage());
    }

    @Test
    void validarTextoExigeValorYLongitudMaxima() throws ValidacionException {
        controlador.validarTexto("Cliente", "El nombre", 10);
        assertThrows(ValidacionException.class, () -> controlador.validarTexto("", "El nombre", 10));
        assertThrows(ValidacionException.class, () -> controlador.validarTexto("x".repeat(11), "El nombre", 10));
    }

    @Test
    void mensajeBDTraduceLosCodigosDeMySQL() {
        assertTrue(controlador.mensajeBD(new SQLException("dup", "23000", 1062)).startsWith("Ya existe"));
        assertTrue(controlador.mensajeBD(new SQLException("fk", "23000", 1451)).contains("proyectos asociados"));
        assertTrue(controlador.mensajeBD(new SQLException("fk", "23000", 1452)).contains("ya no existe"));
        assertTrue(controlador.mensajeBD(new SQLException("conn", "08S01", 0)).contains("No se pudo conectar"));
        assertTrue(controlador.mensajeBD(new SQLException("otro", "HY000", 9999)).startsWith("Ocurrió un error"));
    }
}
