package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entidad.Cliente;
import entidad.Cuenta;
import entidad.Localidad;
import entidad.Provincia;
import entidad.TipoCuenta;
import interfazDao.ICuentaDao;
import java.time.LocalDate;

public class CuentaDao implements ICuentaDao {
	private Connection conexion;

	public CuentaDao() {
		conexion = Conexion.obtenerConexion();
	}

	public int Agregar(Cuenta cuenta) {
		int numeroCuenta = 0;
		String cbu = generarCBUUnico();
		LocalDate fechaActual = LocalDate.now();
		int idCliente = cuenta.getCliente().getIdCliente(); // Obtener el ID del cliente desde la cuenta

		try (PreparedStatement statement = conexion.prepareStatement(
				"INSERT INTO cuenta (idCliente, CBU, saldo, fecha, activo, idTipoCuenta) VALUES (?, ?, ?, ?, 1, ?)",
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, idCliente); // Usar el ID del cliente
			statement.setString(2, cbu);
			statement.setDouble(3, 10000); // Valor fijo de 10,000
			statement.setObject(4, fechaActual);
			statement.setInt(5, cuenta.getTipoCuenta().getIdTipoCuenta());

			int filasAfectadas = statement.executeUpdate();

			if (filasAfectadas > 0) {
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next()) {
					numeroCuenta = generatedKeys.getInt(1);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al agregar la cuenta: " + e.getMessage());
		}

		return numeroCuenta;
	}

	public Cuenta ObtenerPorNumeroCuenta(int numeroCuenta) {
	    Cuenta cuenta = null;

	    try (PreparedStatement statement = conexion.prepareStatement("SELECT c.*, tc.descripcion, " +
	            "cl.*, " +
	            "loc.id AS idLocalidad, loc.Nombre AS nombreLocalidad, " +
	            "prov.id AS idProvincia, prov.Nombre AS nombreProvincia " +
	            "FROM cuenta c " +
	            "INNER JOIN tiposcuenta tc ON c.idTipoCuenta = tc.idTipoCuenta " +
	            "INNER JOIN cliente cl ON c.idCliente = cl.idCliente " +
	            "INNER JOIN localidades loc ON cl.idLocalidad = loc.id " +
	            "INNER JOIN provincias prov ON cl.idProvincia = prov.id " +
	            "WHERE c.numero = ? AND c.activo = 1")) {
	        statement.setInt(1, numeroCuenta);

	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                cuenta = new Cuenta();
	                cuenta.setNumero(resultSet.getInt("numero"));
	                cuenta.setCBU(resultSet.getString("CBU"));
	                cuenta.setSaldo(resultSet.getDouble("saldo"));
	                cuenta.setFecha(resultSet.getDate("fecha").toLocalDate());
	                cuenta.setActivo(resultSet.getInt("activo"));

	                TipoCuenta tipoCuenta = new TipoCuenta();
	                tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuenta"));
	                tipoCuenta.setDescripcion(resultSet.getString("descripcion"));

	                cuenta.setTipoCuenta(tipoCuenta);

	                Cliente cliente = new Cliente();
	                cliente.setIdCliente(resultSet.getInt("idCliente"));
	                cliente.setUsuario(resultSet.getString("usuario"));
	                cliente.setContrasena(resultSet.getString("contraseña"));
	                cliente.setActivo(resultSet.getInt("activo"));
	                cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
	                cliente.setTipoCliente(resultSet.getInt("idTipo")); // Obtener el tipo de cliente de la columna idTipo
	                cliente.setDni(resultSet.getInt("dni"));
	                cliente.setCuil(resultSet.getString("cuil"));
	                cliente.setNombre(resultSet.getString("nombre"));
	                cliente.setApellido(resultSet.getString("apellido"));
	                cliente.setSexo(resultSet.getInt("sexo"));
	                cliente.setNacionalidad(resultSet.getString("nacionalidad"));
	                cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
	                cliente.setDireccion(resultSet.getString("direccion"));

	                Localidad localidad = new Localidad();
	                localidad.setId(resultSet.getInt("idLocalidad"));
	                localidad.setIdProvincia(resultSet.getInt("idProvincia"));
	                localidad.setNombre(resultSet.getString("nombreLocalidad"));

	                Provincia provincia = new Provincia();
	                provincia.setId(resultSet.getInt("idProvincia"));
	                provincia.setNombre(resultSet.getString("nombreProvincia"));

	                cliente.setLocalidad(localidad);
	                cliente.setProvincia(provincia);
	                cliente.setCorreo(resultSet.getString("correo"));

	                cuenta.setCliente(cliente);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al obtener la cuenta por número de cuenta: " + e.getMessage());
	    }

	    return cuenta;
	}


	@Override
	public ArrayList<Cuenta> ListarCuentasActivas(String busqueda) {
		ArrayList<Cuenta> cuentas = new ArrayList<>(); // Inicializa la lista

		StringBuilder queryBuilder = new StringBuilder("SELECT c.*, tc.descripcion, "
				+ "cl.idCliente, cl.usuario, cl.contraseña, cl.activo, cl.fechaCreacion, cl.idTipo AS tipoCliente, "
				+ "cl.dni, cl.cuil, cl.nombre, cl.apellido, cl.sexo AS sexoCliente, "
				+ "cl.nacionalidad, cl.fechaNacimiento, cl.direccion, "
				+ "loc.id AS idLocalidad, loc.Nombre AS nombreLocalidad, "
				+ "prov.id AS idProvincia, prov.Nombre AS nombreProvincia, " + "cl.correo " + "FROM cuenta c "
				+ "INNER JOIN tiposcuenta tc ON c.idTipoCuenta = tc.idTipoCuenta "
				+ "INNER JOIN cliente cl ON c.idCliente = cl.idCliente "
				+ "INNER JOIN localidades loc ON cl.idLocalidad = loc.id "
				+ "INNER JOIN provincias prov ON cl.idProvincia = prov.id " + "WHERE c.activo = 1");

		if (busqueda != null && !busqueda.isEmpty()) {
			// Si busqueda no es nulo ni vacío, agregamos las condiciones LIKE
			queryBuilder.append(" AND (c.CBU LIKE ? OR c.numero = ? OR cl.idCliente = ? OR tc.descripcion LIKE ?)");
		}

		try (PreparedStatement statement = conexion.prepareStatement(queryBuilder.toString())) {
			if (busqueda != null && !busqueda.isEmpty()) {
				// Si busqueda no es nulo ni vacío, establecemos los parámetros
				statement.setString(1, "%" + busqueda + "%");
				statement.setString(2, busqueda);
				statement.setString(3, busqueda);
				statement.setString(4, "%" + busqueda + "%");
			}

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Cuenta cuenta = new Cuenta();
					cuenta.setNumero(resultSet.getInt("numero"));
					cuenta.setCBU(resultSet.getString("CBU"));
					cuenta.setSaldo(resultSet.getDouble("saldo"));
					cuenta.setFecha(resultSet.getDate("fecha").toLocalDate());
					cuenta.setActivo(resultSet.getInt("activo"));

					TipoCuenta tipoCuenta = new TipoCuenta();
					tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuenta"));
					tipoCuenta.setDescripcion(resultSet.getString("descripcion"));

					cuenta.setTipoCuenta(tipoCuenta);

					Cliente cliente = new Cliente();
					cliente.setIdCliente(resultSet.getInt("idCliente"));
					cliente.setUsuario(resultSet.getString("usuario"));
					cliente.setContrasena(resultSet.getString("contraseña"));
					cliente.setActivo(resultSet.getInt("activo"));
					cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
					cliente.setTipoCliente(resultSet.getInt("tipoCliente"));
					cliente.setDni(resultSet.getInt("dni"));
					cliente.setCuil(resultSet.getString("cuil"));
					cliente.setNombre(resultSet.getString("nombre"));
					cliente.setApellido(resultSet.getString("apellido"));
					cliente.setSexo(resultSet.getInt("sexoCliente"));
					cliente.setNacionalidad(resultSet.getString("nacionalidad"));
					cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
					cliente.setDireccion(resultSet.getString("direccion"));

					Localidad localidad = new Localidad();
					localidad.setId(resultSet.getInt("idLocalidad"));
					localidad.setIdProvincia(resultSet.getInt("idProvincia"));
					localidad.setNombre(resultSet.getString("nombreLocalidad"));

					Provincia provincia = new Provincia();
					provincia.setId(resultSet.getInt("idProvincia"));
					provincia.setNombre(resultSet.getString("nombreProvincia"));

					cliente.setLocalidad(localidad);
					cliente.setProvincia(provincia);
					cliente.setCorreo(resultSet.getString("correo"));

					cuenta.setCliente(cliente);

					cuentas.add(cuenta);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al listar cuentas activas: " + e.getMessage());
		}

		return cuentas;
	}

	@Override
	public ArrayList<Cuenta> ListarPorIdCliente(int idCliente, String busqueda) {
		ArrayList<Cuenta> cuentas = new ArrayList<>();

		StringBuilder queryBuilder = new StringBuilder(
				"SELECT c.*, tc.descripcion, cl.*, loc.id, loc.nombre AS localidad, prov.id, prov.nombre AS provincia "
						+ "FROM cuenta c " + "INNER JOIN tiposcuenta tc ON c.idTipoCuenta = tc.idTipoCuenta "
						+ "INNER JOIN cliente cl ON c.idCliente = cl.idCliente "
						+ "INNER JOIN localidades loc ON cl.idLocalidad = loc.id "
						+ "INNER JOIN provincias prov ON cl.idProvincia = prov.id "
						+ "WHERE c.idCliente = ? AND c.activo = 1");

		if (busqueda != null && !busqueda.isEmpty()) {
			// Si busqueda no es nulo ni vacío, agregamos las condiciones LIKE
			queryBuilder.append(" AND (c.CBU LIKE ? OR c.numero = ? OR cl.idCliente = ? OR tc.descripcion LIKE ?)");
		}

		try (PreparedStatement statement = conexion.prepareStatement(queryBuilder.toString())) {
			statement.setInt(1, idCliente);
			if (busqueda != null && !busqueda.isEmpty()) {
				// Si busqueda no es nulo ni vacío, establecemos los parámetros
				statement.setString(2, "%" + busqueda + "%");
				statement.setString(3, busqueda);
				statement.setString(4, busqueda);
				statement.setString(5, "%" + busqueda + "%");
			}

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Cuenta cuenta = new Cuenta();
					cuenta.setNumero(resultSet.getInt("numero"));
					cuenta.setCBU(resultSet.getString("CBU"));
					cuenta.setSaldo(resultSet.getDouble("saldo"));
					cuenta.setFecha(resultSet.getDate("fecha").toLocalDate());
					cuenta.setActivo(resultSet.getInt("activo"));

					TipoCuenta tipoCuenta = new TipoCuenta();
					tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuenta"));
					tipoCuenta.setDescripcion(resultSet.getString("descripcion"));

					cuenta.setTipoCuenta(tipoCuenta);

					Cliente cliente = new Cliente();
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

					Localidad localidad = new Localidad();
					localidad.setId(resultSet.getInt("idLocalidad"));
					localidad.setNombre(resultSet.getString("localidad"));

					Provincia provincia = new Provincia();
					provincia.setId(resultSet.getInt("idProvincia"));
					provincia.setNombre(resultSet.getString("provincia"));

					cliente.setLocalidad(localidad);
					cliente.setProvincia(provincia);
					cliente.setCorreo(resultSet.getString("correo"));

					cuenta.setCliente(cliente);
					cuentas.add(cuenta);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al listar cuentas por idCliente: " + e.getMessage());
		}
		return cuentas;
	}

	@Override
	public int ModificarCuenta(Cuenta cuenta) {
		int resultado = 0;

		try (PreparedStatement statement = conexion
				.prepareStatement("UPDATE cuenta SET saldo = ?, idTipoCuenta = ? WHERE numero = ?")) {
			statement.setDouble(1, cuenta.getSaldo());
			statement.setInt(2, cuenta.getTipoCuenta().getIdTipoCuenta());
			statement.setInt(3, cuenta.getNumero());

			resultado = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al modificar la cuenta: " + e.getMessage());
		}

		return resultado;
	}

	@Override
	public int EliminarCuenta(int numero) {
		int resultado = 0;

		try (PreparedStatement statement = conexion.prepareStatement("UPDATE cuenta SET activo = 0 WHERE numero = ?")) {
			statement.setInt(1, numero);
			resultado = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al realizar la baja lógica de la cuenta: " + e.getMessage());
		}

		return resultado;
	}

	@Override
	public int CantidadCuentasCliente(int idCliente) {
		int cantidadCuentas = 0;

		try (PreparedStatement statement = conexion
				.prepareStatement("SELECT COUNT(*) AS cantidad FROM cuenta WHERE idCliente = ? and activo = 1")) {
			statement.setInt(1, idCliente);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					cantidadCuentas = resultSet.getInt("cantidad");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener la cantidad de cuentas del cliente: " + e.getMessage());
		}

		return cantidadCuentas;
	}

	private String generarCBUUnico() {
		String cbu = "";
		boolean cbuUnico = false;

		while (!cbuUnico) {
			// Generar un CBU único de 22 dígitos aleatorios
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 22; i++) {
				int randomDigit = (int) (Math.random() * 10);
				sb.append(randomDigit);
			}
			cbu = sb.toString();

			// Verificar si el CBU generado es único
			if (!existeCBU(cbu)) {
				cbuUnico = true;
			}
		}

		return cbu;
	}

