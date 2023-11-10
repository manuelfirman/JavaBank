<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.Cuenta"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="servlets.ServletCuenta"%>
<%@include file="Layout.jsp"%>
<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() == Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("ErrorPermiso.jsp");
	}
%>
<%
	// Obtener la cuenta de origen de la sesión
	Cuenta cuentaOrigen = (Cuenta) request.getSession().getAttribute("cuentaOrigen");

	// Verificar si la cuenta de origen no es nula
	if (cuentaOrigen == null) {
		// Redirigir a una página de error o realizar alguna acción apropiada
		response.sendRedirect("error.jsp");
	}

	// Obtener un mensaje de error si existe en la solicitud
	String errorMensaje = (String) request.getAttribute("errorMensaje");
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<title>Transferencia</title>
</head>
<body>
	<div class="container">
		<h1 class="mt-4">Realizar Transferencia</h1>

		<%-- Mostrar mensaje de error si existe --%>
		<div class="alert alert-danger mt-3">
			<%
				if (errorMensaje != null) {
			%>
			<%=errorMensaje%>
			<%
				}
			%>
		</div>

		<form action="ServletCuenta" method="post" class="mt-4">
			<div class="form-group">
				<label for="importe">Importe:</label> <input type="text"
					name="importe" id="importe" class="form-control" required>
			</div>
			<div class="form-group">
				<label for="cbu">CBU (22 números):</label> <input type="text"
					name="cbu" id="cbu" class="form-control" pattern="[0-9]{22}"
					required>
			</div>
			<div class="form-group">
				<label for="origen">Cuenta de Origen:</label> <input type="text"
					class="form-control" value="<%=cuentaOrigen.getNumero()%>"
					readonly> <input type="hidden" name="cuentaOrigen"
					value="<%=cuentaOrigen.getNumero()%>">
			</div>
			<button type="submit" name="btnTransferir" class="btn btn-primary">Realizar
				Transferencia</button>
		</form>
	</div>

</body>
</html>
