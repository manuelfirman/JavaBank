<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.Cuenta"%>
<%@ page import="entidad.TipoCuenta"%>
<%@ page import="java.util.ArrayList"%>
<%@include file="Layout.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() != Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("ErrorPermiso.jsp");
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Formulario para Modificar una Cuenta</title>
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
</head>
<body>
	<%
		Cuenta cuenta = (Cuenta) request.getAttribute("cuenta");
		if (cuenta != null) {
	%>
	<div class="container mt-5">
		<h1 class="mb-4">Formulario para Modificar una Cuenta</h1>
		<form action="ServletCuenta" method="post">
			<div class="mb-3">
				<label for="idCliente" class="form-label">ID del Cliente:</label> <input
					type="text" name="idCliente" id="idCliente"
					value="<%=cuenta.getCliente().getIdCliente()%>"
					class="form-control" readonly>
			</div>

			<div class="mb-3">
				<label for="numero" class="form-label">Número de Cuenta:</label> <input
					type="text" name="numero" id="numero"
					value="<%=cuenta.getNumero()%>" class="form-control" readonly>
			</div>

			<div class="mb-3">
				<label for="CBU" class="form-label">CBU:</label> <input type="text"
					name="CBU" id="CBU" value="<%=cuenta.getCBU()%>"
					class="form-control" readonly>
			</div>

			<div class="mb-3">
				<label for="idTipoCuenta" class="form-label">Tipo de Cuenta:</label>
				<select name="idTipoCuenta" id="idTipoCuenta" class="form-select">
					<%
						ArrayList<TipoCuenta> tiposCuenta = (ArrayList<TipoCuenta>) request.getAttribute("tiposCuenta");
							int idTipoCuentaSeleccionado = cuenta.getTipoCuenta().getIdTipoCuenta();

							for (TipoCuenta tipoCuenta : tiposCuenta) {
								// Comprobar si este tipo de cuenta es el seleccionado
								String selected = (tipoCuenta.getIdTipoCuenta() == idTipoCuentaSeleccionado) ? "selected" : "";
					%>
					<option value="<%=tipoCuenta.getIdTipoCuenta()%>" <%=selected%>><%=tipoCuenta.getDescripcion()%></option>
					<%
						}
					%>
				</select>
			</div>

			<%
				// Obtener el valor del saldo de la cuenta y formatearlo como una cadena con punto como separador decimal
					double saldo = cuenta.getSaldo();
					String saldoFormateado = String.format("%.2f", saldo).replace(',', '.');
			%>

			<div class="mb-3">
				<label for="saldo" class="form-label">Saldo:</label> <input
					type="text" name="saldo" value="<%=saldoFormateado%>" id="saldo"
					maxlength="11" class="form-control" required>
			</div>

			<script>
				document
						.addEventListener(
								"DOMContentLoaded",
								function() {
									var saldoInput = document
											.getElementById("saldo");

									saldoInput
											.addEventListener(
													"input",
													function() {
														var value = saldoInput.value;
														if (!isValidNumber(value)) {
															saldoInput
																	.setCustomValidity("Debe ser un número válido.");
														} else {
															saldoInput
																	.setCustomValidity("");
														}
													});

									function isValidNumber(value) {
										var regex = /^\d+(\.\d{0,2})?$/;
										return regex.test(value);
									}
								});
			</script>



			<button type="submit" class="btn btn-primary" name="btnModificar">Modificar
				Cuenta</button>
		</form>
	</div>
	<%
		} else {
	%>
	<div class="container mt-5">
		<p class="alert alert-danger">La cuenta no se pudo cargar
			correctamente.</p>
	</div>
	<%
		}
	%>
</body>
</html>
