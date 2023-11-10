package negocio;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import dao.ClienteDao;
import entidad.Cliente;
import interfazNegocio.IClienteNegocio;

public class ClienteNegocio implements IClienteNegocio {
	private ClienteDao dao = new ClienteDao();

	@Override
	public Cliente Login(String usuario, String contrasenia) {    
        Cliente cliente = dao.Login(usuario, contrasenia);
        return cliente; // Devuelve el objeto Cliente si es diferente de null
    }

	@Override
	public int Agregar(Cliente cliente) {
		return dao.Agregar(cliente);
	}

	@Override
	public ArrayList<Cliente> ListarClientesActivos(String busqueda) {
		return dao.ListarClientesActivos(busqueda);
	}
	public Cliente ObtenerPorIdCliente(int idCliente) {
		return dao.ObtenerPorIdCliente(idCliente);
	}

	@Override
	public boolean ModificarCliente(Cliente cliente) {
		return dao.ModificarCliente(cliente) == 0 ? false : true;
	}

	@Override
	public boolean EliminarCliente(int idCliente) {
		return dao.EliminarCliente(idCliente) == 0 ? false : true;
	}
	public ArrayList<Cliente> filtrarLista(ArrayList<Cliente> listaOriginal, String tipoFiltro, int numeroFiltro) {
	    ArrayList<Cliente> listaFiltrada = new ArrayList<>();

	    if (numeroFiltro == 0) {
	        return listaOriginal; // No aplicar ningún filtro y devolver la lista completa
	    }

	    for (Cliente cliente : listaOriginal) {
	        int edadCliente = calcularEdad(cliente.getFechaNacimiento()); // Asume que tienes un método para calcular la edad
	        boolean cumpleCondicion = false;

	        if (tipoFiltro.equals("mayor")) {
	            cumpleCondicion = (edadCliente > numeroFiltro);
	        } else if (tipoFiltro.equals("menor")) {
	            cumpleCondicion = (edadCliente < numeroFiltro);
	        } else if (tipoFiltro.equals("igual")) {
	            cumpleCondicion = (edadCliente == numeroFiltro);
	        }

	        if (cumpleCondicion) {
	            listaFiltrada.add(cliente);
	        }
	    }

	    return listaFiltrada;
	}

	public int calcularEdad(LocalDate fechaNacimiento) {
	    LocalDate fechaActual = LocalDate.now();
	    Period edad = Period.between(fechaNacimiento, fechaActual);
	    return edad.getYears();
	}
	
	//devuelven true si es unico elq ue se le pasa y puede ser agregado
	@Override
    public boolean CuilUnico(String cuil,int idCliente) {
		try {
			return dao.CuilUnico(cuil,idCliente) == 0 ? false : true;			
		} catch(Exception e) {
			return false;
		}
    }

    @Override
    public boolean UsuarioUnico(String usuario) {
    	try {    		
    		return dao.UsuarioUnico(usuario) == 0 ? false : true;
    	} catch(Exception e) {
    		return false;
    	}
    }

    @Override
    public boolean CorreoUnico(String correo,int idCliente) {
    	try {
    		return dao.CorreoUnico(correo,idCliente) == 0 ? false : true;    		
    	} catch(Exception e) {
    		return false;
    	}
    }

	@Override
	public boolean DniUnico(int dni,int idCliente) {
		try {			
			return dao.DniUnico(dni,idCliente) == 0? false:true;
		} catch (Exception e) {
			return false;
		}
	}
	
}