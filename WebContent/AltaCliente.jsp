<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entidad.Cliente"%>
<%@ page import="entidad.Provincia"%>
<%@ page import="entidad.Localidad"%>
<%@ page import="entidad.Telefono"%>
<%@ page import="java.util.ArrayList"%>
<%@include file="Layout.jsp"%>
<%
	Cliente clienteUsuario = (Cliente) session.getAttribute("cliente");

	if (clienteUsuario == null || clienteUsuario.getTipoCliente() != Cliente.TipoCliente.ADMIN) {
		response.sendRedirect("ErrorPermiso.jsp");
	}
%>

<!DOCTYPE html>
<html>
<head>
<!-- Agregar enlaces a jQuery, Bootstrap y Bootstrap Datepicker -->
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
<style>
.scrollable-list {
	max-height: 100px;
	overflow-y: auto;
}

.telefono-item {
	margin-bottom: 10px;
}
</style>


<%
	HttpSession session2 = request.getSession();
	Cliente clienteModificar = (Cliente) session2.getAttribute("clienteDatos");

	ArrayList<Provincia> provincias = (ArrayList<Provincia>) request.getAttribute("provincias");
	ArrayList<Localidad> localidades = (ArrayList<Localidad>) request.getAttribute("localidades");
	ArrayList<Telefono> listaTelefonos = (ArrayList<Telefono>) request.getAttribute("telefonos");
%>
<!-- Arreglos JavaScript de provincias y localidades -->
<script>
        var provinciasArray = [
            <%if (provincias != null) {
				for (Provincia prov : provincias) {%>
            { id: "<%=prov.getId()%>", nombre: "<%=prov.getNombre()%>" },
            <%}
			}%>
        ];

        var localidadesArray = [
            <%if (localidades != null) {
				for (Localidad loc : localidades) {%>
            { id: "<%=loc.getId()%>", provinciaId: "<%=loc.getIdProvincia()%>", nombre: "<%=loc.getNombre()%>" },
            <%}
			}%>
        ];
        //setea previo la fecha
        <%if (clienteModificar != null) {%>
    var fechaNacimientoCliente = "<%=clienteModificar.getFechaNacimiento()%>";

    if (fechaNacimientoCliente.trim() !== "") {
        // Convertir la fecha de MySQL a un formato válido de JavaScript (yyyy-mm-dd a mm/dd/yyyy)
        var fechaJavascript = fechaNacimientoCliente.replace(/(\d{4})-(\d{2})-(\d{2})/, "$2/$3/$1");
        $("#fechaNacimiento").val(fechaJavascript);
    }
<%}%>
$(function() {
    $(".datepicker").datepicker({
        format: 'yyyy-mm-dd'
    });

    // Manejar el evento de cambio en el desplegable de provincias
    $("#provincia").change(function() {
        console.log("Cambio en el desplegable de provincias");
        var provinciaId = $(this).val();
        if (provinciaId !== "") {
            // Filtrar las localidades según la provincia seleccionada
            var localidadesFiltradas = localidadesArray.filter(function(loc) {
                return loc.provinciaId === provinciaId;
            });

            // Rellenar el desplegable de localidades con los datos filtrados
            var localidadDropdown = $("#localidad");
            localidadDropdown.empty();
            localidadDropdown.append($("<option>").attr("value", "").text("Seleccionar Localidad"));
            $.each(localidadesFiltradas, function(i, loc) {
                localidadDropdown.append($("<option>").attr("value", loc.id).text(loc.nombre));
            });
        } else {
            // Si no se selecciona una provincia, vaciar el desplegable de localidades
            $("#localidad").html("<option value=''>Seleccionar Localidad</option>");
        }
    });

    // Agrega el código para establecer la provincia y localidad previas a la modificación
    
    <%if (clienteModificar != null) {%>         
    var provinciaId = "<%=clienteModificar.getProvincia() != null ? clienteModificar.getProvincia().getId() : ""%>";
    var localidadId = "<%=clienteModificar.getLocalidad() != null ? clienteModificar.getLocalidad().getId() : ""%>";

    // Selecciona la provincia y llena el desplegable de localidades (si hay una provincia seleccionada)
    if (provinciaId !== "") {
        $("#provincia").val(provinciaId).change();

        if (localidadId !== "") {
            $("#localidad").val(localidadId);
        }
    }
    <%}%>
});
function agregarTelefono() {
    var nuevoTelefono = $("#nuevoTelefono").val();

    // Verificar que el número de teléfono contenga solo dígitos y, si está presente, un solo signo "+" al comienzo
    var telefonoValido = /^[+]?\d+$/;
    if (!telefonoValido.test(nuevoTelefono)) {
        alert("El número de teléfono debe contener solo dígitos y, si está presente, un signo + al comienzo.");
        return;
    }

    var telefonosList = $("#telefonosList");
    var listItem = $("<li>");
    var input = $("<input>");
    input.attr("type", "text");
    input.attr("name", "telefonos");
    input.addClass("form-control"); // Agregar la clase Bootstrap para estilizar el input
    input.val(nuevoTelefono);
    var deleteButton = $("<button>");
    deleteButton.addClass("btn btn-danger"); // Agregar clases de Bootstrap al botón
    var deleteIcon = $("<i>");
    deleteIcon.addClass("bi bi-trash"); // Agregar el ícono de eliminación de Bootstrap
    deleteButton.append(deleteIcon);
    deleteButton.on("click", function() {
        eliminarTelefono(this);
    });
    listItem.append(input);
    listItem.append(deleteButton);
    telefonosList.append(listItem);
    $("#nuevoTelefono").val("");

    // Mostrar un mensaje de éxito
    alert("Teléfono ingresado con éxito.");
}



