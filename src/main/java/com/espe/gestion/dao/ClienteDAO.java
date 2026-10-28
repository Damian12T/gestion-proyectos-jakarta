package com.espe.gestion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.espe.gestion.model.Cliente;
import com.espe.gestion.utils.Db;

/**
 * Acceso a datos de la tabla clientes.
 * Los errores de base de datos se propagan para que el controlador informe al usuario.
 */
public class ClienteDAO {

    /**
     * Obtiene todos los clientes de la BD
     */
    public List<Cliente> findAll() throws SQLException {
        String sql = "SELECT id_cliente, nombre FROM clientes ORDER BY id_cliente DESC";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(rs.getLong("id_cliente"), rs.getString("nombre")));
            }
        }
        return clientes;
    }

    /**
     * Obtiene un cliente por ID, o null si no existe
     */
    public Cliente findById(Long id) throws SQLException {
        String sql = "SELECT id_cliente, nombre FROM clientes WHERE id_cliente = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new Cliente(rs.getLong("id_cliente"), rs.getString("nombre")) : null;
            }
        }
    }

    /**
     * Crea un nuevo cliente y devuelve el ID generado
     */
    public long create(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre) VALUES (?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        }
    }

    /**
     * Actualiza un cliente; devuelve false si el ID no existe
     */
    public boolean update(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ? WHERE id_cliente = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setLong(2, cliente.getId_cliente());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un cliente; lanza SQLException si tiene proyectos asociados (FK)
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
