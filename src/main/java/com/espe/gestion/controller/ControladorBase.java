package com.espe.gestion.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utilidades comunes a los controladores CRUD: lectura de parametros,
 * mensajes flash (patron Post/Redirect/Get) y traduccion de errores de BD.
 */
public abstract class ControladorBase extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ControladorBase.class.getName());

    // Codigos de error de MySQL
    private static final int ER_DUP_ENTRY = 1062;
    private static final int ER_ROW_IS_REFERENCED = 1451;
    private static final int ER_NO_REFERENCED_ROW = 1452;
    private static final int ER_DATA_TOO_LONG = 1406;

    /** Devuelve el parametro sin espacios extremos, o "" si no viene. */
    protected String parametro(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }

    protected Long parseId(String valor) throws ValidacionException {
        Long id = idOpcional(valor);
        if (id == null) {
            throw new ValidacionException("Identificador no válido.");
        }
        return id;
    }

    /** Devuelve el ID si es un entero positivo, o null en cualquier otro caso. */
    protected Long idOpcional(String valor) {
        try {
            long id = Long.parseLong(valor);
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected void validarTexto(String valor, String campo, int maximo) throws ValidacionException {
        if (valor == null || valor.isEmpty()) {
            throw new ValidacionException(campo + " es obligatorio.");
        }
        if (valor.length() > maximo) {
            throw new ValidacionException(campo + " no puede superar " + maximo + " caracteres.");
        }
    }

    protected void exito(HttpServletRequest req, String mensaje) {
        req.getSession().setAttribute("flashExito", mensaje);
    }

    protected void error(HttpServletRequest req, String mensaje) {
        req.getSession().setAttribute("flashError", mensaje);
    }

    protected void redirigir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }

    /** Traduce una SQLException a un mensaje comprensible y registra el detalle tecnico en el log. */
    protected String mensajeBD(SQLException e) {
        LOG.log(Level.WARNING, "Error de base de datos", e);
        switch (e.getErrorCode()) {
            case ER_DUP_ENTRY:
                return "Ya existe un registro con ese nombre.";
            case ER_ROW_IS_REFERENCED:
                return "No se puede eliminar: tiene proyectos asociados. Reasigne o elimine esos proyectos primero.";
            case ER_NO_REFERENCED_ROW:
                return "El cliente o jefe seleccionado ya no existe.";
            case ER_DATA_TOO_LONG:
                return "Uno de los valores es demasiado largo.";
            default:
                String estado = e.getSQLState();
                if (estado != null && estado.startsWith("08")) {
                    return "No se pudo conectar con la base de datos. Verifique que MySQL esté en ejecución.";
                }
                return "Ocurrió un error en la base de datos. Intente nuevamente.";
        }
    }
}
