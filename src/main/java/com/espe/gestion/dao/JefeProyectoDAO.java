package com.espe.gestion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.espe.gestion.model.JefeProyecto;
import com.espe.gestion.utils.Db;

/**
 * Acceso a datos de la tabla jefes_proyecto.
 * Los errores de base de datos se propagan para que el controlador informe al usuario.
 */
public class JefeProyectoDAO {

    /**
     * Obtiene todos los jefes de proyecto
     */
    public List<JefeProyecto> findAll() throws SQLException {
        String sql = "SELECT id_jefe, nombres FROM jefes_proyecto ORDER BY id_jefe DESC";
        List<JefeProyecto> jefes = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                jefes.add(new JefeProyecto(rs.getLong("id_jefe"), rs.getString("nombres")));
            }
        }
        return jefes;
    }

    /**
     * Obtiene un jefe por ID, o null si no existe
     */
    public JefeProyecto findById(Long id) throws SQLException {
        String sql = "SELECT id_jefe, nombres FROM jefes_proyecto WHERE id_jefe = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new JefeProyecto(rs.getLong("id_jefe"), rs.getString("nombres")) : null;
            }
        }
    }

    /**
     * Crea un nuevo jefe y devuelve el ID generado
     */
    public long create(JefeProyecto jefe) throws SQLException {
        String sql = "INSERT INTO jefes_proyecto (nombres) VALUES (?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, jefe.getNombres());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        }
    }

    /**
     * Actualiza un jefe; devuelve false si el ID no existe
     */
    public boolean update(JefeProyecto jefe) throws SQLException {
        String sql = "UPDATE jefes_proyecto SET nombres = ? WHERE id_jefe = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jefe.getNombres());
            ps.setLong(2, jefe.getId_jefe());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un jefe; lanza SQLException si tiene proyectos asociados (FK)
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM jefes_proyecto WHERE id_jefe = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
