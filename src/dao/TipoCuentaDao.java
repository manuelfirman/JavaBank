package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import entidad.TipoCuenta;
import interfazDao.ITipoCuentaDao;

public class TipoCuentaDao implements ITipoCuentaDao {
    private Connection conexion;

    public TipoCuentaDao() {
        conexion = Conexion.obtenerConexion();
    }

    @Override
    public ArrayList<TipoCuenta> ListarTipoCuenta() {
        ArrayList<TipoCuenta> tiposCuenta = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM tiposcuenta";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TipoCuenta tipoCuenta = new TipoCuenta();
                tipoCuenta.setIdTipoCuenta(resultSet.getInt("idTipoCuenta"));
                tipoCuenta.setDescripcion(resultSet.getString("descripcion"));

                tiposCuenta.add(tipoCuenta);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de cuenta: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el ResultSet: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                }
            }
        }

        return tiposCuenta;
    }
}