function eliminarTelefono(button) {
    var telefono = $(button).prev().val(); // Obtener el valor del teléfono que se eliminará
    $(button).parent().remove();
    alert("Teléfono eliminado: " + telefono); // Mostrar una alerta pop-up con el mensaje
}
function validarCampos() {
    var elementos = document.getElementsByTagName("input");
    var contrasena = null;
    var confirmacionContrasena = null;

    for (var i = 0; i < elementos.length; i++) {
        var elemento = elementos[i];
        var valor = elemento.value.trim();

        if (elemento.name === "nombre" || elemento.name === "apellido" || elemento.name === "nacionalidad") {
            if (valor.length < 3) {
                alert("El campo '" + elemento.placeholder + "' debe tener al menos 3 caracteres.");
                elemento.focus();
                return false;
            }
            if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(valor)) {
                alert("El campo '" + elemento.placeholder + "' solo debe contener letras y espacios.");
                elemento.focus();
                return false;
            }
        } else if (elemento.name === "dni" || elemento.name === "cuil") {
            if (!/^\d+$/.test(valor) || valor.length < 6) {
                alert("El campo '" + elemento.placeholder + "' debe contener solo números y tener al menos 6 dígitos.");
                elemento.focus();
                return false;
            }
        } else if (elemento.name === "usuario" || elemento.name === "contrasena") {
            if (valor.length < 3) {
                alert("El campo '" + elemento.placeholder + "' debe tener al menos 3 caracteres.");
                elemento.focus();
                return false;
            }
            if (elemento.name === "contrasena") {
                contrasena = valor;
            }
        } else if (elemento.name === "direccion") {
            if (valor.length < 5) {
                alert("El campo '" + elemento.placeholder + "' debe tener al menos 5 caracteres.");
                elemento.focus();
                return false;
            }
            if (!/^[A-Za-z0-9\s]+$/.test(valor)) {
                alert("El campo '" + elemento.placeholder + "' solo debe contener letras, números y espacios.");
                elemento.focus();
                return false;
            }
        } else if (elemento.name === "repetirContrasena") {
            confirmacionContrasena = valor;
        }
    }

    if (contrasena !== confirmacionContrasena) {
        alert("La contraseña y la confirmación de contraseña no coinciden.");
        return false;
    }

    return true;
}

//Verificar la presencia del parámetro "exito" en la URL y mostrar un mensaje de éxito si está presente
<%if (request.getParameter("exito") != null && request.getParameter("exito").equals("true")) {%>
alert("Cliente agregado exitosamente.");
// Restablecer los valores de los campos
document.getElementById("usuario").value = "";
document.getElementById("contrasena").value = "";
document.getElementById("repetirContraseña").value = "";
document.getElementById("dni").value = "";
document.getElementById("cuil").value = "";
document.getElementById("nombre").value = "";
document.getElementById("apellido").value = "";
document.getElementById("sexo").value = "0"; // Ajusta esto según tu estructura
document.getElementById("nacionalidad").value = "";
document.getElementById("fechaNacimiento").value = "";
document.getElementById("direccion").value = "";
document.getElementById("provincia").value = "";
document.getElementById("localidad").value = "";
document.getElementById("correo").value = "";
document.getElementById("telefonosList").innerHTML = ""; // Limpia la lista de teléfonos
document.getElementById("nuevoTelefono").value = ""; // Limpia el campo de nuevo teléfono
<%}%>

