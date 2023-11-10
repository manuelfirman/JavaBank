package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import entidad.Cliente;
import entidad.Localidad;
import entidad.Provincia;
import excepciones.CorreoExistenteException;
import excepciones.CuilExistenteException;
import excepciones.DNIExistenteException;
import excepciones.UsuarioExistenteException;
import interfazDao.IClienteDao;

public class ClienteDao implements IClienteDao {
    private Connection conexion;

    public ClienteDao() {
        conexion = Conexion.obtenerConexion();
    }

    @Override
    public Cliente Login(String usuario, String contrasena) {
        // Implementación del método Login
        String consulta = "SELECT c.*, l.ID AS localidadID, l.Nombre AS localidadNombre, p.ID AS provinciaID, p.Nombre AS provinciaNombre " +
                "FROM CLIENTE c " +
                "JOIN LOCALIDADES l ON c.idLocalidad = l.ID " +
                "JOIN PROVINCIAS p ON c.idProvincia = p.ID " +
                "WHERE c.usuario = ? AND c.contraseña = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, usuario);
            statement.setString(2, contrasena);

            try (ResultSet resultSet = statement.executeQuery()) {
                Cliente cliente = null;
                while (resultSet.next()) {
                    if (cliente == null) {
                        cliente = new Cliente();
                        cliente.setIdCliente(resultSet.getInt("idCliente"));
                        cliente.setUsuario(resultSet.getString("usuario"));
                        cliente.setContrasena(resultSet.getString("contraseña"));
                        cliente.setActivo(resultSet.getInt("activo"));
                        cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
                        cliente.setTipoCliente(resultSet.getInt("idTipo"));
                        cliente.setDni(resultSet.getInt("dni"));
                        cliente.setCuil(resultSet.getString("cuil"));
                        cliente.setNombre(resultSet.getString("nombre"));
                        cliente.setApellido(resultSet.getString("apellido"));
                        cliente.setSexo(resultSet.getInt("sexo"));
                        cliente.setNacionalidad(resultSet.getString("nacionalidad"));
                        cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
                        cliente.setDireccion(resultSet.getString("direccion"));
                        cliente.setCorreo(resultSet.getString("correo"));
                        cliente.setLocalidad(new Localidad(resultSet.getInt("localidadID"), resultSet.getInt("provinciaID"), resultSet.getString("localidadNombre")));
                        cliente.setProvincia(new Provincia(resultSet.getInt("provinciaID"), resultSet.getString("provinciaNombre")));
                    }
                }
                return cliente;
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta de login: " + e.getMessage());
        }

        return null;
    }

