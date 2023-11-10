<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.TipoCuenta"%>
<%@ page import="java.util.ArrayList"%>
<%@include file="Layout.jsp"%>
<%@ page import="entidad.Cliente"%>
<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() != Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("ErrorPermiso.jsp");
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css">
<title>Formulario para Cargar una Cuenta</title>
<style>
/* Estilo personalizado para centrar el contenido vertical y horizontalmente */
.centered-container {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh; /* Establece la altura al 100% del viewport */
}
</style>
</head>
<body>
	<div class="container">
		<div>
			<h1>Formulario para Cargar una Cuenta</h1>

			<!-- Verificar si hay una advertencia -->
			<%
				String advertencia = (String) request.getAttribute("advertencia");
				if (advertencia != null) {
			%>
			<div
				class="alert alert-<%=advertencia.equals("exito") ? "success" : "danger"%>">
				<strong><%=advertencia.equals("exito") ? "Éxito" : "Error"%>!</strong>
				<%=(advertencia.equals("exito") ? "La cuenta se ha agregado exitosamente."
						: "El cliente ya posee 3 cuentas.")%>
			</div>
			<%
				}
			%>

			<form action="ServletCuenta" method="post">
				<div class="form-group">
					<label for="idCliente">ID del Cliente:</label> <input type="text"
						name="idCliente" id="idCliente" class="form-control"
						value="<%=request.getAttribute("idCliente")%>" readonly>
				</div>

				<div class="form-group">
					<label for="idTipoCuenta">Tipo de Cuenta:</label> <select
						name="idTipoCuenta" id="idTipoCuenta" class="form-control">
						<%
							ArrayList<TipoCuenta> tiposCuenta = (ArrayList<TipoCuenta>) request.getAttribute("tiposCuenta");
							if (tiposCuenta != null) {
								for (TipoCuenta tipoCuenta : tiposCuenta) {
						%>
						<option value="<%=tipoCuenta.getIdTipoCuenta()%>"><%=tipoCuenta.getDescripcion()%></option>
						<%
							}
							}
						%>
					</select>
				</div>

				<button type="submit" name="btnAgregar" class="btn btn-primary">Agregar
					Cuenta</button>
			</form>
		</div>
	</div>
</body>
</html>
