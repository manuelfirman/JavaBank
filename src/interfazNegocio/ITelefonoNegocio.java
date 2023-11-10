package interfazNegocio;

import java.util.ArrayList;

import entidad.Telefono;

public interface ITelefonoNegocio {
	public ArrayList<Telefono> ListarTelefonoPorIdCliente(int idCliente);
	public boolean AgregarTelefonos(int idCliente, String[] numeros);
	public boolean ModificarTelefono(int idTelefono, String nuevoNumero);
	public boolean EliminarTeléfono(int idTelefono);
}