    @Override
    public int Agregar(Cliente cliente) {
        // Implementación del método AgregarCliente
        String consulta = "INSERT INTO CLIENTE (usuario, contraseña, activo, fechaCreacion, idTipo, dni, cuil, nombre, apellido, sexo, nacionalidad, fechaNacimiento, direccion, idLocalidad, idProvincia, correo) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = conexion.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, cliente.getUsuario());
            statement.setString(2, cliente.getContrasena());
            statement.setInt(3, cliente.getActivo());
            // Asignar la fecha actual directamente
            statement.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            statement.setInt(5, cliente.getTipoCliente().ordinal());
            statement.setInt(6, cliente.getDni());
            statement.setString(7, cliente.getCuil());
            statement.setString(8, cliente.getNombre());
            statement.setString(9, cliente.getApellido());
            statement.setInt(10, cliente.getSexo().ordinal());
            statement.setString(11, cliente.getNacionalidad());
            statement.setDate(12, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
            statement.setString(13, cliente.getDireccion());
            statement.setInt(14, cliente.getLocalidad().getId());
            statement.setInt(15, cliente.getProvincia().getId());
            statement.setString(16, cliente.getCorreo());

            int filasInsertadas = statement.executeUpdate();

            if (filasInsertadas > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar un cliente: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public int ModificarCliente(Cliente cliente) {
        // Implementación del método ModificarCliente
        String consulta = "UPDATE CLIENTE " +
                "SET usuario = ?, contraseña = ?, activo = ?, fechaCreacion = ?, idTipo = ?, dni = ?, cuil = ?, nombre = ?, apellido = ?, " +
                "sexo = ?, nacionalidad = ?, fechaNacimiento = ?, direccion = ?, idLocalidad = ?, idProvincia = ?, correo = ? " +
                "WHERE idCliente = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, cliente.getUsuario());
            statement.setString(2, cliente.getContrasena());
            statement.setInt(3, cliente.getActivo());
            statement.setDate(4, java.sql.Date.valueOf(cliente.getFechaCreacion()));
            statement.setInt(5, cliente.getTipoCliente().ordinal());
            statement.setInt(6, cliente.getDni());
            statement.setString(7, cliente.getCuil());
            statement.setString(8, cliente.getNombre());
            statement.setString(9, cliente.getApellido());
            statement.setInt(10, cliente.getSexo().ordinal());
            statement.setString(11, cliente.getNacionalidad());
            statement.setDate(12, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
            statement.setString(13, cliente.getDireccion());
            statement.setInt(14, cliente.getLocalidad().getId());
            statement.setInt(15, cliente.getProvincia().getId());
            statement.setString(16, cliente.getCorreo());
            statement.setInt(17, cliente.getIdCliente());

            int filasActualizadas = statement.executeUpdate();

            return filasActualizadas;
        } catch (SQLException e) {
            System.err.println("Error al modificar cliente: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public int EliminarCliente(int idCliente) {
        // Implementación del método EliminarCliente
        String consulta = "UPDATE CLIENTE SET activo = 0 WHERE idCliente = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setInt(1, idCliente);
            int filasActualizadas = statement.executeUpdate();

            return filasActualizadas;
        } catch (SQLException e) {
            System.err.println("Error al realizar la baja lógica de un cliente: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public ArrayList<Cliente> ListarClientesActivos(String busqueda) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String consultaClientes = "SELECT c.*, l.ID AS localidadID, l.Nombre AS localidadNombre, p.ID AS provinciaID, p.Nombre AS provinciaNombre\r\n" + 
        		"FROM CLIENTE c\r\n" + 
        		"JOIN LOCALIDADES l ON c.idLocalidad = l.ID\r\n" + 
        		"JOIN PROVINCIAS p ON c.idProvincia = p.ID\r\n" + 
        		"WHERE c.activo = 1 AND c.idTipo = 0"; // Agregar la condición para el tipo igual a 0 (no admin)

        if (busqueda != null && !busqueda.isEmpty()) {
            // Agregar el filtro con LIKE a la consulta
            consultaClientes += " AND (c.usuario LIKE ? OR c.nombre LIKE ? OR c.apellido LIKE ? " +
                "OR c.nacionalidad LIKE ? OR c.direccion LIKE ? OR c.correo LIKE ?)";
        }

        try (PreparedStatement statement = conexion.prepareStatement(consultaClientes)) {
            if (busqueda != null && !busqueda.isEmpty()) {
                // Configurar los parámetros del filtro con LIKE
                String busquedaParam = "%" + busqueda + "%";
                statement.setString(1, busquedaParam);
                statement.setString(2, busquedaParam);
                statement.setString(3, busquedaParam);
                statement.setString(4, busquedaParam);
                statement.setString(5, busquedaParam);
                statement.setString(6, busquedaParam);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(resultSet.getInt("idCliente"));
                    cliente.setUsuario(resultSet.getString("usuario"));
                    cliente.setContrasena(resultSet.getString("contraseña"));
                    cliente.setActivo(resultSet.getInt("activo"));
                    cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
                    cliente.setTipoCliente(resultSet.getInt("idTipo"));
                    cliente.setDni(resultSet.getInt("dni"));
                    cliente.setCuil(resultSet.getString("cuil"));
                    cliente.setNombre(resultSet.getString("nombre"));
                    cliente.setApellido(resultSet.getString("apellido"));
                    cliente.setSexo(resultSet.getInt("sexo"));
                    cliente.setNacionalidad(resultSet.getString("nacionalidad"));
                    cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
                    cliente.setDireccion(resultSet.getString("direccion"));
                    cliente.setCorreo(resultSet.getString("correo"));
                    cliente.setLocalidad(new Localidad(resultSet.getInt("localidadID"), resultSet.getInt("provinciaID"), resultSet.getString("localidadNombre")));
                    cliente.setProvincia(new Provincia(resultSet.getInt("provinciaID"), resultSet.getString("provinciaNombre")));
                    clientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes activos: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public Cliente ObtenerPorIdCliente(int idCliente) {
        String consulta = "SELECT c.*, l.ID AS localidadID, l.Nombre AS localidadNombre, p.ID AS provinciaID, p.Nombre AS provinciaNombre " +
                "FROM CLIENTE c " +
                "JOIN LOCALIDADES l ON c.idLocalidad = l.ID " +
                "JOIN PROVINCIAS p ON c.idProvincia = p.ID " +
                "WHERE c.idCliente = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setInt(1, idCliente);

            try (ResultSet resultSet = statement.executeQuery()) {
                Cliente cliente = null;
                while (resultSet.next()) {
                    if (cliente == null) {
                        cliente = new Cliente();
                        cliente.setIdCliente(resultSet.getInt("idCliente"));
                        cliente.setUsuario(resultSet.getString("usuario"));
                        cliente.setContrasena(resultSet.getString("contraseña"));
                        cliente.setActivo(resultSet.getInt("activo"));
                        cliente.setFechaCreacion(resultSet.getDate("fechaCreacion").toLocalDate());
                        cliente.setTipoCliente(resultSet.getInt("idTipo"));
                        cliente.setDni(resultSet.getInt("dni"));
                        cliente.setCuil(resultSet.getString("cuil"));
                        cliente.setNombre(resultSet.getString("nombre"));
                        cliente.setApellido(resultSet.getString("apellido"));
                        cliente.setSexo(resultSet.getInt("sexo"));
                        cliente.setNacionalidad(resultSet.getString("nacionalidad"));
                        cliente.setFechaNacimiento(resultSet.getDate("fechaNacimiento").toLocalDate());
                        cliente.setDireccion(resultSet.getString("direccion"));
                        cliente.setCorreo(resultSet.getString("correo"));
                        cliente.setLocalidad(new Localidad(resultSet.getInt("localidadID"), resultSet.getInt("provinciaID"), resultSet.getString("localidadNombre")));
                        cliente.setProvincia(new Provincia(resultSet.getInt("provinciaID"), resultSet.getString("provinciaNombre")));
                    }
                }
                return cliente;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente por ID: " + e.getMessage());
        }

        return null;
    }


    @Override
    public int UsuarioUnico(String usuario) {
        String consulta = "SELECT COUNT(*) AS count FROM CLIENTE WHERE usuario = ? AND activo = 1";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, usuario);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if(resultSet.getInt("count") > 0) {
                    	throw new UsuarioExistenteException();
                    }
                    
                    return 1; // Devuelve 0 si el usuario existe y está activo, 1 si es único o inactivo.
                }
            }
        } catch (UsuarioExistenteException ex) {
        	System.err.println(ex.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al verificar si el usuario es único y activo: " + e.getMessage());
        }

        throw new UsuarioExistenteException();
    }
    
    @Override
    public int DniUnico(int dni, int idCliente) {
        String consulta = "SELECT COUNT(*) AS count FROM CLIENTE WHERE dni = ? AND activo = 1 AND idCliente <> ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setInt(1, dni);
            statement.setInt(2, idCliente);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	// Exception si el DNI existe y está activo, 1 si es único o inactivo.
                    if(resultSet.getInt("count") > 0) {
                    	throw new DNIExistenteException();
                    }
                    
                    return 1;
                }
            }
        } catch(DNIExistenteException ex) {
        	System.err.println(ex.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al verificar si el DNI es único y activo: " + e.getMessage());
        }

        throw new DNIExistenteException(); // En caso de error, Exception
    }
    
    @Override
    public int CuilUnico(String cuil, int idCliente) {
        String consulta = "SELECT COUNT(*) AS count FROM CLIENTE WHERE cuil = ? AND activo = 1 AND idCliente <> ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, cuil);
            statement.setInt(2, idCliente);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if(resultSet.getInt("count") > 0) {
                    	throw new CuilExistenteException();
                    } 
                    
                    return 1;
                }
            }
            
        } catch(CuilExistenteException ex) {
        	System.err.println(ex.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al verificar si el CUIL es único y activo: " + e.getMessage());
        }

        throw new CuilExistenteException();
    }
    
    @Override
    public int CorreoUnico(String correo, int idCliente) {
        String consulta = "SELECT COUNT(*) AS count FROM CLIENTE WHERE correo = ? AND activo = 1 AND idCliente <> ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, correo);
            statement.setInt(2, idCliente);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if(resultSet.getInt("count") > 0){
                    	throw new CorreoExistenteException();
                    }
                    
                    return 1; // Devuelve Exception si el correo existe y está activo, 1 si es único o inactivo.
                }
            }
        } catch (CorreoExistenteException ex) {
        	System.err.println(ex.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al verificar si el correo es único y activo: " + e.getMessage());
        }

        throw new CorreoExistenteException();
    }

}

