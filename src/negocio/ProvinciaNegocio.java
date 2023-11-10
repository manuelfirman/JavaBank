package negocio;

import java.util.ArrayList;

import dao.ProvinciaDao;
import entidad.Provincia;
import interfazNegocio.IProvinciaNegocio;

public class ProvinciaNegocio implements IProvinciaNegocio {
	
	private ProvinciaDao dao = new ProvinciaDao();

	@Override
	public ArrayList<Provincia> Listar() {
		return dao.ListarProvincias();
	}
	
}