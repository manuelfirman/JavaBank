package negocio;

import java.util.ArrayList;

import dao.MovimientoDao;
import entidad.Movimiento;
import interfazNegocio.IMovimientoNegocio;

public class MovimientoNegocio implements IMovimientoNegocio {
	MovimientoDao dao = new MovimientoDao();

	@Override
	public ArrayList<Movimiento> ListarPorNumeroCuenta(int numeroCuenta, String busqueda) {
		return dao.ListarPorNumeroCuenta(numeroCuenta, busqueda);
	}

	@Override
	public boolean Agregar(Movimiento movimiento) {
		return dao.Agregar(movimiento) == 0 ? false :true;
	}
	
	public ArrayList<Movimiento> filtrarLista(ArrayList<Movimiento> listaOriginal, String tipoFiltro, double saldoFiltro) {
	    ArrayList<Movimiento> listaFiltrada = new ArrayList<>();

	    if (saldoFiltro == 0.0) {
	        return listaOriginal; // No aplicar ning�n filtro y devolver la lista completa
	    }

	    for (Movimiento movimiento : listaOriginal) {
	        boolean cumpleCondici�n = false;
	        double saldo = movimiento.getImporte();

	        if (tipoFiltro.equals("mayor")) {
	            cumpleCondici�n = (saldo > saldoFiltro);
	        } else if (tipoFiltro.equals("menor")) {
	            cumpleCondici�n = (saldo < saldoFiltro);
	        } else if (tipoFiltro.equals("igual")) {
	            cumpleCondici�n = (saldo == saldoFiltro);
	        }

	        if (cumpleCondici�n) {
	            listaFiltrada.add(movimiento);
	        }
	    }

	    return listaFiltrada;
	}
}