<%if (request.getParameter("exito") != null && request.getParameter("exito").equals("true")) {%>
$(function() {
    // Muestra el mensaje de éxito
    $("#mensajeExito").show();
});
<%}%>

<%if (request.getAttribute("advertencia") != null) {%>
$(function() {
    // Mostrar el mensaje de advertencia utilizando JavaScript y Bootstrap
    $("#mensajeAdvertencia").show();
});
<%}%>

</script>
<meta charset="UTF-8">
<title>Detalles del Cliente</title>
</head>
<body>
	<div id="mensajeAdvertencia" class="alert alert-danger"
		style="display: none;">
		<%
			if (request.getAttribute("advertencia") != null) {
				String advertencia = (String) request.getAttribute("advertencia");
				if (advertencia != null) {
		%>
		<%=advertencia%>
		<%
			}
			}
		%>
	</div>
	<div id="mensajeExito" style="display: none;"
		class="alert alert-success">Cliente agregado con éxito.</div>
	<h2>Agregar Cliente</h2>
	<%
		Provincia provincia = clienteModificar != null ? clienteModificar.getProvincia() : null;
	%>
	<form action="ServletCliente" method="post"
		onsubmit="return validarCampos();">
		<input type="hidden" name="idCliente"
			value="<%=(clienteModificar != null) ? clienteModificar.getIdCliente() : 0%>">
		<div class="container">
			<div class="row">
				<div class="col-md-3">
					<!-- Columna 1: Datos de Usuario -->
					<h4>Datos de Usuario</h4>
					<label for="usuario">Usuario:</label> <input type="text"
						maxlength="30" name="usuario" id="usuario" class="form-control"
						placeholder="Usuario" required
						value="<%=(clienteModificar != null) ? clienteModificar.getUsuario() : ""%>"
						<%=(clienteModificar != null && clienteModificar.getIdCliente() != 0) ? "readonly" : ""%>>
					<label for="contrasena">Contraseña:</label> <input type="password"
						maxlength="10" name="contrasena" id="contrasena"
						class="form-control" placeholder="Contraseña" required
						value="<%=(clienteModificar != null) ? clienteModificar.getContrasena() : ""%>">
					<label for="repetirContrasena">Repetir Contraseña:</label> <input
						type="password" name="repetirContrasena" id="repetirContrasena"
						maxlength="10" class="form-control"
						placeholder="Repetir Contraseña" required
						value="<%=(clienteModificar != null) ? clienteModificar.getContrasena() : ""%>">
					<label for="dni">DNI:</label> <input type="text" name="dni"
						maxlength="10" id="dni" class="form-control" placeholder="DNI"
						required
						value="<%=(clienteModificar != null) ? clienteModificar.getDni() : ""%>">
					<label for="cuil">CUIL:</label> <input type="text" name="cuil"
						maxlength="12" id="cuil" class="form-control" placeholder="CUIL"
						required
						value="<%=(clienteModificar != null) ? clienteModificar.getCuil() : ""%>">
				</div>
				<div class="col-md-3">
					<!-- Columna 2: Datos Personales -->
					<h4>Datos Personales</h4>
					<label for="nombre">Nombre:</label> <input type="text"
						maxlength="20" name="nombre" id="nombre" class="form-control"
						placeholder="Nombre" required
						value="<%=(clienteModificar != null) ? clienteModificar.getNombre() : ""%>">
					<label for="apellido">Apellido:</label> <input type="text"
						name="apellido" id="apellido" class="form-control" maxlength="20"
						placeholder="Apellido" required
						value="<%=(clienteModificar != null) ? clienteModificar.getApellido() : ""%>">
					<label>Sexo:</label> <select name="sexo" id="sexo"
						class="form-control" required>
						<option value="0"
							<%=(clienteModificar != null && clienteModificar.getSexo().ordinal() == 0) ? "selected" : ""%>>Varón</option>
						<option value="1"
							<%=(clienteModificar != null && clienteModificar.getSexo().ordinal() == 1) ? "selected" : ""%>>Mujer</option>
						<option value="2"
							<%=(clienteModificar != null && clienteModificar.getSexo().ordinal() == 2) ? "selected" : ""%>>Indefinido</option>
					</select> <label for="nacionalidad">Nacionalidad:</label> <input type="text"
						name="nacionalidad" id="nacionalidad" class="form-control"
						maxlength="20" placeholder="Nacionalidad" required
						value="<%=(clienteModificar != null) ? clienteModificar.getNacionalidad() : ""%>">
					<label for="fechaNacimiento">Fecha de Nacimiento:</label> <input
						type="text" name="fechaNacimiento" class="datepicker form-control"
						id="fechaNacimiento" placeholder="Fecha de Nacimiento" required
						value="<%=(clienteModificar != null) ? request.getAttribute("fechaNacimiento") : ""%>">
				</div>
				<div class="col-md-3">
					<!-- Columna 3: Dirección y Ubicación -->
					<h4>Dirección y Ubicación</h4>
					<label for="direccion">Dirección:</label> <input type="text"
						name="direccion" id="direccion" class="form-control"
						maxlength="20" placeholder="Dirección" required
						value="<%=(clienteModificar != null) ? clienteModificar.getDireccion() : ""%>">
					<label for="provincia">Provincia:</label>
					<!-- Desplegable de provincias -->
					<select name="provincia" id="provincia" class="form-control"
						required>
						<option value="">Seleccionar Provincia</option>
						<script>
                    // Llenar el desplegable de provincias desde el array de JavaScript
                    for (var i = 0; i < provinciasArray.length; i++) {
                        var provincia = provinciasArray[i];
                        document.write('<option value="' + provincia.id + '">' + provincia.nombre + '</option>');
                    }
                </script>
					</select> <label for="localidad">Localidad:</label>
					<!-- Desplegable de localidades -->
					<select name="localidad" id="localidad" class="form-control"
						required>
						<option value="">Seleccionar Localidad</option>
					</select> <label for="correo">Correo:</label> <input type="email"
						name="correo" id="correo" class="form-control" maxlength="20"
						placeholder="Correo" required
						value="<%=(clienteModificar != null) ? clienteModificar.getCorreo() : ""%>">
					<div>
						<input style="margin-top: 20px;" type="submit"
							name="btn<%=(clienteModificar != null && clienteModificar.getIdCliente() != 0) ? "Modificar" : "Guardar"%>"
							class="btn btn-primary"
							value="<%=(clienteModificar != null && clienteModificar.getIdCliente() != 0) ? "Modificar" : "Guardar"%>" />
					</div>
				</div>
				<div class="col-md-3">
					<!-- Columna 4: Teléfonos -->
					<h4>Teléfonos</h4>
					<div class="form-group">
						<label for="nuevoTelefono">Nuevo Teléfono:</label> <input
							maxlength="10" type="text" id="nuevoTelefono"
							class="form-control" placeholder="Nuevo Teléfono">
						<button style="margin-top: 10px; margin-bottom: 5px" type="button"
							id="agregarTelefonoButton" class="btn btn-success"
							onclick="agregarTelefono()">
							<i class="bi bi-telephone"></i> Agregar Teléfono
						</button>
					</div>
					<div class="form-group">
						<label for="telefonosList">Teléfonos:</label>
						<ul id="telefonosList" class="scrollable-list">
							<!-- Aquí se mostrarán los teléfonos -->
							<%
								if (listaTelefonos != null) {
									for (Telefono telefono : listaTelefonos) {
							%>
							<li><input type="text" name="telefonos"
								value="<%=telefono.getNumero()%>" class="form-control">
								<button onclick="eliminarTelefono(this)" class="btn btn-danger">
									<i class="bi bi-trash"></i>
									<!-- Ícono de eliminación de Bootstrap -->
								</button></li>
							<%
								}
							%>
							<%
								}
							%>
						</ul>
					</div>
				</div>

			</div>
		</div>

		<a href="ServletCliente?lista=1" class="btn btn-secondary">Volver
			al Listado</a>
	</form>

</body>
</html>


