<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Damian| Gestión de Proyectos</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Google Font -->
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">

<!-- FontAwesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<style>
:root {
    --primary: #667eea;
    --secondary: #10b981;
    --accent: #0ea5e9;
    --dark: #f4f6f9;
    --card-bg: #fff;
    --text-dark: #1e293b;
}

/* RESET */
* { margin:0; padding:0; box-sizing:border-box; }

body {
    font-family: 'Inter', sans-serif;
    background: linear-gradient(135deg, #e0e7ff, #f4f6f9);
    color: var(--text-dark);
}

/* NAVBAR */
.navbar {
    background: var(--card-bg);
    padding: 20px 50px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #d1d5db;
    border-radius: 0 0 20px 20px;
    box-shadow: 0 8px 25px rgba(0,0,0,0.08);
}

.navbar h2 {
    font-weight: 700;
    color: var(--primary);
    font-size: 1.8rem;
}

.navbar span {
    font-size: 0.9rem;
    color: #64748b;
}

/* CONTENEDOR */
.container {
    padding: 60px 40px;
    max-width: 1200px;
    margin: 40px auto 0 auto;
}

.title h1 {
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 10px;
}

.title p {
    color: #64748b;
    font-size: 1.1rem;
    margin-bottom: 40px;
}

/* GRID TARJETAS */
.grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 30px;
}

.card {
    background: var(--card-bg);
    padding: 35px;
    border-radius: 20px;
    text-decoration: none;
    color: inherit;
    transition: 0.4s ease;
    border: 1px solid rgba(0,0,0,0.05);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
}

.card i {
    font-size: 2.5rem;
    margin-bottom: 20px;
    transition: 0.3s;
}

.card h3 {
    font-size: 1.4rem;
    margin-bottom: 10px;
    font-weight: 600;
}

.card p {
    font-size: 0.95rem;
    color: #64748b;
}

/* COLORES POR TIPO */
.card.proyectos i { color: var(--primary); }
.card.clientes i { color: var(--secondary); }
.card.jefes i { color: var(--accent); }

/* HOVER */
.card:hover {
    transform: translateY(-8px);
    box-shadow: 0 20px 40px rgba(0,0,0,0.15);
}

/* FOOTER */
.footer {
    margin-top: 60px;
    text-align: center;
    font-size: 0.85rem;
    color: #94a3b8;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
}

.footer i {
    color: var(--secondary);
}
</style>
</head>

<body>

<!-- NAVBAR -->
<div class="navbar">
    <h2>Gestión de Proyectos</h2>
   
</div>

<!-- CONTENIDO -->
<div class="container">
    <div class="title">
        <h1>Panel Administrativo</h1>
        <p>Administra proyectos, clientes y jefes de forma ordenada.</p>
    </div>

    <div class="grid">

        <a href="${pageContext.request.contextPath}/proyectos" class="card proyectos">
            <i class="fa-solid fa-diagram-project"></i>
            <h3>Gestión de Proyectos</h3>
            <p>Control de presupuestos, asignaciones y estados activos.</p>
        </a>

        <a href="${pageContext.request.contextPath}/clientes" class="card clientes">
            <i class="fa-solid fa-users"></i>
            <h3>Gestión de Clientes</h3>
            <p>Registro, actualización y control de información empresarial.</p>
        </a>

        <a href="${pageContext.request.contextPath}/jefes" class="card jefes">
            <i class="fa-solid fa-user-tie"></i>
            <h3>Jefes de Proyecto</h3>
            <p>Administración de responsables y asignaciones estratégicas.</p>
        </a>

    </div>

    <div class="footer">
        <i class="fa-solid fa-layer-group"></i>
        Jakarta EE · JSP · MySQL
    </div>
</div>

</body>
</html>
