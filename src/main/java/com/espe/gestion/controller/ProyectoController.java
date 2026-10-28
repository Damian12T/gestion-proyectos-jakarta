package com.espe.gestion.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.espe.gestion.dao.ClienteDAO;
import com.espe.gestion.dao.JefeProyectoDAO;
import com.espe.gestion.dao.ProyectoDAO;
import com.espe.gestion.model.Proyecto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/proyectos")
public class ProyectoController extends ControladorBase {
    private static final long serialVersionUID = 1L;
    private static final String RUTA = "/proyectos";
    private static final int MAX_NOMBRE = 200;
    // Limite de la columna DECIMAL(15,2)
    private static final BigDecimal PRESUPUESTO_MAXIMO = new BigDecimal("9999999999999.99");

    private ProyectoDAO proyectoDAO;
    private ClienteDAO clienteDAO;
    private JefeProyectoDAO jefeDAO;

    @Override
    public void init() throws ServletException {
        proyectoDAO = new ProyectoDAO();
        clienteDAO = new ClienteDAO();
        jefeDAO = new JefeProyectoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (parametro(req, "action")) {
            case "new":
                mostrarFormulario(req, resp, new Proyecto(), "create");
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
            req.setAttribute("proyectos", proyectoDAO.findAll());
        } catch (SQLException e) {
            req.setAttribute("proyectos", List.of());
            req.setAttribute("error", mensajeBD(e));
        }
        req.getRequestDispatcher("/WEB-INF/proyecto-list.jsp").forward(req, resp);
    }

    private void mostrarEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Proyecto proyecto = proyectoDAO.findById(parseId(parametro(req, "id")));
            if (proyecto == null) {
                error(req, "El proyecto solicitado no existe.");
                redirigir(req, resp, RUTA);
                return;
            }
            mostrarFormulario(req, resp, proyecto, "update");
        } catch (ValidacionException e) {
            error(req, e.getMessage());
            redirigir(req, resp, RUTA);
        } catch (SQLException e) {
            error(req, mensajeBD(e));
            redirigir(req, resp, RUTA);
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp,
                                   Proyecto proyecto, String action)
            throws ServletException, IOException {
        req.setAttribute("proyecto", proyecto);
        req.setAttribute("action", action);
        try {
            req.setAttribute("clientes", clienteDAO.findAll());
            req.setAttribute("jefes", jefeDAO.findAll());
        } catch (SQLException e) {
            req.setAttribute("clientes", List.of());
            req.setAttribute("jefes", List.of());
            req.setAttribute("error", mensajeBD(e));
        }
        req.getRequestDispatcher("/WEB-INF/proyecto-form.jsp").forward(req, resp);
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp, boolean actualizar)
            throws ServletException, IOException {
        String action = actualizar ? "update" : "create";
        // Se rellena con lo que se pueda interpretar para no perder lo escrito si hay errores
        String presupuestoTexto = parametro(req, "presupuesto");
        Proyecto proyecto = new Proyecto(
            idOpcional(parametro(req, "id")),
            parametro(req, "nombre"),
            decimalOpcional(presupuestoTexto),
            idOpcional(parametro(req, "id_cliente")),
            idOpcional(parametro(req, "id_jefe"))
        );

        try {
            if (actualizar && proyecto.getId_proyecto() == null) {
                throw new ValidacionException("Identificador no válido.");
            }
            validar(proyecto, presupuestoTexto);

            if (actualizar) {
                if (!proyectoDAO.update(proyecto)) {
                    error(req, "El proyecto ya no existe.");
                    redirigir(req, resp, RUTA);
                    return;
                }
                exito(req, "Proyecto actualizado correctamente.");
            } else {
                proyectoDAO.create(proyecto);
                exito(req, "Proyecto creado correctamente.");
            }
            redirigir(req, resp, RUTA);
        } catch (ValidacionException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("error", e.getMessage());
            mostrarFormulario(req, resp, proyecto, action);
        } catch (SQLException e) {
            req.setAttribute("error", mensajeBD(e));
            mostrarFormulario(req, resp, proyecto, action);
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (proyectoDAO.delete(parseId(parametro(req, "id")))) {
                exito(req, "Proyecto eliminado correctamente.");
            } else {
                error(req, "El proyecto ya no existe.");
            }
        } catch (ValidacionException e) {
            error(req, e.getMessage());
        } catch (SQLException e) {
            error(req, mensajeBD(e));
        }
        redirigir(req, resp, RUTA);
    }

    // Visibilidad de paquete para poder probarla de forma aislada
    void validar(Proyecto proyecto, String presupuestoTexto) throws ValidacionException {
        validarTexto(proyecto.getNombre(), "El nombre del proyecto", MAX_NOMBRE);

        BigDecimal presupuesto = proyecto.getPresupuesto();
        if (presupuestoTexto.isEmpty()) {
            throw new ValidacionException("El presupuesto es obligatorio.");
        }
        if (presupuesto == null) {
            throw new ValidacionException("El presupuesto debe ser un número válido.");
        }
        if (presupuesto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionException("El presupuesto debe ser mayor a cero.");
        }
        if (presupuesto.stripTrailingZeros().scale() > 2) {
            throw new ValidacionException("El presupuesto admite como máximo 2 decimales.");
        }
        if (presupuesto.compareTo(PRESUPUESTO_MAXIMO) > 0) {
            throw new ValidacionException("El presupuesto supera el máximo permitido.");
        }
        if (proyecto.getId_cliente() == null) {
            throw new ValidacionException("Debe seleccionar un cliente.");
        }
        if (proyecto.getId_jefe() == null) {
            throw new ValidacionException("Debe seleccionar un jefe de proyecto.");
        }
    }

    BigDecimal decimalOpcional(String valor) {
        try {
            return valor.isEmpty() ? null : new BigDecimal(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
