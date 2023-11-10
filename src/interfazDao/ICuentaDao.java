package interfazDao;

import java.util.ArrayList;

import entidad.Cuenta;


public interface ICuentaDao {
	public int Agregar(Cuenta cuenta);
	public int SumarSaldo(int numeroCuenta, double saldo);
	public ArrayList<Cuenta> ListarCuentasActivas(String busqueda);
	public ArrayList<Cuenta> ListarPorIdCliente(int idCliente, String busqueda);
	public int ModificarCuenta(Cuenta cuenta);
	public int EliminarCuenta(int idCuenta);
	public int CantidadCuentasCliente(int idCliente);
	public Cuenta ObtenerPorNumeroCuenta(int numero);
	public Cuenta ObtenerPorCbu(String cbu);
}