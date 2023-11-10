package negocio;

import java.util.ArrayList;

import dao.LocalidadDao;
import entidad.Localidad;
import interfazNegocio.ILocalidadNegocio;

public class LocalidadNegocio implements ILocalidadNegocio {
	
	private LocalidadDao dao = new LocalidadDao();

	@Override
	public ArrayList<Localidad> Listar() {
		return dao.ListarLocalidades();
	}

}
