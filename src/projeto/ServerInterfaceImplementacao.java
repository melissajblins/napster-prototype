package projeto;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashSet;

import rmi.model.ServicoHora;
import rmi.model.ServicoHoraImplementacao;

public class ServerInterfaceImplementacao extends UnicastRemoteObject implements ServerInterface {
	private String ip = "127.0.0.1";
	private int porta = 1099;
	private HashSet<Peerold> peers;

	public ServerInterfaceImplementacao() throws RemoteException {
		this.peers = new HashSet<>();
	}

	public String getIp() {
		return ip;
	}

	public int getPorta() {
		return porta;
	}

	public HashSet<Peerold> getPeers() {
		return peers;
	}

	public void setPeers(HashSet<Peerold> peers) {
		this.peers = peers;
	}

	@Override
	public String join(Peerold peer) throws RemoteException {
		if (peers.contains(peer)) {
			return "JOIN_NOT_OK: O peer já foi cadastrado!";
		} else {
			peers.add(peer);
			return "JOIN_OK: O peer foi cadastrado com sucesso!";
		}
	}

	@Override
	public String update(Peerold peer, String arquivo) throws RemoteException {
		if (peers.contains(peer)) {
			return peer.setArquivos(arquivo);
		} else {
			return "UPDATE_NOT_OK: O peer não foi encontrado!";
		}
	}

	@Override
	public HashSet<Peerold> search(String arquivo) throws RemoteException {
		HashSet<Peerold> peersComArquivo = new HashSet<>();
		for (Peerold peer : peers) {
			if (peer.getArquivos().contains(arquivo)) {
				peersComArquivo.add(peer);
			}
		}
		return peersComArquivo;
	}

	@Override
	public String download(String ip, int porta, String path, String nomeArquivo)
			throws UnknownHostException, IOException {
		return nomeArquivo;

	}

	public static void main(String[] args) {
		try {
			ServerInterfaceImplementacao servidor = new ServerInterfaceImplementacao();
			LocateRegistry.createRegistry(servidor.getPorta());
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("servidor", servidor);
			System.out.println("Servidor no ar!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
