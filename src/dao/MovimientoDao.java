package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import entidad.Cliente;
import entidad.Cuenta;
import entidad.Localidad;
import entidad.Movimiento;
import entidad.Provincia;
import entidad.TipoCuenta;
import entidad.TipoMovimiento;
import interfazDao.IMovimientoDao;

public class MovimientoDao implements IMovimientoDao {
	private Connection conexion;

	public MovimientoDao() {
		conexion = Conexion.obtenerConexion();
	}

	public ArrayList<Movimiento> ListarPorNumeroCuenta(int numeroCuenta, String busqueda) {
		ArrayList<Movimiento> listaMovimientos = new ArrayList<>();

		try {
			String query = "SELECT m.idMovimiento, m.detalle, m.importe, m.idTipoMovimiento, t.descripcion, m.fecha, "
					+ "c.numero AS numeroCuenta, c.CBU, c.saldo, c.fecha AS fechaCuenta, c.activo AS activoCuenta, "
					+ "c.idTipoCuenta, cl.idCliente, cl.usuario, cl.contraseña, cl.activo AS activoCliente, "
					+ "cl.fechaCreacion AS fechaCreacionCliente, cl.idTipo AS idTipoCliente, cl.dni, cl.cuil, "
					+ "cl.nombre AS nombreCliente, cl.apellido, cl.sexo AS sexoCliente, cl.nacionalidad, "
					+ "cl.fechaNacimiento AS fechaNacimientoCliente, cl.direccion, cl.idLocalidad, cl.idProvincia, "
					+ "cl.correo " + "FROM movimiento m "
					+ "INNER JOIN tiposmovimiento t ON m.idTipoMovimiento = t.idTipoMovimiento "
					+ "INNER JOIN cuenta c ON m.numeroCuenta = c.numero "
					+ "INNER JOIN cliente cl ON c.idCliente = cl.idCliente " + "WHERE m.numeroCuenta = ?";

			if (busqueda != null) {
				query += " AND (m.detalle LIKE ? OR m.fecha LIKE ? OR t.descripcion LIKE ?)";
			}

			query += " ORDER BY m.idMovimiento DESC";

			// Crea una instancia de PreparedStatement con la consulta
			PreparedStatement preparedStatement = conexion.prepareStatement(query);
			preparedStatement.setInt(1, numeroCuenta); // Asigna el valor del número de cuenta

			if (busqueda != null) {
				preparedStatement.setString(2, "%" + busqueda + "%"); // Búsqueda en detalle
				preparedStatement.setString(3, "%" + busqueda + "%"); // Búsqueda en fecha
				preparedStatement.setString(4, "%" + busqueda + "%"); // Búsqueda en descripción del tipo de movimiento
			}

			// Ejecuta la consulta
			ResultSet resultSet = preparedStatement.executeQuery();

			// Procesa los resultados
			while (resultSet.next()) {
				// Obtener información detallada de la cuenta y el cliente
				int numeroCuenta2 = resultSet.getInt("numeroCuenta");
				String CBU = resultSet.getString("CBU");
				double saldo = resultSet.getDouble("saldo");
				LocalDate fechaCuenta = resultSet.getDate("fechaCuenta").toLocalDate();
				int activoCuenta = resultSet.getInt("activoCuenta");
				int idTipoCuenta = resultSet.getInt("idTipoCuenta");

				int idCliente = resultSet.getInt("idCliente");
				String usuario = resultSet.getString("usuario");
				String contrasena = resultSet.getString("contraseña");
				int activoCliente = resultSet.getInt("activoCliente");
				LocalDate fechaCreacionCliente = resultSet.getDate("fechaCreacionCliente").toLocalDate();
				int idTipoCliente = resultSet.getInt("idTipoCliente");
				int dni = resultSet.getInt("dni");
				String cuil = resultSet.getString("cuil");
				String nombreCliente = resultSet.getString("nombreCliente");
				String apellido = resultSet.getString("apellido");
				int sexoCliente = resultSet.getInt("sexoCliente");
				String nacionalidad = resultSet.getString("nacionalidad");
				LocalDate fechaNacimientoCliente = resultSet.getDate("fechaNacimientoCliente").toLocalDate();
				String direccion = resultSet.getString("direccion");
				int idLocalidad = resultSet.getInt("idLocalidad");
				int idProvincia = resultSet.getInt("idProvincia");
				String correo = resultSet.getString("correo");

				// Crea instancias de Cuenta y Cliente con información detallada
				Cuenta cuenta = new Cuenta();
				cuenta.setNumero(numeroCuenta2);
				cuenta.setCBU(CBU);
				cuenta.setSaldo(saldo);
				cuenta.setFecha(fechaCuenta);
				cuenta.setActivo(activoCuenta);

				TipoCuenta tipoCuenta = new TipoCuenta();
				tipoCuenta.setIdTipoCuenta(idTipoCuenta);
				cuenta.setTipoCuenta(tipoCuenta);

				Cliente cliente = new Cliente();
				cliente.setIdCliente(idCliente);
				cliente.setUsuario(usuario);
				cliente.setContrasena(contrasena);
				cliente.setActivo(activoCliente);
				cliente.setFechaCreacion(fechaCreacionCliente);
				cliente.setTipoCliente(idTipoCliente);
				cliente.setDni(dni);
				cliente.setCuil(cuil);
				cliente.setNombre(nombreCliente);
				cliente.setApellido(apellido);
				cliente.setSexo(sexoCliente);
				cliente.setNacionalidad(nacionalidad);
				cliente.setFechaNacimiento(fechaNacimientoCliente);
				cliente.setDireccion(direccion);

				Localidad localidad = new Localidad();
				localidad.setId(idLocalidad);
				localidad.setIdProvincia(idProvincia);
				cliente.setLocalidad(localidad);

				Provincia provincia = new Provincia();
				provincia.setId(idProvincia);
				cliente.setProvincia(provincia);

				cliente.setCorreo(correo);

				// Obtener información del movimiento
				int idMovimiento = resultSet.getInt("idMovimiento");
				String detalle = resultSet.getString("detalle");
				double importe = resultSet.getDouble("importe");
				int idTipoMovimiento = resultSet.getInt("idTipoMovimiento");
				String tipoMovimientoDescripcion = resultSet.getString("descripcion");
				LocalDate fechaMovimiento = resultSet.getDate("fecha").toLocalDate();

				TipoMovimiento tipoMovimiento = new TipoMovimiento();
				tipoMovimiento.setIdTipoMovimiento(idTipoMovimiento);
				tipoMovimiento.setDescripcion(tipoMovimientoDescripcion);

				Movimiento movimiento = new Movimiento();
				movimiento.setIdMovimiento(idMovimiento);
				movimiento.setDetalle(detalle);
				movimiento.setImporte(importe);
				movimiento.setIdTipoMovimiento(tipoMovimiento);
				movimiento.setFecha(fechaMovimiento);

				cuenta.setCliente(cliente);
				movimiento.setCuenta(cuenta);

				listaMovimientos.add(movimiento);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaMovimientos;
	}

	@Override
	public int Agregar(Movimiento movimiento) {
		try {
			String query = "INSERT INTO movimiento (numeroCuenta, detalle, importe, idTipoMovimiento, fecha) VALUES (?, ?, ?, ?, ?)";

			PreparedStatement preparedStatement = conexion.prepareStatement(query);

			preparedStatement.setInt(1, movimiento.getCuenta().getNumero());
			preparedStatement.setString(2, movimiento.getDetalle());
			preparedStatement.setDouble(3, movimiento.getImporte());
			preparedStatement.setInt(4, movimiento.getIdTipoMovimiento().getIdTipoMovimiento());

			LocalDate fecha = movimiento.getFecha();
			if (fecha == null) {
				fecha = LocalDate.now();
			}
			preparedStatement.setDate(5, Date.valueOf(fecha));

			int filasInsertadas = preparedStatement.executeUpdate();

			preparedStatement.close();

			return filasInsertadas;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0; // Retorna un valor negativo en caso de error
		}
	}

	@Override
	public ArrayList<Movimiento> ListarTodosMovimientos() {
		ArrayList<Movimiento> listaMovimientos = new ArrayList<>();

		try {
			// Crea la consulta SQL para obtener todos los movimientos, ordenados por ID de
			// movimiento en orden descendente
			String query = "SELECT m.idMovimiento, m.detalle, m.importe, m.idTipoMovimiento, t.descripcion, m.fecha, "
					+ "c.numero AS numeroCuenta, c.CBU, c.saldo, c.fecha AS fechaCuenta, c.activo AS activoCuenta, "
					+ "c.idTipoCuenta, cl.idCliente, cl.usuario, cl.contrasena, cl.activo AS activoCliente, "
					+ "cl.fechaCreacion AS fechaCreacionCliente, cl.idTipo AS idTipoCliente, cl.dni, cl.cuil, "
					+ "cl.nombre AS nombreCliente, cl.apellido, cl.sexo AS sexoCliente, cl.nacionalidad, "
					+ "cl.fechaNacimiento AS fechaNacimientoCliente, cl.direccion, cl.idLocalidad, cl.idProvincia, "
					+ "cl.correo " + "FROM movimiento m "
					+ "INNER JOIN tiposmovimiento t ON m.idTipoMovimiento = t.idTipoMovimiento "
					+ "INNER JOIN cuenta c ON m.numeroCuenta = c.numero "
					+ "INNER JOIN cliente cl ON c.idCliente = cl.idCliente " + "ORDER BY m.idMovimiento DESC";

			// Crea una instancia de PreparedStatement con la consulta
			PreparedStatement preparedStatement = conexion.prepareStatement(query);

			// Ejecuta la consulta
			ResultSet resultSet = preparedStatement.executeQuery();

			// Procesa los resultados
			while (resultSet.next()) {
				// Obtener información detallada de la cuenta y el cliente
				int numeroCuenta = resultSet.getInt("numeroCuenta");
				String CBU = resultSet.getString("CBU");
				double saldo = resultSet.getDouble("saldo");
				LocalDate fechaCuenta = resultSet.getDate("fechaCuenta").toLocalDate();
				int activoCuenta = resultSet.getInt("activoCuenta");
				int idTipoCuenta = resultSet.getInt("idTipoCuenta");

				int idCliente = resultSet.getInt("idCliente");
				String usuario = resultSet.getString("usuario");
				String contrasena = resultSet.getString("contrasena");
				int activoCliente = resultSet.getInt("activoCliente");
				LocalDate fechaCreacionCliente = resultSet.getDate("fechaCreacionCliente").toLocalDate();
				int idTipoCliente = resultSet.getInt("idTipoCliente");
				int dni = resultSet.getInt("dni");
				String cuil = resultSet.getString("cuil");
				String nombreCliente = resultSet.getString("nombreCliente");
				String apellido = resultSet.getString("apellido");
				int sexoCliente = resultSet.getInt("sexoCliente");
				String nacionalidad = resultSet.getString("nacionalidad");
				LocalDate fechaNacimientoCliente = resultSet.getDate("fechaNacimientoCliente").toLocalDate();
				String direccion = resultSet.getString("direccion");
				int idLocalidad = resultSet.getInt("idLocalidad");
				int idProvincia = resultSet.getInt("idProvincia");
				String correo = resultSet.getString("correo");

				// Crea instancias de Cuenta y Cliente con información detallada
				Cuenta cuenta = new Cuenta();
				cuenta.setNumero(numeroCuenta);
				cuenta.setCBU(CBU);
				cuenta.setSaldo(saldo);
				cuenta.setFecha(fechaCuenta);
				cuenta.setActivo(activoCuenta);

				TipoCuenta tipoCuenta = new TipoCuenta();
				tipoCuenta.setIdTipoCuenta(idTipoCuenta);
				cuenta.setTipoCuenta(tipoCuenta);

				Cliente cliente = new Cliente();
				cliente.setIdCliente(idCliente);
				cliente.setUsuario(usuario);
				cliente.setContrasena(contrasena);
				cliente.setActivo(activoCliente);
				cliente.setFechaCreacion(fechaCreacionCliente);
				cliente.setTipoCliente(idTipoCliente);
				cliente.setDni(dni);
				cliente.setCuil(cuil);
				cliente.setNombre(nombreCliente);
				cliente.setApellido(apellido);
				cliente.setSexo(sexoCliente);
				cliente.setNacionalidad(nacionalidad);
				cliente.setFechaNacimiento(fechaNacimientoCliente);
				cliente.setDireccion(direccion);

				Localidad localidad = new Localidad();
				localidad.setId(idLocalidad);
				localidad.setIdProvincia(idProvincia);
				cliente.setLocalidad(localidad);

				Provincia provincia = new Provincia();
				provincia.setId(idProvincia);
				cliente.setProvincia(provincia);

				cliente.setCorreo(correo);

				// Obtener información del movimiento
				int idMovimiento = resultSet.getInt("idMovimiento");
				String detalle = resultSet.getString("detalle");
				double importe = resultSet.getDouble("importe");
				int idTipoMovimiento = resultSet.getInt("idTipoMovimiento");
				String tipoMovimientoDescripcion = resultSet.getString("descripcion");
				LocalDate fechaMovimiento = resultSet.getDate("fecha").toLocalDate();

				TipoMovimiento tipoMovimiento = new TipoMovimiento();
				tipoMovimiento.setIdTipoMovimiento(idTipoMovimiento);
				tipoMovimiento.setDescripcion(tipoMovimientoDescripcion);

				Movimiento movimiento = new Movimiento();
				movimiento.setIdMovimiento(idMovimiento);
				movimiento.setDetalle(detalle);
				movimiento.setImporte(importe);
				movimiento.setIdTipoMovimiento(tipoMovimiento);
				movimiento.setFecha(fechaMovimiento);

				cuenta.setCliente(cliente);
				movimiento.setCuenta(cuenta);

				listaMovimientos.add(movimiento);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaMovimientos;
	}

}
