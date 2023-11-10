<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.Cuenta"%>
<%@ page import="java.util.ArrayList"%>
<%@include file="Layout.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() == Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("ErrorPermiso.jsp");
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Listado de Cuentas del Cliente</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Incluye jQuery 3.6.0 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- Incluye DataTables 1.13.6 -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.css">
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>
<script>
	function limpiarCampos() {
		document.getElementById("busqueda").value = "";
		document.getElementById("saldoFiltro").value = "";
		document.querySelector('select[name="operadorSaldo"]').selectedIndex = 0;
		// Simular el clic en el botón "Buscar" después de limpiar los campos
		document.querySelector('[name="btnBusquedaCl"]').click();
	}
	$(document).ready(function() {
		$('#cuentasTable').DataTable({
			"paging" : true,
			"lengthMenu" : [ 5, 10, 25, 50, 100 ],
			"pageLength" : 10,
			"searching" : false,
			"ordering" : true,
			"language" : {
				"emptyTable" : "El cliente no posee cuentas activas"
			}
		});
	});
</script>
</head>
<body>

	<h1>Listado de Cuentas</h1>
	<div class="container mt-4" style="margin-bottom: 10px;">
		<form method="get" action="ServletCuenta" class="row g-3">
			<div class="col-md-6">
				<label for="busqueda" class="form-label">Buscar:</label>
				<div class="input-group">
					<input type="text" id="busqueda" name="busqueda"
						class="form-control"
						value="<%=(request.getParameter("busqueda") != null) ? request.getParameter("busqueda") : ""%>">
					<button type="submit" name="btnBusquedaCl" class="btn btn-primary">Buscar</button>
				</div>
			</div>
			<div class="col-md-6">
				<label for="saldoFiltro" class="form-label">Saldo:</label>
				<div class="input-group">
					<select name="operadorSaldo" class="form-select">
						<option value="mayor"
							<%=(request.getParameter("operadorSaldo") != null
					&& request.getParameter("operadorSaldo").equals("mayor")) ? "selected" : ""%>>Mayor
							que:</option>
						<option value="menor"
							<%=(request.getParameter("operadorSaldo") != null
					&& request.getParameter("operadorSaldo").equals("menor")) ? "selected" : ""%>>Menor
							que:</option>
						<option value="igual"
							<%=(request.getParameter("operadorSaldo") != null
					&& request.getParameter("operadorSaldo").equals("igual")) ? "selected" : ""%>>Igual
							a:</option>
					</select> <input type="text" id="saldoFiltro" name="saldoFiltro"
						class="form-control"
						value="<%=(request.getParameter("saldoFiltro") != null) ? request.getParameter("saldoFiltro") : ""%>"
						oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
					<button type="submit" name="btnFiltrarCl" class="btn btn-success">Filtrar</button>
					<button type="button" onclick="limpiarCampos()"
						class="btn btn-secondary">Limpiar Campos</button>
				</div>
			</div>
		</form>
	</div>

	<table border="1" id="cuentasTable" class="display">
		<thead>
			<tr>
				<th>ID Cliente</th>
				<th>Número de Cuenta</th>
				<th>CBU</th>
				<th>Saldo</th>
				<th>Fecha</th>
				<th>Activo</th>
				<th>Tipo de Cuenta</th>
				<th>Acciones</th>
			</tr>
		</thead>

		<%
			ArrayList<Cuenta> listaCuentas = (ArrayList<Cuenta>) request.getAttribute("listaCuentas");

			if (listaCuentas != null && !listaCuentas.isEmpty()) {
				for (Cuenta cuenta : listaCuentas) {
		%>
		<tr>
			<td><%=cuenta.getCliente().getIdCliente()%></td>
			<td><%=cuenta.getNumero()%></td>
			<td><%=cuenta.getCBU()%></td>
			<td><%=cuenta.getSaldo()%></td>
			<td><%=cuenta.getFecha()%></td>
			<td><%=cuenta.getActivo()%></td>
			<td><%=cuenta.getTipoCuenta().getDescripcion()%></td>
			<td><a href="ServletCuenta?historial=<%=cuenta.getNumero()%>"
				class="btn btn-primary" title="Ver historial"> <i
					class="bi bi-clock"></i> Historial
			</a> <a href="ServletCuenta?transferencia=<%=cuenta.getNumero()%>"
				class="btn btn-warning" title="Transferir fondos"> <i
					class="bi bi-arrow-repeat"></i> Transferir
			</a> <a href="ServletPrestamo?pedirPrestamo=<%=cuenta.getNumero()%>"
				class="btn btn-success" title="Pedir préstamo"> <i
					class="bi bi-cash"></i> Pedir Préstamo
			</a></td>


		</tr>
		<%
			}
			}
		%>
	</table>
</body>
</html>
