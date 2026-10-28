package com.espe.gestion.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.espe.gestion.dao.ClienteDAO;
import com.espe.gestion.model.Cliente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clientes")
public class ClienteController extends ControladorBase {
    private static final long serialVersionUID = 1L;
    private static final String RUTA = "/clientes";
    private static final int MAX_NOMBRE = 150;

    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (parametro(req, "action")) {
            case "new":
                mostrarFormulario(req, resp, new Cliente(), "create");
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
            req.setAttribute("clientes", clienteDAO.findAll());
        } catch (SQLException e) {
            req.setAttribute("clientes", List.of());
            req.setAttribute("error", mensajeBD(e));
        }
        req.getRequestDispatcher("/WEB-INF/cliente-list.jsp").forward(req, resp);
    }

    private void mostrarEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Cliente cliente = clienteDAO.findById(parseId(parametro(req, "id")));
            if (cliente == null) {
                error(req, "El cliente solicitado no existe.");
                redirigir(req, resp, RUTA);
                return;
            }
            mostrarFormulario(req, resp, cliente, "update");
        } catch (ValidacionException e) {
            error(req, e.getMessage());
            redirigir(req, resp, RUTA);
        } catch (SQLException e) {
            error(req, mensajeBD(e));
            redirigir(req, resp, RUTA);
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp,
                                   Cliente cliente, String action)
            throws ServletException, IOException {
        req.setAttribute("cliente", cliente);
        req.setAttribute("action", action);
        req.getRequestDispatcher("/WEB-INF/cliente-form.jsp").forward(req, resp);
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp, boolean actualizar)
            throws ServletException, IOException {
        String action = actualizar ? "update" : "create";
        Cliente cliente = new Cliente();
        cliente.setNombre(parametro(req, "nombre"));

        try {
            if (actualizar) {
                cliente.setId_cliente(parseId(parametro(req, "id")));
            }
            validarTexto(cliente.getNombre(), "El nombre del cliente", MAX_NOMBRE);

            if (actualizar) {
                if (!clienteDAO.update(cliente)) {
                    error(req, "El cliente ya no existe.");
                    redirigir(req, resp, RUTA);
                    return;
                }
                exito(req, "Cliente actualizado correctamente.");
            } else {
                clienteDAO.create(cliente);
                exito(req, "Cliente creado correctamente.");
            }
            redirigir(req, resp, RUTA);
        } catch (ValidacionException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("error", e.getMessage());
            mostrarFormulario(req, resp, cliente, action);
        } catch (SQLException e) {
            req.setAttribute("error", mensajeBD(e));
            mostrarFormulario(req, resp, cliente, action);
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (clienteDAO.delete(parseId(parametro(req, "id")))) {
                exito(req, "Cliente eliminado correctamente.");
            } else {
                error(req, "El cliente ya no existe.");
            }
        } catch (ValidacionException e) {
            error(req, e.getMessage());
        } catch (SQLException e) {
            error(req, mensajeBD(e));
        }
        redirigir(req, resp, RUTA);
    }
}
