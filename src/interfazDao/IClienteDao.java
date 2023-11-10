package interfazDao;

import java.util.ArrayList;

import entidad.Cliente;

public interface IClienteDao {
	public Cliente Login(String usuario, String constrasenia);
	public int Agregar(Cliente cliente);
	public ArrayList<Cliente> ListarClientesActivos(String busqueda);
	public Cliente ObtenerPorIdCliente(int idCliente);
	public int ModificarCliente(Cliente cliente);
	public int EliminarCliente(int idCliente);
	public int DniUnico(int dni, int idCliente) ;
	public int CuilUnico(String cuil, int idCliente);
	public int UsuarioUnico(String usuario);
	public int CorreoUnico(String correo, int idCliente);
}
