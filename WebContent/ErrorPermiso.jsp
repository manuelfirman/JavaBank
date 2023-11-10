<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="entidad.Cliente" %>
<%@ page import="entidad.Cliente.TipoCliente" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ include file="Layout.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Error de permiso</title>
    <link rel="stylesheet" type="text/css" href="css/Layout.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <div class="container mt-5">
        <div class="alert alert-danger">
            <% if (cliente != null) { %>
                <h3 class="alert-heading">Ups! <%= cliente.getUsuario() %> no tienes los permisos para acceder a esta sección.</h3>
            <% } else { %>
                <h3 class="alert-heading">Necesitas loguearte para ingresar aquí.</h3>
            <% } %>
        </div>
        <a href="Inicio.jsp" class="btn btn-primary">Volver al inicio</a>
    </div>
</body>
</html>
