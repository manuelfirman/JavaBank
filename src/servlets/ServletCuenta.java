package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entidad.Cliente;
import entidad.Cuenta;
import entidad.Movimiento;
import entidad.TipoCuenta;
import entidad.TipoMovimiento;
import negocio.ClienteNegocio;
import negocio.CuentaNegocio;
import negocio.MovimientoNegocio;

@WebServlet("/ServletCuenta")
public class ServletCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CuentaNegocio cuentaNegocio = new CuentaNegocio();

	public ServletCuenta() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// AGREGAR ID (ListadoClientes.jsp)
		if (request.getParameter("AgregarId") != null)
			AgregarId(request, response);
		
		
		// MODIFICAR NUMERO DE CUENTA (ModificarCuenta.jsp)
		if (request.getParameter("ModificarNumero") != null)
			ModificarNumeroCuenta(request, response);

		
		// LISTA (ListadoCuentas.jsp)
		if (request.getParameter("lista") != null) 
			Lista(request, response);

		// ELIMINAR NUMERO DE CUENTA (ListadoCuentas.jsp)
		if (request.getParameter("EliminarNumeroCuenta") != null)
			EliminarNumeroCuenta(request, response);
			
		// BUSCAR (ListadoCuentas.jsp)
		if (request.getParameter("btnBusqueda") != null) 
			Buscar(request, response);
			
		// FILTRAR (ListadoCuentas.jsp)
		if (request.getParameter("btnFiltrar") != null) 
			Filtrar(request, response);
		

		// LISTAR POR ID (ListadoCuentasDeCliente.jsp)
		if (request.getParameter("listaPorId") != null)
			ListaPorId(request, response);
		
		// BUSCAR CLIENTES (ListadoCuentasDeCliente.jsp)
		if (request.getParameter("btnBusquedaCl") != null) 
			BuscarClientes(request, response);
		
		// FILTRAR CLIENTES (ListadoCuentasDeCliente.jsp)
		if (request.getParameter("btnFiltrarCl") != null) 
			FiltrarClientes(request, response);

		// HISTORIAL GENERAL (ListadoCuentasDeCliente.jsp)
		if (request.getParameter("historial") != null) 
			HistorialGeneral(request, response);
		
		
		// TRANSFERENCIA (Transferencia.jsp)
		if (request.getParameter("transferencia") != null) 
			Transferencia(request, response);
		
		
		// BUSCAR HISTORIAL (Historial.jsp)
		if (request.getParameter("btnBusquedaHistorial") != null) 
			BusquedaHistorial(request, response);

		// FILTRAR HISTORIAL (Historial.jsp)
		if (request.getParameter("btnFiltrarHistorial") != null) 
			FiltrarHistorial(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		
		// AGREGAR CUENTA (AltaCuenta.jsp)
		if (request.getParameter("btnAgregar") != null) 
			AgregarCuenta(request, response);

		// MODIFICAR CUENTA (ModificarCuenta.jsp)
		if (request.getParameter("btnModificar") != null)
			ModificarCuenta(request, response);
		
		// TRANSFERIR (Transferencia.jsp)
		if (request.getParameter("btnTransferir") != null) 
			Transferir(request, response);
			
	}
	
	// ******************************************* METODOS DO GET *********************************************
	// AGREGAR ID
	private void AgregarId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idCliente = Integer.parseInt(request.getParameter("AgregarId"));

		request.setAttribute("idCliente", idCliente);

		ArrayList<TipoCuenta> tiposCuenta = cuentaNegocio.ListarTipoCuenta();

		request.setAttribute("tiposCuenta", tiposCuenta);

		RequestDispatcher dispatcher = request.getRequestDispatcher("AltaCuenta.jsp");
		dispatcher.forward(request, response);
	}
	
	// MODIFICAR NUMERO DE CUENTA
	private void ModificarNumeroCuenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int numeroCuenta = Integer.parseInt(request.getParameter("ModificarNumero"));

		Cuenta cuenta = cuentaNegocio.ObtenerPorNumeroCuenta(numeroCuenta);

		request.setAttribute("cuenta", cuenta);

		ArrayList<TipoCuenta> tiposCuenta = cuentaNegocio.ListarTipoCuenta();

		request.setAttribute("tiposCuenta", tiposCuenta);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ModificarCuenta.jsp");
		dispatcher.forward(request, response);
	}
	
	// LISTA
	private void Lista(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("advertencia", null);

		ArrayList<Cuenta> Cuentas = cuentaNegocio.ListarCuentasActivas(null);
		request.setAttribute("listaCuentas", Cuentas);

		request.setAttribute("operadorSaldo", "mayor");
		request.setAttribute("busqueda", "");
		request.setAttribute("saldoFiltro", "");

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentas.jsp");
		dispatcher.forward(request, response);
	}
	
	// ELIMINAR NUMERO DE CUENTA
	private void EliminarNumeroCuenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int numeroCuentaAEliminar = Integer.parseInt(request.getParameter("EliminarNumeroCuenta"));
		boolean exitoEliminacion = cuentaNegocio.EliminarCuenta(numeroCuentaAEliminar);

		if (exitoEliminacion) {
			request.setAttribute("exitoEliminacion", true);
		} else {
			request.setAttribute("exitoEliminacion", false);
		}

		ArrayList<Cuenta> Cuentas = cuentaNegocio.ListarCuentasActivas(null);
		request.setAttribute("listaCuentas", Cuentas);

		request.setAttribute("mostrarError", !exitoEliminacion);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentas.jsp");
		dispatcher.forward(request, response);
	}

	
	// BUSCAR
	private void Buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String busqueda = request.getParameter("busqueda");

		ArrayList<Cuenta> listaCuentas = cuentaNegocio.ListarCuentasActivas(busqueda);

		request.setAttribute("listaCuentas", listaCuentas);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentas.jsp");
		dispatcher.forward(request, response);
	}
	
	
	// FILTRAR
	private void Filtrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tipoFiltro = request.getParameter("operadorSaldo");
		String saldoFiltroStr = request.getParameter("saldoFiltro");
		double saldoFiltro = (saldoFiltroStr != null && !saldoFiltroStr.isEmpty())
				? Double.parseDouble(saldoFiltroStr)
				: 0.0;
		String busqueda = request.getParameter("busqueda");

		ArrayList<Cuenta> listaCuentas = cuentaNegocio.ListarCuentasActivas(busqueda);
		listaCuentas = cuentaNegocio.filtrarLista(listaCuentas, tipoFiltro, saldoFiltro);

		request.setAttribute("listaCuentas", listaCuentas);

		request.setAttribute("operadorSaldo", tipoFiltro);
		request.setAttribute("busqueda", busqueda);
		request.setAttribute("saldoFiltro", saldoFiltro);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentas.jsp");
		dispatcher.forward(request, response);
	}

	// LISTA POR ID
	private void ListaPorId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Cliente cliente = (Cliente) session.getAttribute("cliente");

		CuentaNegocio cuentaNegocio = new CuentaNegocio();

		if (cliente != null) {
			int idCliente = cliente.getIdCliente();

			ArrayList<Cuenta> cuentas = cuentaNegocio.ListarPorIdCliente(idCliente, null);
			request.setAttribute("listaCuentas", cuentas);

			request.setAttribute("operadorSaldo", "mayor");
			request.setAttribute("busqueda", "");
			request.setAttribute("saldoFiltro", "");

			RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentasDeCliente.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect("error.jsp");
		}
	}
	
	// BUSCAR CLIENTES
	private void BuscarClientes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Cliente cliente = (Cliente) session.getAttribute("cliente");

		int idCliente = cliente.getIdCliente();
		String busqueda = request.getParameter("busqueda");

		ArrayList<Cuenta> listaCuentas = cuentaNegocio.ListarPorIdCliente(idCliente, busqueda);

		request.setAttribute("listaCuentas", listaCuentas);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentasDeCliente.jsp");
		dispatcher.forward(request, response);
	}
	
	// FILTRAR CLIENTES
	private void FiltrarClientes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Cliente cliente = (Cliente) session.getAttribute("cliente");

		int idCliente = cliente.getIdCliente();

		String tipoFiltro = request.getParameter("operadorSaldo");
		String saldoFiltroStr = request.getParameter("saldoFiltro");
		double saldoFiltro = (saldoFiltroStr != null && !saldoFiltroStr.isEmpty())
				? Double.parseDouble(saldoFiltroStr)
				: 0.0;
		String busqueda = request.getParameter("busqueda");

		ArrayList<Cuenta> listaCuentas = cuentaNegocio.ListarPorIdCliente(idCliente, busqueda);
		listaCuentas = cuentaNegocio.filtrarLista(listaCuentas, tipoFiltro, saldoFiltro);

		request.setAttribute("listaCuentas", listaCuentas);

		request.setAttribute("operadorSaldo", tipoFiltro);
		request.setAttribute("busqueda", busqueda);
		request.setAttribute("saldoFiltro", saldoFiltro);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentasDeCliente.jsp");
		dispatcher.forward(request, response);
	}
	
	// HISTORIAL GENERAL
	private void HistorialGeneral(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int numeroCuenta = Integer.parseInt(request.getParameter("historial"));
		MovimientoNegocio movimientoNegocio = new MovimientoNegocio();

		HttpSession session = request.getSession();

		session.setAttribute("numeroCuenta", numeroCuenta);

		ArrayList<Movimiento> movimientos = movimientoNegocio.ListarPorNumeroCuenta(numeroCuenta, null);
		request.setAttribute("movimientos", movimientos);

		RequestDispatcher dispatcher = request.getRequestDispatcher("Historial.jsp");
		dispatcher.forward(request, response);
	}
	
	// TRANSFERENCIA
	private void Transferencia(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int numeroCuenta = Integer.parseInt(request.getParameter("transferencia"));
		Cuenta cuentaOrigen = cuentaNegocio.ObtenerPorNumeroCuenta(numeroCuenta);

		System.out.print(cuentaOrigen.toString());

		HttpSession session = request.getSession();

		request.setAttribute("operadorSaldo", "mayor");
		request.setAttribute("busqueda", "");
		request.setAttribute("saldoFiltro", "");

		session.removeAttribute("cuentaOrigen");
		session.setAttribute("cuentaOrigen", cuentaOrigen);

		response.sendRedirect("Transferencia.jsp");
	}
	
	// BUSQUEDA EN EL HISTORIAL
	private void BusquedaHistorial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String busqueda = request.getParameter("busqueda");
		HttpSession session = request.getSession();
		Integer numeroCuenta = (Integer) session.getAttribute("numeroCuenta");

		MovimientoNegocio movimientoNegocio = new MovimientoNegocio();
		ArrayList<Movimiento> movimientos = movimientoNegocio.ListarPorNumeroCuenta(numeroCuenta, busqueda);

		request.setAttribute("movimientos", movimientos);

		RequestDispatcher dispatcher = request.getRequestDispatcher("Historial.jsp");
		dispatcher.forward(request, response);
	}
		
	
	// FILTRAR HISTORIAL
	private void FiltrarHistorial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String busqueda = request.getParameter("busqueda");
		HttpSession session = request.getSession();
		Integer numeroCuenta = (Integer) session.getAttribute("numeroCuenta");

		String tipoFiltro = request.getParameter("operadorSaldo");
		String saldoFiltroStr = request.getParameter("saldoFiltro");
		double saldoFiltro = (saldoFiltroStr != null && !saldoFiltroStr.isEmpty())
				? Double.parseDouble(saldoFiltroStr)
				: -100000000000000.0;

		MovimientoNegocio movimientoNegocio = new MovimientoNegocio();
		ArrayList<Movimiento> movimientos = movimientoNegocio.ListarPorNumeroCuenta(numeroCuenta, busqueda);
		movimientos = movimientoNegocio.filtrarLista(movimientos, tipoFiltro, saldoFiltro);

		request.setAttribute("movimientos", movimientos);

		request.setAttribute("operadorSaldo", tipoFiltro);
		request.setAttribute("busqueda", busqueda);
		request.setAttribute("saldoFiltro", saldoFiltro);

		RequestDispatcher dispatcher = request.getRequestDispatcher("Historial.jsp");
		dispatcher.forward(request, response);
	}
	
	
	// ******************************************* METODOS DO POST *********************************************
	
	// AGREGAR CUENTA
	private void AgregarCuenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int idCliente = Integer.parseInt(request.getParameter("idCliente"));
		int idTipoCuenta = Integer.parseInt(request.getParameter("idTipoCuenta"));

		Cuenta cuenta = new Cuenta();
		ClienteNegocio clienteNegocio = new ClienteNegocio();
		Cliente cliente = clienteNegocio.ObtenerPorIdCliente(idCliente);
		cuenta.setCliente(cliente);

		TipoCuenta tipoCuenta = new TipoCuenta();
		tipoCuenta.setIdTipoCuenta(idTipoCuenta);
		cuenta.setTipoCuenta(tipoCuenta);

		CuentaNegocio cuentaNegocio = new CuentaNegocio();
		int numeroCuenta = cuentaNegocio.Agregar(cuenta);

		request.setAttribute("idCliente", idCliente);

		if (numeroCuenta == -1) {
			request.setAttribute("advertencia", "error");

			ArrayList<TipoCuenta> tiposCuenta = cuentaNegocio.ListarTipoCuenta();
			request.setAttribute("tiposCuenta", tiposCuenta);

			RequestDispatcher dispatcher = request.getRequestDispatcher("AltaCuenta.jsp");
			dispatcher.forward(request, response);
			return;
		}

		cuenta = cuentaNegocio.ObtenerPorNumeroCuenta(numeroCuenta);

		Movimiento movimiento = new Movimiento();
		movimiento.setCuenta(cuenta);
		movimiento.setImporte(10000);
		movimiento.setIdTipoMovimiento(new TipoMovimiento(1));
		movimiento.setDetalle("Dinero Incial Alta de Cuenta");

		MovimientoNegocio movimientoNegocio = new MovimientoNegocio();
		movimientoNegocio.Agregar(movimiento);

		request.setAttribute("advertencia", "exito");
		ArrayList<TipoCuenta> tiposCuenta = cuentaNegocio.ListarTipoCuenta();
		request.setAttribute("tiposCuenta", tiposCuenta);
		RequestDispatcher dispatcher = request.getRequestDispatcher("AltaCuenta.jsp");
		dispatcher.forward(request, response);
	}
	
	// MODIFICAR CUENTA
	private void ModificarCuenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idCliente = Integer.parseInt(request.getParameter("idCliente"));
		int numero = Integer.parseInt(request.getParameter("numero"));
		String CBU = request.getParameter("CBU");
		int idTipoCuenta = Integer.parseInt(request.getParameter("idTipoCuenta"));
		double saldo = Double.parseDouble(request.getParameter("saldo"));

		Cuenta cuenta = new Cuenta();

		ClienteNegocio clienteNegocio = new ClienteNegocio();
		Cliente cliente = clienteNegocio.ObtenerPorIdCliente(idCliente);
		cuenta.setCliente(cliente);

		cuenta.setNumero(numero);
		cuenta.setCBU(CBU);
		cuenta.setSaldo(saldo);
		cuenta.setActivo(1);

		TipoCuenta tipoCuenta = new TipoCuenta();
		tipoCuenta.setIdTipoCuenta(idTipoCuenta);
		cuenta.setTipoCuenta(tipoCuenta);

		cuentaNegocio = new CuentaNegocio();
		boolean exito = cuentaNegocio.ModificarCuenta(cuenta);

		if (exito) {
			request.setAttribute("exitoModificacion", true);

			request.setAttribute("advertencia", null);

			ArrayList<Cuenta> Cuentas = cuentaNegocio.ListarCuentasActivas(null);
			request.setAttribute("listaCuentas", Cuentas);

			request.setAttribute("operadorSaldo", "mayor");
			request.setAttribute("busqueda", "");
			request.setAttribute("saldoFiltro", "");

			RequestDispatcher dispatcher = request.getRequestDispatcher("ListadoCuentas.jsp");
			dispatcher.forward(request, response);

		} else {
			response.sendRedirect("error.jsp");
		}
	}

	// TRANSFERIR
	private void Transferir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		double importe = Double.parseDouble(request.getParameter("importe"));
		Cuenta cuentaOrigen = (Cuenta) request.getSession().getAttribute("cuentaOrigen");

		String cbuDestino = request.getParameter("cbu");

		Cuenta cuentaDestino = cuentaNegocio.ObtenerPorCbu(cbuDestino);

		if (cuentaDestino == null) {

			request.setAttribute("errorMensaje", "El CBU de destino no se encontró.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("Transferencia.jsp");
			dispatcher.forward(request, response);
			return;
		}

		if (cuentaOrigen.getNumero() == cuentaDestino.getNumero()) {

			request.setAttribute("errorMensaje", "No se puede transferir a la misma cuenta.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("Transferencia.jsp");
			dispatcher.forward(request, response);
			return;
		}

		boolean existeSaldo = cuentaNegocio.SumarSaldo(cuentaOrigen.getNumero(), importe * -1);

		if (existeSaldo) {
			cuentaNegocio.SumarSaldo(cuentaDestino.getNumero(), importe);

			Movimiento movimientoOrigen = new Movimiento();
			movimientoOrigen.setCuenta(cuentaOrigen);
			movimientoOrigen.setImporte(-importe);
			movimientoOrigen.setIdTipoMovimiento(new TipoMovimiento(4));
			movimientoOrigen.setDetalle("Resta por transferencia");

			Movimiento movimientoDestino = new Movimiento();
			movimientoDestino.setCuenta(cuentaDestino);
			movimientoDestino.setImporte(importe);
			movimientoDestino.setIdTipoMovimiento(new TipoMovimiento(5));
			movimientoDestino.setDetalle("Suma por transferencia");

			MovimientoNegocio movimientoNegocio = new MovimientoNegocio();
			movimientoNegocio.Agregar(movimientoOrigen);
			movimientoNegocio.Agregar(movimientoDestino);

			request.setAttribute("errorMensaje", "Transferencia exitosa.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("Transferencia.jsp");
			dispatcher.forward(request, response);
		} else {

			request.setAttribute("errorMensaje", "Cuenta de Origen no tiene fondos suficientes");
			RequestDispatcher dispatcher = request.getRequestDispatcher("Transferencia.jsp");
			dispatcher.forward(request, response);
			return;
		}

	}
	
}
	
