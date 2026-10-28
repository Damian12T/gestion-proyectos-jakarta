<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Clientes</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }

    body {
        font-family: 'Segoe UI', Arial, sans-serif;
        background: linear-gradient(135deg, #e0e7ff, #f4f6f9);
        padding: 20px;
        min-height: 100vh;
    }

    .container {
        max-width: 1100px;
        margin: 0 auto;
        background: #fff;
        border-radius: 20px;
        box-shadow: 0 16px 40px rgba(0,0,0,0.12);
        overflow: hidden;
        transition: transform 0.3s;
    }

    .container:hover { transform: translateY(-3px); }

    .header {
        background: linear-gradient(135deg, #667eea, #2563eb);
        color: #fff;
        padding: 30px;
        text-align: center;
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }

    .header h1 {
        font-size: 32px;
        font-weight: 700;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 12px;
    }

    .nav {
        display: flex;
        gap: 12px;
        justify-content: center;
        padding: 15px;
        background: #f1f5f9;
        border-bottom: 1px solid #ddd;
    }

    .nav a {
        padding: 10px 18px;
        text-decoration: none;
        border-radius: 8px;
        color: #333;
        font-weight: 600;
        transition: 0.3s;
    }

    .nav a.active {
        background: #667eea;
        color: #fff;
        box-shadow: 0 4px 12px rgba(102,126,234,0.3);
    }

    .nav a:hover { background: #2563eb; color: #fff; }

    .content {
        padding: 30px;
    }

    h2 {
        color: #1e293b;
        margin-bottom: 20px;
        font-size: 24px;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .btn-new {
        background: #10b981;
        color: white;
        padding: 12px 24px;
        text-decoration: none;
        border-radius: 10px;
        display: inline-block;
        margin-bottom: 20px;
        font-weight: 600;
        font-size: 1em;
        transition: 0.3s;
        box-shadow: 0 6px 15px rgba(16,185,129,0.3);
    }

    .btn-new:hover {
        background: #059669;
        box-shadow: 0 8px 20px rgba(5,150,105,0.4);
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
        font-size: 15px;
    }

    th {
        background: #2563eb;
        color: white;
        padding: 14px;
        text-align: left;
        font-weight: 600;
        border-radius: 6px 6px 0 0;
    }

    tbody tr:nth-child(even) { background: #f1f5f9; }
    tbody tr:hover { background: #e0e7ff; transition: 0.3s; }

    td {
        padding: 12px;
        border-bottom: 1px solid #ddd;
    }

    .btn-edit, .btn-delete {
        padding: 8px 14px;
        text-decoration: none;
        border-radius: 8px;
        font-size: 0.9em;
        font-weight: 600;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        transition: 0.3s;
    }

    .btn-edit { background: #2563eb; color: #fff; }
    .btn-edit:hover { background: #1d4ed8; }

    .btn-delete { background: #dc2626; color: #fff; border: none; cursor: pointer; font-family: inherit; }
    .btn-delete:hover { background: #b91c1c; }

    .form-inline { display: inline; }

    .empty {
        text-align: center;
        padding: 40px;
        color: #999;
        font-size: 1.1em;
    }
</style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1><i class="fa-solid fa-users"></i> Gestión de Clientes</h1>
    </div>

    <div class="nav">
        <a href="${pageContext.request.contextPath}/proyectos">Proyectos</a>
        <a href="${pageContext.request.contextPath}/clientes" class="active">Clientes</a>
        <a href="${pageContext.request.contextPath}/jefes">Jefes</a>
    </div>

    <div class="content">
        <h2><i class="fa-solid fa-clipboard-list"></i> Lista de Clientes</h2>
        <%@ include file="/WEB-INF/fragmentos/alertas.jspf" %>
        <a href="${pageContext.request.contextPath}/clientes?action=new" class="btn-new">
            <i class="fa-solid fa-plus"></i> Nuevo Cliente
        </a>

        <c:choose>
            <c:when test="${empty clientes}">
                <div class="empty">No hay clientes registrados</div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${clientes}">
                            <tr>
                                <td>${c.id_cliente}</td>
                                <td><c:out value="${c.nombre}"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/clientes?action=edit&id=${c.id_cliente}" class="btn-edit">
                                        <i class="fa-solid fa-pen"></i> Editar
                                    </a>
                                    <form method="POST" action="${pageContext.request.contextPath}/clientes" class="form-inline"
                                          onsubmit="return confirm('¿Eliminar este cliente?')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${c.id_cliente}">
                                        <button type="submit" class="btn-delete">
                                            <i class="fa-solid fa-trash"></i> Eliminar
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
