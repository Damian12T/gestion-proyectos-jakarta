package com.espe.gestion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.espe.gestion.model.Cliente;
import com.espe.gestion.model.JefeProyecto;
import com.espe.gestion.model.Proyecto;
import com.espe.gestion.utils.Db;

/**
 * Acceso a datos de la tabla proyectos.
 * Las relaciones (cliente y jefe) se cargan con un JOIN en una sola consulta.
 */
public class ProyectoDAO {

    private static final String SELECT_BASE =
            "SELECT p.id_proyecto, p.nombre, p.presupuesto, p.id_cliente, p.id_jefe, "
          + "c.nombre AS cliente_nombre, j.nombres AS jefe_nombres "
          + "FROM proyectos p "
          + "JOIN clientes c ON c.id_cliente = p.id_cliente "
          + "JOIN jefes_proyecto j ON j.id_jefe = p.id_jefe ";

    /**
     * Obtiene todos los proyectos con sus relaciones
     */
    public List<Proyecto> findAll() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY p.id_proyecto DESC";
        List<Proyecto> proyectos = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                proyectos.add(mapear(rs));
            }
        }
        return proyectos;
    }

    /**
     * Obtiene un proyecto por ID con sus relaciones, o null si no existe
     */
    public Proyecto findById(Long id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id_proyecto = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /**
     * Crea un nuevo proyecto y devuelve el ID generado
     */
    public long create(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO proyectos (nombre, presupuesto, id_cliente, id_jefe) VALUES (?,?,?,?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, proyecto.getNombre());
            ps.setBigDecimal(2, proyecto.getPresupuesto());
            ps.setLong(3, proyecto.getId_cliente());
            ps.setLong(4, proyecto.getId_jefe());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        }
    }

    /**
     * Actualiza un proyecto; devuelve false si el ID no existe
     */
    public boolean update(Proyecto proyecto) throws SQLException {
        String sql = "UPDATE proyectos SET nombre = ?, presupuesto = ?, id_cliente = ?, id_jefe = ? WHERE id_proyecto = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proyecto.getNombre());
            ps.setBigDecimal(2, proyecto.getPresupuesto());
            ps.setLong(3, proyecto.getId_cliente());
            ps.setLong(4, proyecto.getId_jefe());
            ps.setLong(5, proyecto.getId_proyecto());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un proyecto por ID
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM proyectos WHERE id_proyecto = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Proyecto mapear(ResultSet rs) throws SQLException {
        Proyecto proyecto = new Proyecto(
            rs.getLong("id_proyecto"),
            rs.getString("nombre"),
            rs.getBigDecimal("presupuesto"),
            rs.getLong("id_cliente"),
            rs.getLong("id_jefe")
        );
        proyecto.setCliente(new Cliente(proyecto.getId_cliente(), rs.getString("cliente_nombre")));
        proyecto.setJefeProyecto(new JefeProyecto(proyecto.getId_jefe(), rs.getString("jefe_nombres")));
        return proyecto;
    }
}
