-- =====================================================
-- Sistema de Gestión de Proyectos - Script SQL
-- Base de Datos: gestion_proyectos
-- =====================================================

DROP DATABASE IF EXISTS gestion_proyectos;
CREATE DATABASE gestion_proyectos 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE gestion_proyectos;

-- =====================================================
-- Tabla: clientes
-- =====================================================
CREATE TABLE clientes (
    id_cliente BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID único del cliente',
    nombre VARCHAR(150) NOT NULL UNIQUE COMMENT 'Nombre del cliente',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB COMMENT='Tabla de clientes';

-- =====================================================
-- Tabla: jefes_proyecto
-- =====================================================
CREATE TABLE jefes_proyecto (
    id_jefe BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID único del jefe',
    nombres VARCHAR(150) NOT NULL UNIQUE COMMENT 'Nombres del jefe de proyecto',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    INDEX idx_nombres (nombres)
) ENGINE=InnoDB COMMENT='Tabla de jefes de proyecto';

-- =====================================================
-- Tabla: proyectos
-- =====================================================
CREATE TABLE proyectos (
    id_proyecto BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID único del proyecto',
    nombre VARCHAR(200) NOT NULL COMMENT 'Nombre del proyecto',
    presupuesto DECIMAL(15,2) NOT NULL COMMENT 'Presupuesto del proyecto',
    id_cliente BIGINT NOT NULL COMMENT 'FK cliente',
    id_jefe BIGINT NOT NULL COMMENT 'FK jefe',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',

    CONSTRAINT fk_proyecto_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES clientes(id_cliente)
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,

    CONSTRAINT fk_proyecto_jefe 
        FOREIGN KEY (id_jefe) 
        REFERENCES jefes_proyecto(id_jefe)
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,

    INDEX idx_nombre (nombre),
    INDEX idx_cliente (id_cliente),
    INDEX idx_jefe (id_jefe)
) ENGINE=InnoDB COMMENT='Tabla de proyectos';

-- =====================================================
-- Datos de ejemplo
-- =====================================================

INSERT INTO clientes (nombre) VALUES
('Empresa ABC S.A.'),
('Tech Solutions Inc.'),
('Consultores Globales'),
('Digital Innovations'),
('Enterprise Systems'),
('Cloud Computing Co.'),
('Development Labs');

INSERT INTO jefes_proyecto (nombres) VALUES
('Juan García López'),
('María Rodríguez Martínez'),
('Carlos Hernández Silva'),
('Ana María Flores García'),
('Roberto Gutiérrez Pérez'),
('Patricia López Sánchez'),
('Miguel Ángel Torres');

INSERT INTO proyectos (nombre, presupuesto, id_cliente, id_jefe) VALUES
('Desarrollo Sistema Web', 50000, 1, 1),
('Migración a Cloud', 75000, 2, 2),
('Modernización Aplicativo', 35000, 3, 3),
('Implementación ERP', 150000, 4, 4),
('Transformación Digital', 120000, 5, 5),
('API REST Integration', 45000, 6, 6),
('Mobile App Development', 85000, 7, 7);
