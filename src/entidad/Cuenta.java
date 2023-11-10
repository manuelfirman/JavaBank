package entidad;

import java.time.LocalDate;

public class Cuenta {
    private int numero;
    private Cliente cliente;
    private String CBU;
    private double saldo;
    private LocalDate fecha;
    private int activo;
    private TipoCuenta tipoCuenta;

    public Cuenta() {
    }

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCBU() {
		return CBU;
	}

	public void setCBU(String cBU) {
		CBU = cBU;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	public TipoCuenta getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(TipoCuenta tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	@Override
	public String toString() {
		return "Cuenta [numero=" + numero + ", cliente=" + cliente + ", CBU=" + CBU + ", saldo=" + saldo + ", fecha="
				+ fecha + ", activo=" + activo + ", tipoCuenta=" + tipoCuenta + "]";
	}

}
