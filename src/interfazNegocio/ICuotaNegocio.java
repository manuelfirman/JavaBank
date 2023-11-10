package interfazNegocio;

import java.util.ArrayList;

import entidad.Cuota;
import entidad.Prestamo;

public interface ICuotaNegocio {
	public boolean GenerarCuotas(Prestamo prestamo);
	public boolean PagarCuota(int idCouta);
	public Cuota obtenerCuotaPorNumeroYID(int idCuota);
	public ArrayList<Cuota> ListarPorIdPrestamo(int idPrestamo);
	public ArrayList<Cuota> ListarPorClienteAprobadoEImpago(int idCliente);
}
