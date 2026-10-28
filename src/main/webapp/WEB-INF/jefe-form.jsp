<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${action == 'create' ? 'Nuevo Jefe' : 'Editar Jefe'}</title>

    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
            background-color: #f4f6f9;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }

        .form-container {
            background-color: #fff;
            border-radius: 16px;
            box-shadow: 0 12px 30px rgba(0,0,0,0.08);
            padding: 40px 30px;
            width: 100%;
            max-width: 480px;
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .form-container:hover {
            transform: translateY(-3px);
            box-shadow: 0 16px 40px rgba(0,0,0,0.12);
        }

        .header {
            text-align: center;
            margin-bottom: 35px;
        }

        .header i {
            font-size: 48px;
            color: #2563eb;
            margin-bottom: 10px;
        }

        .header h1 {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
        }

        .form-group {
            margin-bottom: 22px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
            color: #334155;
            font-size: 15px;
        }

        input[type="text"] {
            width: 100%;
            padding: 14px;
            border-radius: 10px;
            border: 1px solid #d1d5db;
            font-size: 15px;
            transition: 0.3s;
        }

        input[type="text"]:focus {
            border-color: #2563eb;
            outline: none;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.2);
        }

        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 28px;
        }

        button {
            flex: 1;
            padding: 14px;
            border-radius: 10px;
            border: none;
            font-weight: 600;
            font-size: 15px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: 0.3s;
        }

        .btn-save {
            background-color: #28a745;
            color: white;
        }

        .btn-save:hover {
            background-color: #218838;
        }

        .btn-cancel {
            background-color: #6c757d;
            color: white;
        }

        .btn-cancel:hover {
            background-color: #5a6268;
        }

        .btn-save i, .btn-cancel i {
            margin-right: 8px;
            font-size: 16px;
        }

        /* Responsive */
        @media(max-width: 500px){
            .form-container {
                padding: 30px 20px;
            }
        }
    </style>
</head>

<body>
    <div class="form-container">
        <div class="header">
            <i class="fa-solid fa-user-tie"></i>
            <h1>${action == 'create' ? 'Nuevo Jefe de Proyecto' : 'Editar Jefe de Proyecto'}</h1>
        </div>

        <%@ include file="/WEB-INF/fragmentos/alertas.jspf" %>

        <form method="POST" action="${pageContext.request.contextPath}/jefes">
            <input type="hidden" name="action" value="${action}">
            <c:if test="${action == 'update'}">
                <input type="hidden" name="id" value="${jefe.id_jefe}">
            </c:if>

            <div class="form-group">
                <label for="nombres">Nombres del Jefe : </label>
                <input type="text" id="nombres" name="nombres" value="${fn:escapeXml(jefe.nombres)}" maxlength="150" required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-save">
                    <i class="fa-solid fa-floppy-disk"></i> Guardar
                </button>
                <button type="button" class="btn-cancel" onclick="location.href='${pageContext.request.contextPath}/jefes'">
                    <i class="fa-solid fa-arrow-left"></i> Cancelar
                </button>
            </div>
        </form>
    </div>
</body>
</html>
