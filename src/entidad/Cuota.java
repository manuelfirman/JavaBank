package entidad;

import java.time.LocalDate;

public class Cuota {
	private int idCuota;
	private int numeroCuota;
	private Prestamo prestamo;
	private double importe;
	private LocalDate fechaPago;
	private Estado estado;

	
	public enum Estado{
		IMPAGO,
		PAGO
	}
	
	public Cuota() {}

	public Cuota(int numeroCuota, Prestamo prestamo, double importe, LocalDate fechaPago, Estado estado) {
		super();
		this.numeroCuota = numeroCuota;
		this.prestamo = prestamo;
		this.importe = importe;
		this.fechaPago = fechaPago;
		this.estado = estado;
	}
	
	public int getIdCuota() {
		return this.idCuota;
	}
	public void setIdCuota(int idCuota) {
		this.idCuota = idCuota;
	}
	
	public int getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(int numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Prestamo getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	

}
