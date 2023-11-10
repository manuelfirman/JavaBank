<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.Cuenta"%>
<%@ page import="java.util.ArrayList"%>
<%@include file="Layout.jsp"%>
<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() != Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("Inicio.jsp");
	}
%>

<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.css">
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script>
    function limpiarCampos() {
        document.getElementById("busqueda").value = "";
        document.getElementById("saldoFiltro").value = "";
        document.querySelector('select[name="operadorSaldo"]').selectedIndex = 0;
        // Simular el clic en el botón "Buscar" después de limpiar los campos
        document.querySelector('[name="btnBusqueda"]').click();
    }
</script>
<title>Listado de Cuentas</title>
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
					<button type="submit" name="btnBusqueda" class="btn btn-primary">Buscar</button>
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
					<button type="submit" name="btnFiltrar" class="btn btn-success">Filtrar</button>
					<button type="button" onclick="limpiarCampos()"
						class="btn btn-secondary">Limpiar Campos</button>
				</div>
			</div>
		</form>
	</div>
	<%
		if (request.getAttribute("exitoModificacion") != null
				&& (boolean) request.getAttribute("exitoModificacion")) {
	%>
	<div class="alert alert-success" role="alert">Modificado con
		éxito.</div>
	<%
		}
	%>


	<%
		if (request.getAttribute("exitoEliminacion") != null
				&& (boolean) request.getAttribute("exitoEliminacion")) {
	%>
	<div class="alert alert-success">La cuenta se eliminó
		correctamente.</div>
	<%
		}
	%>

	<%
		if (request.getAttribute("exitoEliminacion") != null
				&& !(boolean) request.getAttribute("exitoEliminacion")) {
	%>
	<div class="alert alert-danger">No se pudo eliminar la cuenta.</div>
	<%
		}
	%>

	<%
		ArrayList<Cuenta> listaCuentas = (ArrayList<Cuenta>) request.getAttribute("listaCuentas");
		if (listaCuentas != null && !listaCuentas.isEmpty()) {
	%>
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
				<th>Borrar</th>
				<th>Modificar</th>
			</tr>
		</thead>
		<tbody>
			<%
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
				<td>
					<button class="btn btn-danger" data-toggle="tooltip"
						data-placement="top" title="Eliminar"
						onclick="confirmarEliminacion(<%=cuenta.getNumero()%>);">
						<i class="bi bi-trash"></i> Eliminar
					</button>
				</td>
				<td><a
					href="ServletCuenta?ModificarNumero=<%=cuenta.getNumero()%>"
					class="btn btn-primary" data-toggle="tooltip" data-placement="top"
					title="Modificar"> <i class="bi bi-pencil"></i> Modificar
				</a></td>
			</tr>

			<%
				}
			%>
		</tbody>
	</table>
	<%
		} else {
	%>
	<p>No se encontraron cuentas.</p>
	<%
		}
	%>

	<script>
        function confirmarEliminacion(idCuenta) {
            const confirmacion = confirm("¿Seguro que deseas eliminar esta cuenta?");
            if (confirmacion) {
                window.location.href = "ServletCuenta?EliminarNumeroCuenta=" + idCuenta;
            }
        }

        $(document).ready(function() {
            $('#cuentasTable').DataTable({
                "paging": true,
                "lengthMenu": [5, 10, 25, 50, 100],
                "pageLength": 10,
                "searching": true,
                "ordering": true,
                "searching": false
            });
        });     
        <%if (request.getAttribute("exitoEliminacion") != null) {
				boolean exitoEliminacion = (boolean) request.getAttribute("exitoEliminacion");
				if (exitoEliminacion) {%>        
        alert("La cuenta se eliminó correctamente.");
        <%} else {%>        
        alert("No se pudo eliminar la cuenta.");
        <%}
			}%>
			
    </script>
</body>
</html>
