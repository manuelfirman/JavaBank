package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entidad.Cliente;
import entidad.Localidad;
import entidad.Provincia;
import entidad.Telefono;
import interfazDao.ITelefonoDao;

public class TelefonoDao implements ITelefonoDao {
    private Connection conexion;

    public TelefonoDao() {
        conexion = Conexion.obtenerConexion();
    }

    @Override
    public ArrayList<Telefono> ListarTelefonoPorIdCliente(int idCliente) {
        ArrayList<Telefono> telefonos = new ArrayList<>();
        String consulta = "SELECT t.idTelefono, t.numero, t.activo, c.*, l.Nombre AS localidad, p.Nombre AS provincia " +
                         "FROM TELEFONOS t " +
                         "LEFT JOIN CLIENTE c ON t.idCliente = c.idCliente " +
                         "LEFT JOIN localidades l ON c.idLocalidad = l.ID " +
                         "LEFT JOIN provincias p ON c.idProvincia = p.ID " +
                         "WHERE t.activo = 1";

        if (idCliente > 0) {
            consulta += " AND t.idCliente = ?";
        }

        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            if (idCliente > 0) {
                statement.setInt(1, idCliente);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Telefono telefono = new Telefono();
                    telefono.setIdTelefono(resultSet.getInt("idTelefono"));
                    telefono.setNumero(resultSet.getString("numero"));
                    telefono.setActivo(resultSet.getInt("activo"));

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
                    cliente.setCorreo(resultSet.getString("correo"));
                    
                    Localidad localidad = new Localidad();
                    localidad.setNombre(resultSet.getString("localidad"));
                    localidad.setIdProvincia(resultSet.getInt("idProvincia"));
                    cliente.setLocalidad(localidad);
                    
                    Provincia provincia = new Provincia();
                    provincia.setNombre(resultSet.getString("provincia"));
                    cliente.setProvincia(provincia);

                    telefono.setCliente(cliente);

                    telefonos.add(telefono);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar teléfonos por ID de cliente: " + e.getMessage());
        }

        return telefonos;
    }

    @Override
    public int AgregarTelefonos(int idCliente, String[] numeros) {
        String consultaDesactivar = "UPDATE TELEFONOS SET activo = 0 WHERE idCliente = ?";
        try (PreparedStatement statementDesactivar = conexion.prepareStatement(consultaDesactivar)) {
            statementDesactivar.setInt(1, idCliente);
            statementDesactivar.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al marcar como inactivos los teléfonos existentes: " + e.getMessage());
            return 0;
        }

        int filasInsertadas = 0;
        if (numeros != null && numeros.length > 0) {
            String consultaAgregar = "INSERT INTO TELEFONOS (idCliente, numero, activo) VALUES (?, ?, 1)";
            try (PreparedStatement statementAgregar = conexion.prepareStatement(consultaAgregar)) {
                for (String numero : numeros) {
                    statementAgregar.setInt(1, idCliente);
                    statementAgregar.setString(2, numero);
                    filasInsertadas += statementAgregar.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Error al agregar teléfonos nuevos: " + e.getMessage());
            }
        }

        return filasInsertadas;
    }

    @Override
    public int ModificarTelefono(int idTelefono, String nuevoNumero) {
        String consulta = "UPDATE TELEFONOS SET numero = ? WHERE idTelefono = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, nuevoNumero);
            statement.setInt(2, idTelefono);

            int filasActualizadas = statement.executeUpdate();

            return filasActualizadas;
        } catch (SQLException e) {
            System.err.println("Error al modificar un teléfono: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public int EliminarTeléfono(int idTelefono) {
        String consulta = "UPDATE TELEFONOS SET activo = 0 WHERE idTelefono = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setInt(1, idTelefono);

            int filasActualizadas = statement.executeUpdate();

            return filasActualizadas;
        } catch (SQLException e) {
            System.err.println("Error al eliminar un teléfono: " + e.getMessage());
        }

        return 0;
    }
}
