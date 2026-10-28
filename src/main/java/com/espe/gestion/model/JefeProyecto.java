package com.espe.gestion.model;

import java.io.Serializable;

public class JefeProyecto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id_jefe;
    private String nombres;

    // Constructores
    public JefeProyecto() {
        super();
    }

    public JefeProyecto(Long id_jefe, String nombres) {
        this.id_jefe = id_jefe;
        this.nombres = nombres;
    }

    // Getters y Setters
    public Long getId_jefe() {
        return id_jefe;
    }

    public void setId_jefe(Long id_jefe) {
        this.id_jefe = id_jefe;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @Override
    public String toString() {
        return "JefeProyecto [id_jefe=" + id_jefe + ", nombres=" + nombres + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_jefe == null) ? 0 : id_jefe.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JefeProyecto other = (JefeProyecto) obj;
        if (id_jefe == null) {
            if (other.id_jefe != null)
                return false;
        } else if (!id_jefe.equals(other.id_jefe))
            return false;
        return true;
    }
}
