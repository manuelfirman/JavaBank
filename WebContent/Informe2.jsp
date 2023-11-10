<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Seleccione Mes y Año</title>

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script>
	$(function() {
		$("#datepicker")
				.datepicker(
						{
							changeMonth : true,
							changeYear : true,
							showButtonPanel : true,
							dateFormat : 'mm/yy',
							onClose : function(dateText, inst) {
								var month = $(
										"#ui-datepicker-div .ui-datepicker-month :selected")
										.val();
								var year = $(
										"#ui-datepicker-div .ui-datepicker-year :selected")
										.val();
								var selectedDate = (parseInt(month) + 1) + '/'
										+ year;
								$(this).val(selectedDate);
							}
						});
	});
</script>
</head>
<body>
	<h1>Seleccione Mes y Año</h1>
	<form action="ServletCliente" method="get">
		<input type="text" id="datepicker" name="fecha"> <input
			type="submit" value="Buscar">
	</form>

	<h2>Tabla de Ejemplo</h2>
	<table border="1">
		<tr>
			<th>Tipo de Movimiento</th>
			<th>Importe Total</th>
		</tr>
		<tr>
			<td>Alta Cuenta</td>
			<td>100.00</td>
		</tr>
		<tr>
			<td>Pagar Prestamo</td>
			<td>75.50</td>
		</tr>
		<tr>
			<td>Adquirir Préstamo</td>
			<td>120.25</td>
		</tr>
	</table>
</body>
</html>

