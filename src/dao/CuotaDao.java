package dao;

import entidad.Cliente;
import entidad.Cuenta;
import entidad.Cuota;
import entidad.Localidad;
import entidad.Prestamo;
import entidad.Provincia;
import entidad.TipoCuenta;
import interfazDao.ICuotaDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CuotaDao implements ICuotaDao {
	private Connection conexion;

	public CuotaDao() {
		conexion = Conexion.obtenerConexion();
	}

	@Override
	public int GenerarCuotas(Prestamo prestamo) {
	    int idPrestamo = prestamo.getIdPrestamo();
	    int cantidadCuotas = prestamo.getCuotas();
	    double importePorMes = prestamo.getImportePorMes();

	    String query = "INSERT INTO cuota (nCuota, idPrestamo, importe, fechaPago, estado) VALUES (?, ?, ?, NULL, 0)";

	    try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
	        for (int nCuota = 1; nCuota <= cantidadCuotas; nCuota++) {
	            pstmt.setInt(1, nCuota); // Agregar el número de cuota
	            pstmt.setInt(2, idPrestamo);
	            pstmt.setDouble(3, importePorMes);
	            pstmt.addBatch(); // Agregar la sentencia a un lote para mejorar el rendimiento
	        }

	        int[] resultados = pstmt.executeBatch(); // Ejecutar todas las inserciones

	        for (int resultado : resultados) {
	            if (resultado == PreparedStatement.EXECUTE_FAILED) {
	                // Manejar error
	                return 0;
	            }
	        }
	        return resultados.length;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

	public int PagarCuota(int idCuota, int idPrestamo) {
	    String query = "UPDATE cuota SET fechaPago = CURRENT_DATE, estado = ? WHERE idCuota = ? AND idPrestamo = ?";

	    try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
	        pstmt.setInt(1, 1); // 1 representa el estado PAGO en la base de datos
	        pstmt.setInt(2, idCuota);
	        pstmt.setInt(3, idPrestamo);

	        return pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}


	@Override
	public Cuota obtenerCuotaPorNumeroYID(int idCouta) {
		String query = "SELECT cuota.*, prestamo.*, cuenta.*, cliente.*, tiposcuenta.*, localidades.*, provincias.* " +
		            "FROM cuota " +
		            "JOIN prestamo ON cuota.idPrestamo = prestamo.idPrestamo " +
		            "JOIN cuenta ON prestamo.numeroCuenta = cuenta.numero " +
		            "JOIN cliente ON cuenta.idCliente = cliente.idCliente " +
		            "JOIN tiposcuenta ON cuenta.idTipoCuenta = tiposcuenta.idTipoCuenta " +
		            "JOIN localidades ON cliente.idLocalidad = localidades.id " +
		            "JOIN provincias ON cliente.idProvincia = provincias.id " +
		            "WHERE cuota.idCuota = ?";

	    try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
	        pstmt.setInt(1, idCouta);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                Cuota cuota = new Cuota();
	                Prestamo prestamo = new Prestamo();
	                Cuenta cuenta = new Cuenta();
	                Cliente cliente = new Cliente();
	                TipoCuenta tipoCuenta = new TipoCuenta();
	                Localidad localidad = new Localidad();
	                Provincia provincia = new Provincia();
	                
	                prestamo.setIdPrestamo(rs.getInt("idPrestamo"));
	                prestamo.setImportePedido(rs.getDouble("importePedido"));
	                prestamo.setImportePorMes(rs.getDouble("importexmes"));
	                prestamo.setCuotas(rs.getInt("cuotas"));
	                prestamo.setFechaPedido(rs.getDate("fechaPedido").toLocalDate());
	                prestamo.setEstado((rs.getInt("prestamoEstado") == 1) ? Prestamo.Estado.APROBADO : Prestamo.Estado.PENDIENTE);

	                cuota.setIdCuota(rs.getInt("idCuota"));
	                cuota.setNumeroCuota(rs.getInt("nCuota"));
	                cuota.setImporte(rs.getDouble("importe"));
	                cuota.setFechaPago((rs.getDate("fechaPago") != null) ? rs.getDate("fechaPago").toLocalDate() : null);
	                cuota.setEstado((rs.getInt("estado") == 1) ? Cuota.Estado.PAGO : Cuota.Estado.IMPAGO);

	                prestamo.setIdPrestamo(rs.getInt("prestamo.idPrestamo"));
	                cuota.setPrestamo(prestamo);

	                cuenta.setNumero(rs.getInt("cuenta.numero"));
	                cuenta.setCBU(rs.getString("cuenta.CBU"));
	                cuenta.setSaldo(rs.getDouble("cuenta.saldo"));
	                cuenta.setFecha(rs.getDate("cuenta.fecha").toLocalDate());
	                cuenta.setActivo(rs.getInt("cuenta.activo"));

	                tipoCuenta.setIdTipoCuenta(rs.getInt("idtipocuenta"));
	                tipoCuenta.setDescripcion(rs.getString("tiposcuenta.descripcion"));
	                cuenta.setTipoCuenta(tipoCuenta);

	                cliente.setIdCliente(rs.getInt("cliente.idCliente"));
	                cliente.setUsuario(rs.getString("cliente.usuario"));
	                cliente.setContrasena(rs.getString("cliente.contraseña"));
	                cliente.setActivo(rs.getInt("cliente.activo"));
	                cliente.setFechaCreacion(rs.getDate("cliente.fechaCreacion").toLocalDate());
	                cliente.setTipoCliente(Cliente.TipoCliente.values()[rs.getInt("cliente.idTipo")]);
	                cliente.setDni(rs.getInt("cliente.dni"));
	                cliente.setCuil(rs.getString("cliente.cuil"));
	                cliente.setNombre(rs.getString("cliente.nombre"));
	                cliente.setApellido(rs.getString("cliente.apellido"));
	                cliente.setSexo(Cliente.Sexo.values()[rs.getInt("cliente.sexo")]);
	                cliente.setNacionalidad(rs.getString("cliente.nacionalidad"));
	                cliente.setFechaNacimiento(rs.getDate("cliente.fechaNacimiento").toLocalDate());
	                cliente.setDireccion(rs.getString("cliente.direccion"));
	                cliente.setCorreo(rs.getString("cliente.correo"));

	                localidad.setId(rs.getInt("localidades.id"));
	                localidad.setIdProvincia(rs.getInt("localidades.idProvincia"));
	                localidad.setNombre(rs.getString("localidades.nombre"));
	                cliente.setLocalidad(localidad);

	                provincia.setId(rs.getInt("provincias.id"));
	                provincia.setNombre(rs.getString("provincias.nombre"));
	                cliente.setProvincia(provincia);

	                cuenta.setCliente(cliente);
	                prestamo.setCuenta(cuenta);
	                cuota.setPrestamo(prestamo);

	                return cuota;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}



	@Override
	public ArrayList<Cuota> ListarPorIdPrestamo(int idPrestamo) {
	    ArrayList<Cuota> cuotas = new ArrayList<>();
	    String query = "SELECT cuota.*, prestamo.*, cuenta.*, cliente.*, tiposcuenta.*, localidades.*, provincias.* " +
	            "FROM cuota " +
	            "JOIN prestamo ON cuota.idPrestamo = prestamo.idPrestamo " +
	            "JOIN cuenta ON prestamo.numeroCuenta = cuenta.numero " +
	            "JOIN cliente ON cuenta.idCliente = cliente.idCliente " +
	            "JOIN tiposcuenta ON cuenta.idTipoCuenta = tiposcuenta.idTipoCuenta " +
	            "JOIN localidades ON cliente.idLocalidad = localidades.id " +
	            "JOIN provincias ON cliente.idProvincia = provincias.id " +
	            "WHERE cuota.idPrestamo = ?";

	    try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
	        pstmt.setInt(1, idPrestamo);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Cuota cuota = new Cuota();
	                Prestamo prestamo = new Prestamo();
	                Cuenta cuenta = new Cuenta();
	                Cliente cliente = new Cliente();
	                TipoCuenta tipoCuenta = new TipoCuenta();
	                Localidad localidad = new Localidad();
	                Provincia provincia = new Provincia();

	                cuota.setNumeroCuota(rs.getInt("nCuota"));
	         
	                cuota.setImporte(rs.getDouble("importe"));
	                LocalDate fechaPago = (rs.getDate("fechaPago") != null) ? rs.getDate("fechaPago").toLocalDate() : null;
	                cuota.setFechaPago(fechaPago);
	                int estado = rs.getInt("estado");
	                cuota.setEstado((estado == 1) ? Cuota.Estado.PAGO : Cuota.Estado.IMPAGO);

	                prestamo.setIdPrestamo(rs.getInt("prestamo.idPrestamo"));
	                prestamo.setImportePedido(rs.getDouble("importePedido"));
	                prestamo.setImportePorMes(rs.getDouble("importexmes"));
	                prestamo.setCuotas(rs.getInt("cuotas"));
	                prestamo.setFechaPedido(rs.getDate("fechaPedido").toLocalDate());

	                cuota.setPrestamo(prestamo);
	                
	                cuota.setIdCuota(rs.getInt("idCuota"));
	                cuenta.setNumero(rs.getInt("cuenta.numero"));
	                cuenta.setCBU(rs.getString("cuenta.CBU"));
	                cuenta.setSaldo(rs.getDouble("cuenta.saldo"));
	                cuenta.setFecha(rs.getDate("cuenta.fecha").toLocalDate());
	                cuenta.setActivo(rs.getInt("cuenta.activo"));

	                tipoCuenta.setIdTipoCuenta(rs.getInt("idtipocuenta"));
	                tipoCuenta.setDescripcion(rs.getString("tiposcuenta.descripcion"));
	                cuenta.setTipoCuenta(tipoCuenta);

	                cliente.setIdCliente(rs.getInt("cliente.idCliente"));
	                cliente.setUsuario(rs.getString("cliente.usuario"));
	                cliente.setContrasena(rs.getString("cliente.contraseña"));
	                cliente.setActivo(rs.getInt("cliente.activo"));
	                cliente.setFechaCreacion(rs.getDate("cliente.fechaCreacion").toLocalDate());
	                cliente.setTipoCliente(Cliente.TipoCliente.values()[rs.getInt("cliente.idTipo")]);
	                cliente.setDni(rs.getInt("cliente.dni"));
	                cliente.setCuil(rs.getString("cliente.cuil"));
	                cliente.setNombre(rs.getString("cliente.nombre"));
	                cliente.setApellido(rs.getString("cliente.apellido"));
	                cliente.setSexo(Cliente.Sexo.values()[rs.getInt("cliente.sexo")]);
	                cliente.setNacionalidad(rs.getString("cliente.nacionalidad"));
	                cliente.setFechaNacimiento(rs.getDate("cliente.fechaNacimiento").toLocalDate());
	                cliente.setDireccion(rs.getString("cliente.direccion"));
	                cliente.setCorreo(rs.getString("cliente.correo"));

	                localidad.setId(rs.getInt("localidades.id"));
	                localidad.setIdProvincia(rs.getInt("localidades.idProvincia"));
	                localidad.setNombre(rs.getString("localidades.nombre"));
	                cliente.setLocalidad(localidad);

	                provincia.setId(rs.getInt("provincias.id"));
	                provincia.setNombre(rs.getString("provincias.nombre"));
	                cliente.setProvincia(provincia);

	                cuenta.setCliente(cliente);
	                prestamo.setCuenta(cuenta);
	                cuota.setPrestamo(prestamo);

	                cuotas.add(cuota);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return cuotas;
	}


	@Override
	public ArrayList<Cuota> ListarPorClienteAprobadoEImpago(int idCliente) {
	    ArrayList<Cuota> cuotas = new ArrayList<>();
	    String query = "SELECT cuota.*, prestamo.*, cuenta.*, cliente.*, tiposcuenta.*, localidades.*, provincias.* " +
	            "FROM cuota " +
	            "JOIN prestamo ON cuota.idPrestamo = prestamo.idPrestamo " +
	            "JOIN cuenta ON prestamo.numeroCuenta = cuenta.numero " +
	            "JOIN cliente ON cuenta.idCliente = cliente.idCliente " +
	            "JOIN tiposcuenta ON cuenta.idTipoCuenta = tiposcuenta.idTipoCuenta " +
	            "JOIN localidades ON cliente.idLocalidad = localidades.id " +
	            "JOIN provincias ON cliente.idProvincia = provincias.id " +
	            "WHERE cliente.idCliente = ? " +
	            "AND prestamo.estado = 1 " +
	            "AND cuota.estado = 0";

	    try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
	        pstmt.setInt(1, idCliente);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Cuota cuota = new Cuota();
	                Prestamo prestamo = new Prestamo();
	                Cuenta cuenta = new Cuenta();
	                Cliente cliente = new Cliente();
	                TipoCuenta tipoCuenta = new TipoCuenta();
	                Localidad localidad = new Localidad();
	                Provincia provincia = new Provincia();
	                
	                cuota.setNumeroCuota(rs.getInt("nCuota"));
	                cuota.setImporte(rs.getDouble("importe"));
	                LocalDate fechaPago = (rs.getDate("fechaPago") != null) ? rs.getDate("fechaPago").toLocalDate() : null;
	                cuota.setFechaPago(fechaPago);
	                int estado = rs.getInt("estado");
	                cuota.setEstado((estado == 1) ? Cuota.Estado.PAGO : Cuota.Estado.IMPAGO);

	                prestamo.setIdPrestamo(rs.getInt("prestamo.idPrestamo"));
	                prestamo.setImportePedido(rs.getDouble("importePedido"));
	                prestamo.setImportePorMes(rs.getDouble("importexmes"));
	                prestamo.setCuotas(rs.getInt("cuotas"));
	                prestamo.setFechaPedido(rs.getDate("fechaPedido").toLocalDate());

	                cuota.setPrestamo(prestamo);
	                
	                cuota.setIdCuota(rs.getInt("idCuota"));
	                cuenta.setNumero(rs.getInt("cuenta.numero"));
	                cuenta.setCBU(rs.getString("cuenta.CBU"));
	                cuenta.setSaldo(rs.getDouble("cuenta.saldo"));
	                cuenta.setFecha(rs.getDate("cuenta.fecha").toLocalDate());
	                cuenta.setActivo(rs.getInt("cuenta.activo"));

	                tipoCuenta.setIdTipoCuenta(rs.getInt("idtipocuenta"));
	                tipoCuenta.setDescripcion(rs.getString("tiposcuenta.descripcion"));
	                cuenta.setTipoCuenta(tipoCuenta);

	                cliente.setIdCliente(rs.getInt("cliente.idCliente"));
	                cliente.setUsuario(rs.getString("cliente.usuario"));
	                cliente.setContrasena(rs.getString("cliente.contraseña"));
	                cliente.setActivo(rs.getInt("cliente.activo"));
	                cliente.setFechaCreacion(rs.getDate("cliente.fechaCreacion").toLocalDate());
	                cliente.setTipoCliente(Cliente.TipoCliente.values()[rs.getInt("cliente.idTipo")]);
	                cliente.setDni(rs.getInt("cliente.dni"));
	                cliente.setCuil(rs.getString("cliente.cuil"));
	                cliente.setNombre(rs.getString("cliente.nombre"));
	                cliente.setApellido(rs.getString("cliente.apellido"));
	                cliente.setSexo(Cliente.Sexo.values()[rs.getInt("cliente.sexo")]);
	                cliente.setNacionalidad(rs.getString("cliente.nacionalidad"));
	                cliente.setFechaNacimiento(rs.getDate("cliente.fechaNacimiento").toLocalDate());
	                cliente.setDireccion(rs.getString("cliente.direccion"));
	                cliente.setCorreo(rs.getString("cliente.correo"));

	                localidad.setId(rs.getInt("localidades.id"));
	                localidad.setIdProvincia(rs.getInt("localidades.idProvincia"));
	                localidad.setNombre(rs.getString("localidades.nombre"));
	                cliente.setLocalidad(localidad);

	                provincia.setId(rs.getInt("provincias.id"));
	                provincia.setNombre(rs.getString("provincias.nombre"));
	                cliente.setProvincia(provincia);

	                cuenta.setCliente(cliente);
	                prestamo.setCuenta(cuenta);
	                cuota.setPrestamo(prestamo);

	                cuotas.add(cuota);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return cuotas;
	}
	@Override
	public int PagarCuota(int idCuota) {
	    String query = "UPDATE cuota SET fechaPago = CURRENT_DATE, estado = ? WHERE idCuota = ?";

	    try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
	        pstmt.setInt(1, 1); // 1 representa el estado PAGO en la base de datos
	        pstmt.setInt(2, idCuota);

	        return pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}



}
