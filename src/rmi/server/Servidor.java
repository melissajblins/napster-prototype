package rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.model.ServicoHora;
import rmi.model.ServicoHoraImplementacao;

public class Servidor {

	public static void main(String[] args) throws Exception {
		ServicoHora sh = new ServicoHoraImplementacao();
		
		LocateRegistry.createRegistry(1099);
		Registry reg = LocateRegistry.getRegistry();
		
		reg.bind("rmi://127.0.0.1/servicoHora", sh);
		System.out.println("Servidor no ar!");
		
	}
}
