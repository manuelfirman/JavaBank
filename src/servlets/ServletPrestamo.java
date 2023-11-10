
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
import entidad.Cuota;
import entidad.Movimiento;
import entidad.Prestamo;
import entidad.Prestamo.Estado;
import entidad.TipoMovimiento;
import negocio.CuentaNegocio;
import negocio.CuotaNegocio;
import negocio.MovimientoNegocio;
import negocio.PrestamoNegocio;

@WebServlet("/ServletPrestamo")
public class ServletPrestamo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	CuentaNegocio cuentaNegocio = new CuentaNegocio();

	public ServletPrestamo() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// PEDIR PRESTAMO
		if (request.getParameter("pedirPrestamo") != null)
			IrAPedirPrestamo(request, response);

		// LISTA PAGAR
		if (request.getParameter("listaPagar") != null) 
			ListaPagar(request, response);


		// LISTA PRESTAMOS ADMIN
		if (request.getParameter("lista") != null) 
			ListaPrestamosAdmin(request, response);
		
		// BUSCAR PRESTAMOS ADMIN
		if (request.getParameter("btnBusquedaAdmin") != null) 
			BuscarPrestamosAdmin(request, response);
		
		// FILTRAR PRESTAMOS ADMIN
		if (request.getParameter("btnFiltrarAdmin") != null)
			FiltrarPrestamosAdmin(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		// PEDIR PRESTAMO
		if (request.getParameter("btnPedirPrestamo") != null) 
			PedirPrestamo(request, response);
		
		// APROBAR PRESTAMO
		if (request.getParameter("btnAprobar") != null) 
			AprobarPrestamo(request, response);
		
		// RECHAZAR PRESTAMO
		if (request.getParameter("btnRechazar") != null) 
			RechazarPrestamo(request, response);
		
		// PAGAR PRESTAMO
		if (request.getParameter("btnPagar") != null) 
			PagarPrestamo(request, response);
	}
	
	
	// ******************************************* METODOS DO GET *********************************************

	// IR A PEDIR PRESTAMO
	private void IrAPedirPrestamo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		session.setAttribute("cuentaPrestamo", null);

		int numeroCuenta = Integer.parseInt(request.getParameter("pedirPrestamo"));
		Cuenta cuenta = cuentaNegocio.ObtenerPorNumeroCuenta(numeroCuenta);

		session.setAttribute("cuentaPrestamo", cuenta);

		response.sendRedirect("PedirPrestamo.jsp");
	}
	
	// LISTA PAGAR
	private void ListaPagar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Cliente cliente = (Cliente) session.getAttribute("cliente");
		int idCliente = cliente.getIdCliente();

		CuotaNegocio cuotaNegocio = new CuotaNegocio();
		ArrayList<Cuota> listaCuotas = cuotaNegocio.ListarPorClienteAprobadoEImpago(idCliente);

		request.setAttribute("listaCuotas", listaCuotas);

		CuentaNegocio cuentaNegocio = new CuentaNegocio();
		ArrayList<Cuenta> listaCuentas = cuentaNegocio.ListarPorIdCliente(idCliente, null);

		request.setAttribute("listaCuentas", listaCuentas);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListarPrestamosCliente.jsp");
		dispatcher.forward(request, response);
	}
	
	// LISTA PRESTAMOS ADMIN
	private void ListaPrestamosAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrestamoNegocio prestamoNegocio = new PrestamoNegocio();
		ArrayList<Prestamo> listaPrestamos = prestamoNegocio.ListarPendientes(null);

		request.setAttribute("listaPrestamo", listaPrestamos);
		RequestDispatcher dispatcher = request.getRequestDispatcher("ListarPrestamosAdmin.jsp");
		dispatcher.forward(request, response);
	}
	
	// BUSCAR PRESTAMOS ADMIN
	private void BuscarPrestamosAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String busqueda = request.getParameter("busqueda");

		PrestamoNegocio prestamoNegocio = new PrestamoNegocio();
		ArrayList<Prestamo> listaPrestamos = prestamoNegocio.ListarPendientes(busqueda);

		request.setAttribute("listaPrestamo", listaPrestamos);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListarPrestamosAdmin.jsp");
		dispatcher.forward(request, response);
	}
	
	// FILTRAR PRESTAMOS ADMIN
	private void FiltrarPrestamosAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tipoFiltro = request.getParameter("operadorSaldo");
		String saldoFiltroStr = request.getParameter("saldoFiltro");
		double saldoFiltro = (saldoFiltroStr != null && !saldoFiltroStr.isEmpty())
				? Double.parseDouble(saldoFiltroStr)
				: 0.0;
		String busqueda = request.getParameter("busqueda");

		PrestamoNegocio prestamoNegocio = new PrestamoNegocio();
		ArrayList<Prestamo> listaPrestamos = prestamoNegocio.ListarPendientes(busqueda);
		listaPrestamos = prestamoNegocio.filtrarLista(listaPrestamos, tipoFiltro, saldoFiltro);

		request.setAttribute("listaPrestamo", listaPrestamos);

		request.setAttribute("operadorSaldo", tipoFiltro);
		request.setAttribute("busqueda", busqueda);
		request.setAttribute("saldoFiltro", saldoFiltro);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ListarPrestamosAdmin.jsp");
		dispatcher.forward(request, response);
	}
	
	// ******************************************* METODOS DO POST *********************************************
	
	// PEDIR PRESTAMO
	private void PedirPrestamo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Cuenta cuenta = (Cuenta) session.getAttribute("cuentaPrestamo");

		Cliente cliente = (Cliente) session.getAttribute("cliente");

		Prestamo prestamo = new Prestamo();

		cuenta.setCliente(cliente);
		prestamo.setCuenta(cuenta);

		int cuotas = Integer.parseInt(request.getParameter("cuotas"));
		prestamo.setCuotas(cuotas);

		double importe = Double.parseDouble(request.getParameter("importe"));
		prestamo.setImportePedido(importe);

		PrestamoNegocio prestamoNegocio = new PrestamoNegocio();

		boolean exito = prestamoNegocio.PedirPrestamo(prestamo);

		if (exito) {
			session.setAttribute("cuentaPrestamo", null);
			response.sendRedirect(request.getContextPath() + "/ServletCuenta?listaPorId=1");
		}
	}
	
	// APROBAR PRESTAMO
	private void AprobarPrestamo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrestamoNegocio prestamoNegocio = new PrestamoNegocio();
		int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));

		prestamoNegocio.CambiarEstadoPrestamo(idPrestamo, Estado.APROBADO);

		Prestamo prestamo = prestamoNegocio.ObtenerPrestamoPorId(idPrestamo);
		CuentaNegocio cuentaNegocio = new CuentaNegocio();
		Cuenta cuenta = cuentaNegocio.ObtenerPorNumeroCuenta(prestamo.getCuenta().getNumero());

		// Generar movimiento
		Movimiento movimiento = new Movimiento();
		movimiento.setDetalle("Suma por préstamo aprobado");
		movimiento.setIdTipoMovimiento(new TipoMovimiento(2));
		movimiento.setImporte(prestamo.getImportePedido());
		movimiento.setCuenta(cuenta);

		cuentaNegocio.SumarSaldo(cuenta.getNumero(), prestamo.getImportePedido());

		MovimientoNegocio movimientoNegocio = new MovimientoNegocio();
		movimientoNegocio.Agregar(movimiento);

		// Generar Cuotas
		CuotaNegocio cuotaNegocio = new CuotaNegocio();
		if (cuotaNegocio.GenerarCuotas(prestamo)) {

			ArrayList<Prestamo> listaPrestamos = prestamoNegocio.ListarPendientes(null);
			request.setAttribute("listaPrestamo", listaPrestamos);

			RequestDispatcher dispatcher = request.getRequestDispatcher("ListarPrestamosAdmin.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	// RECHAZAR PRESTAMO
	private void RechazarPrestamo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrestamoNegocio prestamoNegocio = new PrestamoNegocio();
		int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));

		prestamoNegocio.CambiarEstadoPrestamo(idPrestamo, Estado.RECHAZADO);

		RequestDispatcher dispatcher = request.getRequestDispatcher("Inicio.jsp");
		dispatcher.forward(request, response);
	}
	
	// PAGAR PRESTAMO
	private void PagarPrestamo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idCuota = Integer.parseInt(request.getParameter("cuota"));
		double importe = Double.parseDouble(request.getParameter("importe"));
		int numeroCuenta = Integer.parseInt(request.getParameter("cuenta"));

		Cuenta cuenta = cuentaNegocio.ObtenerPorNumeroCuenta(numeroCuenta);

		CuentaNegocio cuentaNegocio = new CuentaNegocio();
		boolean exitoCuenta = cuentaNegocio.SumarSaldo(numeroCuenta, importe * (-1));

		if (exitoCuenta) {

			CuotaNegocio cuotaNegocio = new CuotaNegocio();
			boolean exitoCuota = cuotaNegocio.PagarCuota(idCuota);
			if (exitoCuota) {
				Movimiento movimiento = new Movimiento();
				movimiento.setDetalle("Pago de cuota de Préstamo");
				movimiento.setIdTipoMovimiento(new TipoMovimiento(3));
				movimiento.setImporte(importe * (-1));
				movimiento.setCuenta(cuenta);

				MovimientoNegocio movimientoNegocio = new MovimientoNegocio();
				movimientoNegocio.Agregar(movimiento);

				RequestDispatcher dispatcher = request.getRequestDispatcher("Inicio.jsp");
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("no hay fondos");

			// aca hay que mopstrar alertas
		}
	}
}
