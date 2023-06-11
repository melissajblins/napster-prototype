package rmi.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// comunicação 1 a 1
public class ServicoHoraImplementacao extends UnicastRemoteObject implements ServicoHora { 
	private static final long serialVersionUID = 1L;

	public ServicoHoraImplementacao() throws RemoteException{
		super();
	}
	
	@Override
	public Hora obterHora(String nomeCliente) throws RemoteException {
		Hora horaServidor = new Hora(nomeCliente, System.currentTimeMillis());
		return horaServidor;
	} 
}
