<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.Cliente"%>
<%@ page import="entidad.Telefono"%>
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
<meta charset="UTF-8">
<title>Listado de Clientes</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script type="text/javascript" charset="utf8"
	src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.css" />
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#clientesTable').DataTable();
	});
	<script type="text/javascript">
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#clientesTable').DataTable({
			"lengthMenu" : [ 5, 10, 25, 50, 100 ],
			"pageLength" : 5,
			"ordering" : true,
			"info" : true,
			"paging" : true,
			"searching" : false
		});
	});

	function limpiarCampos() {
		document.getElementById("busqueda").value = "";
		document.getElementById("edad").value = "";
		document.querySelector('select[name="operadorEdad"]').selectedIndex = 0;
		document.querySelector('[name="btnBusqueda"]').click();
	};
</script>
</head>
<body>
	<h2>Listado de Clientes</h2>

	<div class="container mt-4">
		<form method="get" action="ServletCliente" class="row g-3">
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
				<label for="edad" class="form-label">Edad:</label>
				<div class="input-group">
					<select name="operadorEdad" class="form-select">
						<option value="mayor"
							<%=(request.getParameter("operadorEdad") != null
					&& request.getParameter("operadorEdad").equals("mayor")) ? "selected" : ""%>>Mayor
							que:</option>
						<option value="menor"
							<%=(request.getParameter("operadorEdad") != null
					&& request.getParameter("operadorEdad").equals("menor")) ? "selected" : ""%>>Menor
							que:</option>
						<option value="igual"
							<%=(request.getParameter("operadorEdad") != null
					&& request.getParameter("operadorEdad").equals("igual")) ? "selected" : ""%>>Igual
							a:</option>
					</select><input type="number" id="edad" name="edad"
						value="<%=(request.getParameter("edad") != null && !request.getParameter("edad").isEmpty())
					? request.getParameter("edad")
					: ""%>"
						class="form-control"
						oninput="this.value = this.value.replace(/[^0-9]/g, '');" step="1">


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
		if (request.getAttribute("exitoEliminacionCliente") != null
				&& (boolean) request.getAttribute("exitoEliminacionCliente")) {
	%>
	<div class="alert alert-success">Cliente eliminado correctamente.</div>
	<%
		}
	%>


	<%
		ArrayList<Cliente> lista = (ArrayList<Cliente>) request.getAttribute("lista");
		ArrayList<Telefono> telefonosCliente = (ArrayList<Telefono>) request.getAttribute("listaTelefonos");

		if (lista != null && !lista.isEmpty()) {
	%>
	<table border="1" id="clientesTable" class="display">
		<thead>
			<tr>
				<th>ID</th>
				<th>Usuario</th>
				<th>Contraseña</th>
				<th>Activo</th>
				<th>Fecha de Creación</th>
				<th>Tipo de Cliente</th>
				<th>DNI</th>
				<th>CUIL</th>
				<th>Nombre</th>
				<th>Apellido</th>
				<th>Sexo</th>
				<th>Nacionalidad</th>
				<th>Fecha de Nacimiento</th>
				<th>Dirección</th>
				<th>Correo</th>
				<th>Localidad</th>
				<th>Provincia</th>
				<th>Teléfonos</th>
				<th>Acciones</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (Cliente cliente2 : lista) {
			%>
			<tr>
				<td><%=cliente2.getIdCliente()%></td>
				<td><%=cliente2.getUsuario()%></td>
				<td><%=cliente2.getContrasena()%></td>
				<td><%=(cliente2.getActivo() == 1) ? "Activo" : "Inactivo"%></td>
				<td><%=cliente2.getFechaCreacion()%></td>
				<td><%=(cliente2.getTipoCliente() == Cliente.TipoCliente.CLIENTE) ? "Cliente" : "Administrador"%></td>
				<td><%=cliente2.getDni()%></td>
				<td><%=cliente2.getCuil()%></td>
				<td><%=cliente2.getNombre()%></td>
				<td><%=cliente2.getApellido()%></td>
				<td><%=cliente2.getSexo()%></td>
				<td><%=cliente2.getNacionalidad()%></td>
				<td><%=cliente2.getFechaNacimiento()%></td>
				<td><%=cliente2.getDireccion()%></td>
				<td><%=cliente2.getCorreo()%></td>
				<td><%=cliente2.getLocalidad().getNombre()%></td>
				<td><%=cliente2.getProvincia().getNombre()%></td>
				<td><select name="telefonosCliente">
						<%
							boolean clienteTieneNumeros = false; // Variable de bandera para realizar un seguimiento
									if (telefonosCliente != null && !telefonosCliente.isEmpty()) {
										for (Telefono telefono : telefonosCliente) {
											if (telefono.getCliente().getIdCliente() == cliente2.getIdCliente()) {
						%>
						<option value="<%=telefono.getNumero()%>"><%=telefono.getNumero()%></option>
						<%
							clienteTieneNumeros = true; // El cliente tiene números
											}
										}
									}

									if (!clienteTieneNumeros) {
						%>
						<option value="No posee números">No posee números</option>
						<%
							}
						%>
				</select></td>

				<td><a
					href="ServletCliente?ModifId=<%=cliente2.getIdCliente()%>"
					class="btn btn-success" data-toggle="tooltip" data-placement="top"
					title="Modificar"> <i class="bi bi-pencil"></i>
				</a> <a href="ServletCuenta?AgregarId=<%=cliente2.getIdCliente()%>"
					class="btn btn-primary" data-toggle="tooltip" data-placement="top"
					title="Agregar Cuenta"> <i class="bi bi-plus"></i>
				</a> <a href="#" class="btn btn-danger" data-toggle="tooltip"
					data-placement="top" title="Eliminar"
					onclick="confirmarEliminacionCliente(<%=cliente2.getIdCliente()%>);">
						<i class="bi bi-trash"></i>
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
	<p>No hay clientes para mostrar.</p>
	<%
		}
	%>
	<script>
		function confirmarEliminacionCliente(idCliente) {
			const confirmacion = confirm("¿Seguro que deseas eliminar a este cliente?");
			if (confirmacion) {
				window.location.href = "ServletCliente?ElimId=" + idCliente;
			} else {
				// No hacer nada o redirigir a alguna otra página, si es necesario
			}
		}
	</script>
</body>
</html>