	// Método para verificar si un CBU ya existe en la base de datos
	private boolean existeCBU(String cbu) {
		try (PreparedStatement statement = conexion
				.prepareStatement("SELECT COUNT(*) AS count FROM cuenta WHERE CBU = ?")) {
			statement.setString(1, cbu);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt("count");
					return count > 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al verificar la existencia del CBU: " + e.getMessage());
		}

		return false;
	}

	@Override
	public Cuenta ObtenerPorCbu(String cbu) {
	    Cuenta cuenta = null;

	    try (PreparedStatement statement = conexion.prepareStatement(
	            "SELECT c.*, tc.*, cl.contraseña, cl.usuario, cl.dni, cl.cuil, cl.nombre, cl.apellido, " +
	            "cl.sexo AS sexoCliente, cl.nacionalidad, cl.fechaNacimiento, cl.direccion, " +
	            "loc.id AS idLocalidad, loc.nombre AS localidad, " +
	            "prov.id AS idProvincia, prov.nombre AS provincia, cl.correo " +
	            "FROM cuenta c " +
	            "INNER JOIN tiposcuenta tc ON c.idTipoCuenta = tc.idTipoCuenta " +
	            "INNER JOIN cliente cl ON c.idCliente = cl.idCliente " +
	            "INNER JOIN localidades loc ON cl.idLocalidad = loc.id " +
	            "INNER JOIN provincias prov ON cl.idProvincia = prov.id " +
	            "WHERE c.CBU = ? AND c.activo = 1")) {
	        statement.setString(1, cbu);

	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                cuenta = new Cuenta();
	                cuenta.setNumero(resultSet.getInt("numero"));
	                cuenta.setCBU(resultSet.getString("CBU"));
	                cuenta.setSaldo(resultSet.getDouble("saldo"));
	                cuenta.setFecha(resultSet.getDate("fecha").toLocalDate());
	                cuenta.setActivo(resultSet.getInt("activo"));

	                TipoCuenta tipoCuenta = new TipoCuenta();
	                tipoCuenta.setIdTipoCuenta(resultSet.getInt("tc.idTipoCuenta"));
	                tipoCuenta.setDescripcion(resultSet.getString("tc.descripcion"));

	                cuenta.setTipoCuenta(tipoCuenta);

	                Cliente cliente = new Cliente();
	                cliente.setIdCliente(resultSet.getInt("idCliente"));
	                cliente.setUsuario(resultSet.getString("usuario"));
	                cliente.setContrasena(resultSet.getString("contraseña"));
	                cliente.setActivo(resultSet.getInt("activo"));
	                cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
	                cliente.setTipoCliente(resultSet.getInt("idTipo")); // Obtener el tipo de cliente de la columna idTipo
	                cliente.setDni(resultSet.getInt("dni"));
	                cliente.setCuil(resultSet.getString("cuil"));
	                cliente.setNombre(resultSet.getString("nombre"));
	                cliente.setApellido(resultSet.getString("apellido"));
	                cliente.setSexo(resultSet.getInt("sexoCliente"));
	                cliente.setNacionalidad(resultSet.getString("nacionalidad"));
	                cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
	                cliente.setDireccion(resultSet.getString("direccion"));

	                Localidad localidad = new Localidad();
	                localidad.setId(resultSet.getInt("idLocalidad"));
	                localidad.setIdProvincia(resultSet.getInt("idProvincia"));
	                localidad.setNombre(resultSet.getString("localidad"));

	                Provincia provincia = new Provincia();
	                provincia.setId(resultSet.getInt("idProvincia"));
	                provincia.setNombre(resultSet.getString("provincia"));

	                cliente.setLocalidad(localidad);
	                cliente.setProvincia(provincia);
	                cliente.setCorreo(resultSet.getString("correo"));

	                cuenta.setCliente(cliente);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al obtener la cuenta por CBU: " + e.getMessage());
	    }

	    return cuenta;
	}

	@Override
	public int SumarSaldo(int numeroCuenta, double saldoASumar) { // ya comprueba si no da saldo negativo.
		int filasActualizadas = 0;

		try (PreparedStatement statement = conexion.prepareStatement("SELECT saldo FROM cuenta WHERE numero = ?")) {
			statement.setInt(1, numeroCuenta);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				double saldoActual = resultSet.getDouble("saldo");
				double nuevoSaldo = saldoActual + saldoASumar;

				// Verificar que el nuevo saldo no sea negativo
				if (nuevoSaldo >= 0) {
					try (PreparedStatement updateStatement = conexion
							.prepareStatement("UPDATE cuenta SET saldo = ? WHERE numero = ?")) {
						updateStatement.setDouble(1, nuevoSaldo);
						updateStatement.setInt(2, numeroCuenta);

						filasActualizadas = updateStatement.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al sumar saldo a la cuenta: " + e.getMessage());
		}

		return filasActualizadas;
	}

}