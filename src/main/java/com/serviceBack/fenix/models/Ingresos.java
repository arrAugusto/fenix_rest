package com.serviceBack.fenix.models;



public class Ingresos {
	private String usuario;
	private String nit;
	private String canalDigital;
	private String fechaGarita;
	private String fechaBodega;
	private String fechaOperativa;
	private String documento;
	private String codigoQR;
	private int bultos;
	private double cif;
	private double impuestos;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getCanalDigital() {
		return canalDigital;
	}

	public void setCanalDigital(String canalDigital) {
		this.canalDigital = canalDigital;
	}

	public String getFechaGarita() {
		return fechaGarita;
	}

	public void setFechaGarita(String fechaGarita) {
		this.fechaGarita = fechaGarita;
	}

	public String getFechaBodega() {
		return fechaBodega;
	}

	public void setFechaBodega(String fechaBodega) {
		this.fechaBodega = fechaBodega;
	}

	public String getFechaOperativa() {
		return fechaOperativa;
	}

	public void setFechaOperativa(String fechaOperativa) {
		this.fechaOperativa = fechaOperativa;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getCodigoQR() {
		return codigoQR;
	}

	public void setCodigoQR(String codigoQR) {
		this.codigoQR = codigoQR;
	}

	public int getBultos() {
		return bultos;
	}

	public void setBultos(int bultos) {
		this.bultos = bultos;
	}

	public double getCif() {
		return cif;
	}

	public void setCif(double cif) {
		this.cif = cif;
	}

	public double getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}

}
