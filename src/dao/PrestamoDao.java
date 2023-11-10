package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import entidad.Cliente;
import entidad.Cuenta;
import entidad.Localidad;
import entidad.Prestamo;
import entidad.Prestamo.Estado;
import entidad.Provincia;
import entidad.TipoCuenta;
import interfazDao.IPrestamoDao;
import java.time.LocalDateTime;

public class PrestamoDao implements IPrestamoDao {
	private Connection conexion;

	public PrestamoDao() {
		conexion = Conexion.obtenerConexion();
	}

	@Override
	public ArrayList<Prestamo> ListarTodos() {
		ArrayList<Prestamo> prestamos = new ArrayList<>();

		String query = "SELECT p.*, c.*, cl.*, tc.descripcion AS tipoCuentaDescripcion, l.nombre AS localidadDescripcion, pr.nombre AS provinciaDescripcion "
				+ "FROM prestamo p " + "JOIN cuenta c ON p.numeroCuenta = c.numero "
				+ "JOIN cliente cl ON c.idCliente = cl.idCliente "
				+ "JOIN tiposcuenta tc ON c.idTipoCuenta = tc.idTipoCuenta "
				+ "JOIN provincias pr ON cl.idProvincia = pr.id " + "JOIN localidades l ON cl.idLocalidad = l.id";

		try (PreparedStatement statement = conexion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				Prestamo prestamo = new Prestamo();
				Cuenta cuenta = new Cuenta();
				Cliente cliente = new Cliente();
				TipoCuenta tipoCuenta = new TipoCuenta();
				Localidad localidad = new Localidad();
				Provincia provincia = new Provincia();

				prestamo.setIdPrestamo(resultSet.getInt("idPrestamo"));
				cuenta.setNumero(resultSet.getInt("numeroCuenta"));

				cuenta.setCBU(resultSet.getString("CBU"));
				cuenta.setSaldo(resultSet.getDouble("saldo"));
				cuenta.setFecha(resultSet.getDate("fecha").toLocalDate());
				cuenta.setActivo(resultSet.getInt("activo"));
				tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuenta"));
				tipoCuenta.setDescripcion(resultSet.getString("tipoCuentaDescripcion"));
				cuenta.setTipoCuenta(tipoCuenta);
				prestamo.setCuenta(cuenta);
				prestamo.setImportePedido(resultSet.getDouble("importePedido"));
				prestamo.setImportePorMes(resultSet.getDouble("importexmes"));
				prestamo.setCuotas(resultSet.getInt("cuotas"));
				prestamo.setFechaPedido(resultSet.getDate("fechaPedido").toLocalDate());

				int estadoNumerico = resultSet.getInt("estado");
				Estado estadoPrestamo = Estado.PENDIENTE;
				if (estadoNumerico == 1) {
					estadoPrestamo = Estado.APROBADO;
				} else if (estadoNumerico == 2) {
					estadoPrestamo = Estado.RECHAZADO;
				}
				prestamo.setEstado(estadoPrestamo);

				// Cliente
				cliente.setIdCliente(resultSet.getInt("idCliente"));
				cliente.setUsuario(resultSet.getString("usuario"));
				cliente.setContrasena(resultSet.getString("contraseña"));
				cliente.setActivo(resultSet.getInt("activo"));
				cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
				cliente.setTipoCliente(Cliente.TipoCliente.values()[resultSet.getInt("idTipo")]);
				cliente.setDni(resultSet.getInt("dni"));
				cliente.setCuil(resultSet.getString("cuil"));
				cliente.setNombre(resultSet.getString("nombre"));
				cliente.setApellido(resultSet.getString("apellido"));
				cliente.setSexo(Cliente.Sexo.values()[resultSet.getInt("sexo")]);
				cliente.setNacionalidad(resultSet.getString("nacionalidad"));
				cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
				cliente.setDireccion(resultSet.getString("direccion"));
				cliente.setCorreo(resultSet.getString("correo"));

				localidad.setId(resultSet.getInt("idLocalidad"));
				localidad.setIdProvincia(resultSet.getInt("idProvincia"));
				localidad.setNombre(resultSet.getString("localidadDescripcion"));
				cliente.setLocalidad(localidad);

				provincia.setId(resultSet.getInt("idProvincia"));
				provincia.setNombre(resultSet.getString("provinciaDescripcion"));
				cliente.setProvincia(provincia);

				cuenta.setCliente(cliente);
				prestamo.setCuenta(cuenta);

				prestamos.add(prestamo);
			}
		} catch (SQLException e) {
			System.err.println("Error al listar todos los préstamos: " + e.getMessage());
		}

