package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entidad.Localidad;
import interfazDao.ILocalidadDao;

public class LocalidadDao implements ILocalidadDao {
	private Connection conexion;

    public LocalidadDao() {
		conexion = Conexion.obtenerConexion();
	}

    @Override
    public ArrayList<Localidad> ListarLocalidades() {
        ArrayList<Localidad> localidades = new ArrayList<>();

        String consulta = "SELECT id, idProvincia, nombre FROM localidades";
        try (PreparedStatement statement = conexion.prepareStatement(consulta);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Localidad localidad = new Localidad();
                localidad.setId(resultSet.getInt("id"));
                localidad.setIdProvincia(resultSet.getInt("idProvincia"));
                localidad.setNombre(resultSet.getString("nombre"));
                localidades.add(localidad);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar localidades: " + e.getMessage());
        }

        return localidades;
    }
    
    
}