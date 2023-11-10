<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.Cliente"%>
<%@ page import="entidad.Cliente.TipoCliente"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Group3 Bank</title>
<link rel="stylesheet" type="text/css" href="css/Layout.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<%
		// Obtén la sesión actual
		HttpSession ses = request.getSession(false); // El argumento "false" evita la creación de una nueva sesión si no existe.
		Cliente cliente = (Cliente) ses.getAttribute("cliente");
		if (cliente != null) {
	%>
	<div id="titulo">
		<h1 class="h1-layout">
			<%
				if (cliente.getTipoCliente() == TipoCliente.ADMIN) {
			%>
			Área Administración
			<%
				} else {
			%>
			Área clientes
			<%
				}
			%>
		</h1>
	</div>
	<div id="menu">
		<nav class="navbar navbar-expand-lg navbar-light bg-light"> <a
			class="navbar-brand" href="Inicio.jsp">Group3 Bank</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav mr-auto">
				<!-- Mover elementos a la izquierda -->
				<%
					if (cliente.getTipoCliente() == TipoCliente.CLIENTE) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="ServletCuenta?listaPorId=1">Listar Cuentas</a></li>
				<li class="nav-item"><a class="nav-link"
					href="ServletPrestamo?listaPagar=1">Pagar Préstamos</a></li>
				<li class="nav-item"><a class="nav-link"
					href="DetalleCliente.jsp">Datos Personales</a></li>
				<%
					} else if (cliente.getTipoCliente() == TipoCliente.ADMIN) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="ServletCliente?lista=1">Listar Clientes</a></li>
				<li class="nav-item"><a class="nav-link"
					href="ServletCliente?alta=1">Agregar Cliente</a></li>
				<li class="nav-item"><a class="nav-link"
					href="ServletPrestamo?lista=1">Listar Préstamos Admin</a></li>
				<li class="nav-item"><a class="nav-link"
					href="ServletCuenta?lista=1">Listar Cuentas</a></li>
				<li class="nav-item"><a class="nav-link" href="Informe1.jsp">Informe
						1</a></li>
				<li class="nav-item"><a class="nav-link" href="Informe2.jsp">Informe
						2</a></li>
				<%
					}
				%>
			</ul>
			<ul style="" class="navbar-nav ml-auto">
				<!-- Mover elementos a la derecha -->
				<li class="nav-item"><label style="font-weight: bold;" class="nav-link"><%=cliente.getUsuario()%></label>
				</li>
				<li class="nav-item">
					<form id="logoutForm" action="ServletCliente" method="POST">
						<button type="submit" class="btn btn-link" name="btnCerrarSesion">Cerrar
							Sesión</button>
					</form>
				</li>
			</ul>

		</div>
		</nav>
	</div>
	<%
		} else {
	%>
	<div id="titulo">
		<h1 class="h1-layout">Inicio</h1>
	</div>
	<%
		}
	%>
	<div id="contenido">
		<!-- Espacio para mostrar el contenido particular de cada JSP -->
	</div>
</body>
</html>