package br.mel.rmi.servidor;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServidorImplementacao extends UnicastRemoteObject implements Servidor {

	private static final long serialVersionUID = 1L;
	private Map<String, List<String>> peerArquivosMap = new HashMap<String, List<String>>();

	protected ServidorImplementacao() throws RemoteException {
		super();
	}

	@Override
	public String join(String peerHostPorta, List<String> arquivos) throws Exception {
		if (peerArquivosMap.containsKey(peerHostPorta)) {
			System.out.println("Peer " + peerHostPorta + " adicionado com arquivos ");
			for (String arquivo : arquivos) {
				System.out.println(arquivo);
			}
			return "JOIN_NOT_OK: O peer já foi cadastrado!"; // IMPORTANTE
		} else {
			peerArquivosMap.put(peerHostPorta, arquivos);
			System.out.println(arquivos);
			return "JOIN_OK: O peer foi cadastrado com sucesso!"; // IMPORTANTE
		}
	}

	@Override
	public List<String> search(String arquivo) throws RemoteException {

		System.out.println("Peer  solicitou arquivo" + arquivo);
		return peerArquivosMap.entrySet().stream().filter(item -> item.getValue().contains(arquivo)).map(Entry::getKey)
				.toList();
	}

	@Override
	public String update(String peerHostPorta, String arquivo) throws RemoteException {
		List<String> arquivosPeer = peerArquivosMap.get(peerHostPorta);
		if (arquivosPeer != null) {
			arquivosPeer.add(arquivo);
			return "UPDATE_OK: O peer foi atualizado com sucesso!"; // IMPORTANTE
		} else {
			return "UPDATE_NOT_OK: O peer não foi encontrado!"; // IMPORTANTE
		}
	}

	public static void main(String[] args) {
		try {
			Servidor servidor = new ServidorImplementacao();
			LocateRegistry.createRegistry(1099);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("rmi://127.0.0.1/servidor", servidor);
			System.out.println("Servidor no ar!");
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
