package rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.model.Hora;
import rmi.model.ServicoHora;

public class Cliente {

	public static void main(String[] args) throws Exception {
		Registry reg = LocateRegistry.getRegistry();
		ServicoHora shc = (ServicoHora) reg.lookup("rmi://127.0.0.1/servicoHora");
		Hora hora = shc.obterHora("Cliente");
		
		System.out.println(hora.nomeCliente + " - " + hora.timestamp);
	}
}
