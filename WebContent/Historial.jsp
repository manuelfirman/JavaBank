<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="entidad.Movimiento"%>
<%@include file="Layout.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.css">
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>

<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() == Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("ErrorPermiso.jsp");
	}
%>
<script>
	function limpiarCampos() {
		document.getElementById("busqueda").value = "";
		document.getElementById("saldoFiltro").value = "";
		document.querySelector('select[name="operadorSaldo"]').selectedIndex = 0;
		// Simular el clic en el botón "Buscar" después de limpiar los campos
		document.querySelector('[name="btnBusquedaHistorial"]').click();
	}
	$(document).ready(function() {
	    $('#movimientosTable').DataTable({
	        "paging": true,
	        "lengthMenu": [5, 10, 25, 50, 100],
	        "pageLength": 10,
	        "searching": false, // Configuración para desactivar la búsqueda
	        "ordering": true,
	        "language": {
	            "emptyTable": "No hay movimientos disponibles en la tabla"
	        }
	    });
	});
</script>
<title>Historial de Movimientos</title>
</head>
<body>
	<h1>Historial de Movimientos</h1>

	<div class="container mt-4" style="margin-bottom: 10px;">
		<form method="get" action="ServletCuenta" class="row g-3">
			<div class="col-md-6">
				<label for="busqueda" class="form-label">Buscar:</label>
				<div class="input-group">
					<input type="text" id="busqueda" name="busqueda"
						class="form-control"
						value="<%=(request.getParameter("busqueda") != null) ? request.getParameter("busqueda") : ""%>">
					<button type="submit" name="btnBusquedaHistorial"
						class="btn btn-primary">Buscar</button>
				</div>
			</div>
			<div class="col-md-6">
				<label for="saldoFiltro" class="form-label">Importe:</label>
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
					<button type="submit" name="btnFiltrarHistorial"
						class="btn btn-success">Filtrar</button>
					<button type="button" onclick="limpiarCampos()"
						class="btn btn-secondary">Limpiar Campos</button>
				</div>
			</div>
		</form>
	</div>

	<%
		// Recupera la lista de movimientos desde el atributo "movimientos"
		ArrayList<Movimiento> movimientos = (ArrayList<Movimiento>) request.getAttribute("movimientos");
	%>

	<table id="movimientosTable" class="display" border="1">
		<thead>
			<tr>
				<th>ID Movimiento</th>
				<th>Detalle</th>
				<th>Importe</th>
				<th>Tipo de Movimiento</th>
				<th>Fecha</th>
			</tr>
		</thead>
		<tbody>
			<%
				if (movimientos != null && !movimientos.isEmpty()) {
					for (Movimiento movimiento : movimientos) {
			%>
			<tr>
				<td><%=movimiento.getIdMovimiento()%></td>
				<td><%=movimiento.getDetalle()%></td>
				<td><%=movimiento.getImporte()%></td>
				<td><%=movimiento.getIdTipoMovimiento().getDescripcion()%></td>
				<td><%=movimiento.getFecha()%></td>
			</tr>
			<%
				}
				}
			%>
		</tbody>
	</table>
</body>
</html>
