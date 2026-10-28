package com.espe.gestion.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexiones a MySQL.
 *
 * La configuracion se toma de propiedades del sistema (-DDB_URL=...) o de
 * variables de entorno (DB_URL, DB_USER, DB_PASSWORD). Si no estan definidas
 * se usan los valores por defecto para un MySQL local.
 */
public final class Db {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/gestion_proyectos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";

    private static final String URL = config("DB_URL", DEFAULT_URL);
    private static final String USER = config("DB_USER", "root");
    private static final String PASSWORD = config("DB_PASSWORD", "");

    private Db() {
    }

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL: " + e.getMessage());
        }
    }

    /** Obtiene una conexion nueva; quien la pide debe cerrarla (try-with-resources). */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Prioridad: propiedad del sistema, variable de entorno, valor por defecto. */
    private static String config(String clave, String porDefecto) {
        String valor = System.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(clave);
        }
        return (valor == null || valor.isBlank()) ? porDefecto : valor;
    }
}
