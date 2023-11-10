package interfazNegocio;

import java.util.ArrayList;

import entidad.Prestamo;
import entidad.Prestamo.Estado;

public interface IPrestamoNegocio {
	public ArrayList<Prestamo> ListarTodos();
	public ArrayList<Prestamo> ListarPendientes(String busqueda);
	public boolean PedirPrestamo(Prestamo prestamo);
	public boolean CambiarEstadoPrestamo(int idPrestamo, Estado estado);
	public Prestamo ObtenerPrestamoPorId(int idPrestamo);
	public ArrayList<Prestamo> ListarPorClienteAprobados(int idCliente);
}
