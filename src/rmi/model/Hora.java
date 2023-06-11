package rmi.model;

import java.io.Serializable;

public class Hora implements Serializable {

	private static final long serialVersionUID = 1L;
	public String nomeCliente;
	public long timestamp;
	
	public Hora(String nomeCliente, long timestamp) {
		this.nomeCliente = nomeCliente;
		 this.timestamp = timestamp;
	}
}
