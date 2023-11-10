<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="Layout.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Datos del Cliente</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	
	<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() == Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("ErrorPermiso.jsp");
	}
%>
</head>
<body>
	<div class="container">
		<h1 class="mt-4">Datos del Cliente</h1>
		<%
			HttpSession ses2 = request.getSession(false);
			Cliente cliente2 = (Cliente) ses.getAttribute("cliente");
			if (cliente2 != null) {
		%>
		<div class="row">
			<div class="col-md-6">
				<table class="table table-striped">
					<tr>
						<th scope="row">Nombre:</th>
						<td><%=cliente2.getNombre()%></td>
					</tr>
					<tr>
						<th scope="row">Apellido:</th>
						<td><%=cliente2.getApellido()%></td>
					</tr>
					<tr>
						<th scope="row">DNI:</th>
						<td><%=cliente2.getDni()%></td>
					</tr>
					<tr>
						<th scope="row">CUIL:</th>
						<td><%=cliente2.getCuil()%></td>
					</tr>
					<tr>
						<th scope="row">Sexo:</th>
						<td><%=(cliente2.getSexo() == entidad.Cliente.Sexo.VARON) ? "Varón"
						: (cliente.getSexo() == entidad.Cliente.Sexo.MUJER) ? "Mujer" : "Indefinido"%></td>
					</tr>
				</table>
			</div>
			<div class="col-md-6">
				<table class="table table-striped">
					<tr>
						<th scope="row">Nacionalidad:</th>
						<td><%=cliente2.getNacionalidad()%></td>
					</tr>
					<tr>
						<th scope="row">Fecha de Nacimiento:</th>
						<td><%=cliente2.getFechaNacimiento()%></td>
					</tr>
					<tr>
						<th scope="row">Dirección:</th>
						<td><%=cliente2.getDireccion()%></td>
					</tr>
					<tr>
						<th scope="row">Correo:</th>
						<td><%=cliente2.getCorreo()%></td>
					</tr>
					<tr>
						<th scope="row">Localidad:</th>
						<td><%=cliente2.getLocalidad().getNombre()%></td>
					</tr>
					<tr>
						<th scope="row">Provincia:</th>
						<td><%=cliente2.getProvincia().getNombre()%></td>
					</tr>
				</table>
			</div>
		</div>
		<%
			} else {
		%>
		<p>No se encontró un cliente en la sesión.</p>
		<%
			}
		%>
		<a class="btn btn-primary" href="Inicio.jsp">Volver al Inicio</a>
	</div>
</body>
</html>
