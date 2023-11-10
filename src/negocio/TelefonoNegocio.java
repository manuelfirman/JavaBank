package negocio;

import java.util.ArrayList;

import dao.TelefonoDao;
import entidad.Telefono;
import interfazNegocio.ITelefonoNegocio;

public class TelefonoNegocio implements ITelefonoNegocio {
	private TelefonoDao dao = new TelefonoDao();

	@Override
	public ArrayList<Telefono> ListarTelefonoPorIdCliente(int idCliente) {
		return dao.ListarTelefonoPorIdCliente(idCliente);
	}

	@Override
	public boolean AgregarTelefonos(int idCliente, String[] numeros) {
		return dao.AgregarTelefonos(idCliente, numeros)== 0?false : true;
	}

	@Override
	public boolean ModificarTelefono(int idTelefono, String nuevoNumero) {
		return dao.ModificarTelefono(idTelefono, nuevoNumero) == 0?false:true;
	}

	@Override
	public boolean EliminarTeléfono(int idTelefono) {
		return dao.EliminarTeléfono(idTelefono) == 0?false:true;
	}

}