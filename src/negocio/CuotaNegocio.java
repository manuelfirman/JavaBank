package negocio;

import java.util.ArrayList;

import dao.CuotaDao;
import entidad.Cuota;
import entidad.Prestamo;
import interfazNegocio.ICuotaNegocio;

public class CuotaNegocio implements ICuotaNegocio {
	CuotaDao dao = new CuotaDao();

	@Override
	public boolean GenerarCuotas(Prestamo prestamo) {
		return dao.GenerarCuotas(prestamo) == 0 ? false : true;
	}

	@Override
	public boolean PagarCuota(int idCuota) {
		return dao.PagarCuota(idCuota) == 0 ? false : true;
	}

	@Override
	public Cuota obtenerCuotaPorNumeroYID(int numeroCuota) {
		return dao.obtenerCuotaPorNumeroYID(numeroCuota);
	}

	@Override
	public ArrayList<Cuota> ListarPorIdPrestamo(int idPrestamo) {
		return dao.ListarPorIdPrestamo(idPrestamo);
	}

	@Override
	public ArrayList<Cuota> ListarPorClienteAprobadoEImpago(int idCliente) {
		return dao.ListarPorClienteAprobadoEImpago(idCliente);
	}

}