		return prestamos;
	}

	@Override
	public ArrayList<Prestamo> ListarPendientes(String busqueda) {
		ArrayList<Prestamo> prestamosPendientes = new ArrayList<>();

		// Define la consulta SQL base sin filtro de búsqueda
		String query = "SELECT p.*, c.*, cl.*, tc.descripcion AS tipoCuentaDescripcion, l.nombre AS localidadDescripcion, pr.nombre AS provinciaDescripcion "
				+ "FROM prestamo p " + "JOIN cuenta c ON p.numeroCuenta = c.numero "
				+ "JOIN cliente cl ON c.idCliente = cl.idCliente "
				+ "JOIN tiposcuenta tc ON c.idTipoCuenta = tc.idTipoCuenta "
				+ "JOIN provincias pr ON cl.idProvincia = pr.id " + "JOIN localidades l ON cl.idLocalidad = l.id "
				+ "WHERE p.estado = 0";

		// Verifica si se proporciona una cadena de búsqueda
		if (busqueda != null) {
			// Agrega el filtro de búsqueda para campos específicos
			query += " AND (c.numero LIKE ? OR cl.idCliente LIKE ? OR p.importePedido LIKE ? OR p.importexmes LIKE ? OR p.fechaPedido LIKE ? OR p.idPrestamo LIKE ?)";
		}

		try (PreparedStatement statement = conexion.prepareStatement(query)) {
			// Establece los parámetros en la consulta
			if (busqueda != null) {
				String searchString = "%" + busqueda + "%";
				for (int i = 1; i <= 6; i++) {
					statement.setString(i, searchString);
				}
			}
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Prestamo prestamo = new Prestamo();
					Cuenta cuenta = new Cuenta();
					Cliente cliente = new Cliente();
					TipoCuenta tipoCuenta = new TipoCuenta();
					Localidad localidad = new Localidad();
					Provincia provincia = new Provincia();

					prestamo.setIdPrestamo(resultSet.getInt("idPrestamo"));
					cuenta.setNumero(resultSet.getInt("numeroCuenta"));

					cuenta.setCBU(resultSet.getString("CBU"));
					cuenta.setSaldo(resultSet.getDouble("saldo"));
					cuenta.setFecha(resultSet.getDate("fecha").toLocalDate());
					cuenta.setActivo(resultSet.getInt("activo"));
					tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuenta"));
					tipoCuenta.setDescripcion(resultSet.getString("tipoCuentaDescripcion"));
					cuenta.setTipoCuenta(tipoCuenta);
					prestamo.setCuenta(cuenta);
					prestamo.setImportePedido(resultSet.getDouble("importePedido"));
					prestamo.setImportePorMes(resultSet.getDouble("importexmes"));
					prestamo.setCuotas(resultSet.getInt("cuotas"));
					prestamo.setFechaPedido(resultSet.getDate("fechaPedido").toLocalDate());
					prestamo.setEstado(Estado.PENDIENTE);

					// Cliente
					cliente.setIdCliente(resultSet.getInt("idCliente"));
					cliente.setUsuario(resultSet.getString("usuario"));
					cliente.setContrasena(resultSet.getString("contraseña"));
					cliente.setActivo(resultSet.getInt("activo"));
					cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
					cliente.setTipoCliente(Cliente.TipoCliente.values()[resultSet.getInt("idTipo")]);
					cliente.setDni(resultSet.getInt("dni"));
					cliente.setCuil(resultSet.getString("cuil"));
					cliente.setNombre(resultSet.getString("nombre"));
					cliente.setApellido(resultSet.getString("apellido"));
					cliente.setSexo(Cliente.Sexo.values()[resultSet.getInt("sexo")]);
					cliente.setNacionalidad(resultSet.getString("nacionalidad"));
					cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
					cliente.setDireccion(resultSet.getString("direccion"));
					cliente.setCorreo(resultSet.getString("correo"));

					localidad.setId(resultSet.getInt("idLocalidad"));
					localidad.setNombre(resultSet.getString("localidadDescripcion"));
					cliente.setLocalidad(localidad);

					provincia.setId(resultSet.getInt("idProvincia"));
					provincia.setNombre(resultSet.getString("provinciaDescripcion"));
					cliente.setProvincia(provincia);

					cuenta.setCliente(cliente);
					prestamo.setCuenta(cuenta);

					prestamosPendientes.add(prestamo);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al listar préstamos pendientes: " + e.getMessage());
		}

		return prestamosPendientes;
	}

	@Override
	public int PedirPrestamo(Prestamo prestamo) {
		int resultado = 0;

		try (PreparedStatement statement = conexion.prepareStatement(
				"INSERT INTO prestamo (numeroCuenta, importePedido, importexmes, cuotas, fechaPedido, estado) VALUES (?, ?, ?, ?, ?, ?)",
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, prestamo.getCuenta().getNumero());
			statement.setDouble(2, prestamo.getImportePedido());

			// IMPORTE CON 10% intereses mensual
			double importeMensual = (prestamo.getImportePedido() * 1.1) / prestamo.getCuotas();
			statement.setDouble(3, importeMensual);

			statement.setInt(4, prestamo.getCuotas());

			// Establece la fecha actual
			statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

			// Establece el estado como 0 (pendiente)
			statement.setInt(6, Estado.PENDIENTE.ordinal());

			resultado = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al pedir un préstamo: " + e.getMessage());
		}

		return resultado;
	}

	@Override
	public int CambiarEstadoPrestamo(int idPrestamo, Estado estado) {
		int resultado = 0;

		try (PreparedStatement statement = conexion
				.prepareStatement("UPDATE prestamo SET estado = ? WHERE idPrestamo = ?")) {
			statement.setInt(1, estado.ordinal());
			statement.setInt(2, idPrestamo);

			resultado = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al cambiar el estado de un préstamo: " + e.getMessage());
		}

		return resultado;
	}

	@Override
	public Prestamo ObtenerPrestamoPorId(int idPrestamo) {
		Prestamo prestamo = null;

		try (PreparedStatement statement = conexion.prepareStatement("SELECT p.*, "
				+ "c.numero AS numeroCuenta, c.CBU AS CBU, c.saldo AS saldoCuenta, c.fecha AS fechaCuenta, "
				+ "c.activo AS activoCuenta, c.idTipoCuenta AS idTipoCuentaCuenta, "
				+ "tc.descripcion AS tipoCuentaDescripcion, cl.*, l.id AS idLocalidad, "
				+ "l.idProvincia AS idProvinciaLocalidad, l.nombre AS localidadDescripcion, pr.id AS idProvincia, "
				+ "pr.nombre AS provinciaDescripcion " + "FROM prestamo p "
				+ "JOIN cuenta c ON p.numeroCuenta = c.numero " + "JOIN cliente cl ON c.idCliente = cl.idCliente "
				+ "JOIN tiposcuenta tc ON c.idTipoCuenta = tc.idTipoCuenta "
				+ "JOIN localidades l ON cl.idLocalidad = l.id " + "JOIN provincias pr ON cl.idProvincia = pr.id "
				+ "WHERE p.idPrestamo = ?")) {
			statement.setInt(1, idPrestamo);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					prestamo = new Prestamo();
					Cuenta cuenta = new Cuenta();
					Cliente cliente = new Cliente();
					TipoCuenta tipoCuenta = new TipoCuenta();
					Localidad localidad = new Localidad();
					Provincia provincia = new Provincia();

					prestamo.setIdPrestamo(resultSet.getInt("idPrestamo"));
					cuenta.setNumero(resultSet.getInt("numeroCuenta"));
					cuenta.setCBU(resultSet.getString("CBU"));
					cuenta.setSaldo(resultSet.getDouble("saldoCuenta"));
					cuenta.setFecha(resultSet.getDate("fechaCuenta").toLocalDate());
					cuenta.setActivo(resultSet.getInt("activoCuenta"));
					tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuentaCuenta"));
					tipoCuenta.setDescripcion(resultSet.getString("tipoCuentaDescripcion"));
					cuenta.setTipoCuenta(tipoCuenta);
					prestamo.setImportePedido(resultSet.getDouble("importePedido"));
					prestamo.setImportePorMes(resultSet.getDouble("importexmes"));
					prestamo.setCuotas(resultSet.getInt("cuotas"));
					prestamo.setFechaPedido(resultSet.getDate("fechaPedido").toLocalDate());

					int estadoNumerico = resultSet.getInt("estado");
					Estado estadoPrestamo = Estado.PENDIENTE;
					if (estadoNumerico == 1) {
						estadoPrestamo = Estado.APROBADO;
					} else if (estadoNumerico == 2) {
						estadoPrestamo = Estado.RECHAZADO;
					}
					prestamo.setEstado(estadoPrestamo);

					// Cliente
					cliente.setIdCliente(resultSet.getInt("idCliente"));
					cliente.setUsuario(resultSet.getString("usuario"));
					cliente.setContrasena(resultSet.getString("contraseña"));
					cliente.setActivo(resultSet.getInt("activo"));
					cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
					cliente.setTipoCliente(Cliente.TipoCliente.values()[resultSet.getInt("idTipo")]);
					cliente.setDni(resultSet.getInt("dni"));
					cliente.setCuil(resultSet.getString("cuil"));
					cliente.setNombre(resultSet.getString("nombre"));
					cliente.setApellido(resultSet.getString("apellido"));
					cliente.setSexo(Cliente.Sexo.values()[resultSet.getInt("sexo")]);
					cliente.setNacionalidad(resultSet.getString("nacionalidad"));
					cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
					cliente.setDireccion(resultSet.getString("direccion"));
					cliente.setCorreo(resultSet.getString("correo"));

					localidad.setId(resultSet.getInt("idLocalidad"));
					localidad.setIdProvincia(resultSet.getInt("idProvinciaLocalidad"));
					localidad.setNombre(resultSet.getString("localidadDescripcion"));
					cliente.setLocalidad(localidad);

					provincia.setId(resultSet.getInt("idProvincia"));
					provincia.setNombre(resultSet.getString("provinciaDescripcion"));

					cliente.setProvincia(provincia);

					cuenta.setCliente(cliente);
					prestamo.setCuenta(cuenta);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener un préstamo por ID: " + e.getMessage());
		}

		return prestamo;
	}

	@Override
	public ArrayList<Prestamo> ListarPorClienteAprobados(int idCliente) {
		ArrayList<Prestamo> prestamosAprobados = new ArrayList<>();

		try (PreparedStatement statement = conexion.prepareStatement(
				"SELECT p.*, c.*, cl.*, tc.descripcion AS tipoCuentaDescripcion, l.nombre AS localidadDescripcion, pr.nombre AS provinciaDescripcion "
						+ "FROM prestamo p " + "JOIN cuenta c ON p.numeroCuenta = c.numero "
						+ "JOIN cliente cl ON c.idCliente = cl.idCliente "
						+ "JOIN tiposcuenta tc ON c.idTipoCuenta = tc.id "
						+ "JOIN localidades l ON cl.idLocalidad = l.id "
						+ "JOIN provincias pr ON cl.idProvincia = pr.id " + "WHERE c.idCliente = ? "
						+ "AND p.estado = 1 " + "AND c.activo = 1")) {
			statement.setInt(1, idCliente);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Prestamo prestamo = new Prestamo();
					Cuenta cuenta = new Cuenta();
					Cliente cliente = new Cliente();
					TipoCuenta tipoCuenta = new TipoCuenta();
					Localidad localidad = new Localidad();
					Provincia provincia = new Provincia();

					prestamo.setIdPrestamo(resultSet.getInt("idPrestamo"));
					cuenta.setNumero(resultSet.getInt("numeroCuenta"));
					cliente.setIdCliente(resultSet.getInt("idCliente"));

					cuenta.setCliente(cliente);
					cuenta.setCBU(resultSet.getString("CBU"));
					cuenta.setSaldo(resultSet.getDouble("saldo"));
					cuenta.setFecha(resultSet.getDate("fecha").toLocalDate());
					cuenta.setActivo(resultSet.getInt("activo"));
					tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuenta"));
					tipoCuenta.setDescripcion(resultSet.getString("tipoCuentaDescripcion"));
					cuenta.setTipoCuenta(tipoCuenta);

					prestamo.setImportePedido(resultSet.getDouble("importePedido"));
					prestamo.setImportePorMes(resultSet.getDouble("importexmes"));
					prestamo.setCuotas(resultSet.getInt("cuotas"));
					prestamo.setFechaPedido(resultSet.getDate("fechaPedido").toLocalDate());
					prestamo.setEstado(Estado.APROBADO);

					// Cliente
					cliente.setIdCliente(resultSet.getInt("idCliente"));
					cliente.setUsuario(resultSet.getString("usuario"));
					cliente.setContrasena(resultSet.getString("contraseña"));
					cliente.setActivo(resultSet.getInt("activo"));
					cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
					cliente.setTipoCliente(Cliente.TipoCliente.values()[resultSet.getInt("idTipo")]);
					cliente.setDni(resultSet.getInt("dni"));
					cliente.setCuil(resultSet.getString("cuil"));
					cliente.setNombre(resultSet.getString("nombre"));
					cliente.setApellido(resultSet.getString("apellido"));
					cliente.setSexo(Cliente.Sexo.values()[resultSet.getInt("sexo")]);
					cliente.setNacionalidad(resultSet.getString("nacionalidad"));
					cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
					cliente.setDireccion(resultSet.getString("direccion"));
					cliente.setCorreo(resultSet.getString("correo"));

					localidad.setId(resultSet.getInt("idLocalidad"));
					localidad.setNombre(resultSet.getString("localidadDescripcion"));
					cliente.setLocalidad(localidad);

					provincia.setId(resultSet.getInt("idProvincia"));
					provincia.setNombre(resultSet.getString("provinciaDescripcion"));
					cliente.setProvincia(provincia);

					cuenta.setCliente(cliente);
					prestamo.setCuenta(cuenta);

					prestamosAprobados.add(prestamo);
				}
			}
		} catch (SQLException e) {
			System.err.println(
					"Error al listar préstamos aprobados con cuentas activas para un cliente: " + e.getMessage());
		}
		return prestamosAprobados;
	}

}
