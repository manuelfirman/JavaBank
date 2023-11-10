package negocio;

import java.util.ArrayList;

import dao.PrestamoDao;
import entidad.Prestamo;
import entidad.Prestamo.Estado;
import interfazNegocio.IPrestamoNegocio;

public class PrestamoNegocio implements IPrestamoNegocio {
	PrestamoDao dao = new PrestamoDao();

	@Override
	public ArrayList<Prestamo> ListarTodos() {
		return dao.ListarTodos();
	}

	@Override
	public ArrayList<Prestamo> ListarPendientes(String busqueda) {
		// TODO Auto-generated method stub
		return dao.ListarPendientes(busqueda);
	}

	@Override
	public boolean PedirPrestamo(Prestamo prestamo) {
		return dao.PedirPrestamo(prestamo) == 0? false : true;
	}


	@Override
	public boolean CambiarEstadoPrestamo(int idPrestamo, Estado estado) {
		return dao.CambiarEstadoPrestamo(idPrestamo, estado) == 0 ? false : true;
	}

	@Override
	public Prestamo ObtenerPrestamoPorId(int idPrestamo) {
		return dao.ObtenerPrestamoPorId(idPrestamo);
	}

	@Override
	public ArrayList<Prestamo> ListarPorClienteAprobados(int idCliente) {
		return dao.ListarPorClienteAprobados(idCliente);
	}
	public ArrayList<Prestamo> filtrarLista(ArrayList<Prestamo> listaOriginal, String tipoFiltro, double saldoFiltro) {
	    ArrayList<Prestamo> listaFiltrada = new ArrayList<>();

	    if (saldoFiltro == 0.0) {
	        return listaOriginal; // No aplicar ningún filtro y devolver la lista completa
	    }

	    for (Prestamo prestamo : listaOriginal) {
	        boolean cumpleCondición = false;
	        double saldo = prestamo.getImportePedido();

	        if (tipoFiltro.equals("mayor")) {
	            cumpleCondición = (saldo > saldoFiltro);
	        } else if (tipoFiltro.equals("menor")) {
	            cumpleCondición = (saldo < saldoFiltro);
	        } else if (tipoFiltro.equals("igual")) {
	            cumpleCondición = (saldo == saldoFiltro);
	        }

	        if (cumpleCondición) {
	            listaFiltrada.add(prestamo);
	        }
	    }

	    return listaFiltrada;
	}
}
