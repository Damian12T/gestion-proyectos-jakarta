package com.espe.gestion.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Proyecto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id_proyecto;
    private String nombre;
    private BigDecimal presupuesto;
    private Long id_cliente;
    private Long id_jefe;
    
    // Objetos relacionados
    private Cliente cliente;
    private JefeProyecto jefeProyecto;

    // Constructores
    public Proyecto() {
        super();
    }

    public Proyecto(Long id_proyecto, String nombre, BigDecimal presupuesto, 
                   Long id_cliente, Long id_jefe) {
        this.id_proyecto = id_proyecto;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.id_cliente = id_cliente;
        this.id_jefe = id_jefe;
    }

    // Getters y Setters
    public Long getId_proyecto() {
        return id_proyecto;
    }

    public void setId_proyecto(Long id_proyecto) {
        this.id_proyecto = id_proyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Long getId_jefe() {
        return id_jefe;
    }

    public void setId_jefe(Long id_jefe) {
        this.id_jefe = id_jefe;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public JefeProyecto getJefeProyecto() {
        return jefeProyecto;
    }

    public void setJefeProyecto(JefeProyecto jefeProyecto) {
        this.jefeProyecto = jefeProyecto;
    }

    @Override
    public String toString() {
        return "Proyecto [id_proyecto=" + id_proyecto + ", nombre=" + nombre 
               + ", presupuesto=" + presupuesto + ", id_cliente=" + id_cliente 
               + ", id_jefe=" + id_jefe + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_proyecto == null) ? 0 : id_proyecto.hashCode());
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
        Proyecto other = (Proyecto) obj;
        if (id_proyecto == null) {
            if (other.id_proyecto != null)
                return false;
        } else if (!id_proyecto.equals(other.id_proyecto))
            return false;
        return true;
    }
}
