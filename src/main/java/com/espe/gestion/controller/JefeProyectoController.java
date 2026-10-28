package com.espe.gestion.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.espe.gestion.dao.JefeProyectoDAO;
import com.espe.gestion.model.JefeProyecto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/jefes")
public class JefeProyectoController extends ControladorBase {
    private static final long serialVersionUID = 1L;
    private static final String RUTA = "/jefes";
    private static final int MAX_NOMBRES = 150;

    private JefeProyectoDAO jefeDAO;

    @Override
    public void init() throws ServletException {
        jefeDAO = new JefeProyectoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (parametro(req, "action")) {
            case "new":
                mostrarFormulario(req, resp, new JefeProyecto(), "create");
                break;
            case "edit":
                mostrarEditar(req, resp);
                break;
            default:
                listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (parametro(req, "action")) {
            case "create":
                guardar(req, resp, false);
                break;
            case "update":
                guardar(req, resp, true);
                break;
            case "delete":
                eliminar(req, resp);
                break;
            default:
                redirigir(req, resp, RUTA);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("jefes", jefeDAO.findAll());
        } catch (SQLException e) {
            req.setAttribute("jefes", List.of());
            req.setAttribute("error", mensajeBD(e));
        }
        req.getRequestDispatcher("/WEB-INF/jefe-list.jsp").forward(req, resp);
    }

    private void mostrarEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            JefeProyecto jefe = jefeDAO.findById(parseId(parametro(req, "id")));
            if (jefe == null) {
                error(req, "El jefe de proyecto solicitado no existe.");
                redirigir(req, resp, RUTA);
                return;
            }
            mostrarFormulario(req, resp, jefe, "update");
        } catch (ValidacionException e) {
            error(req, e.getMessage());
            redirigir(req, resp, RUTA);
        } catch (SQLException e) {
            error(req, mensajeBD(e));
            redirigir(req, resp, RUTA);
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp,
                                   JefeProyecto jefe, String action)
            throws ServletException, IOException {
        req.setAttribute("jefe", jefe);
        req.setAttribute("action", action);
        req.getRequestDispatcher("/WEB-INF/jefe-form.jsp").forward(req, resp);
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp, boolean actualizar)
            throws ServletException, IOException {
        String action = actualizar ? "update" : "create";
        JefeProyecto jefe = new JefeProyecto();
        jefe.setNombres(parametro(req, "nombres"));

        try {
            if (actualizar) {
                jefe.setId_jefe(parseId(parametro(req, "id")));
            }
            validarTexto(jefe.getNombres(), "El nombre del jefe", MAX_NOMBRES);

            if (actualizar) {
                if (!jefeDAO.update(jefe)) {
                    error(req, "El jefe de proyecto ya no existe.");
                    redirigir(req, resp, RUTA);
                    return;
                }
                exito(req, "Jefe de proyecto actualizado correctamente.");
            } else {
                jefeDAO.create(jefe);
                exito(req, "Jefe de proyecto creado correctamente.");
            }
            redirigir(req, resp, RUTA);
        } catch (ValidacionException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("error", e.getMessage());
            mostrarFormulario(req, resp, jefe, action);
        } catch (SQLException e) {
            req.setAttribute("error", mensajeBD(e));
            mostrarFormulario(req, resp, jefe, action);
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (jefeDAO.delete(parseId(parametro(req, "id")))) {
                exito(req, "Jefe de proyecto eliminado correctamente.");
            } else {
                error(req, "El jefe de proyecto ya no existe.");
            }
        } catch (ValidacionException e) {
            error(req, e.getMessage());
        } catch (SQLException e) {
            error(req, mensajeBD(e));
        }
        redirigir(req, resp, RUTA);
    }
}
