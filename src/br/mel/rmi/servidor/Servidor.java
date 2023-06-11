package br.mel.rmi.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Servidor extends Remote {

	String join(String peerHostPorta, List<String> arquivos) throws Exception;

	List<String> search(String arquivo) throws RemoteException;
	
	String update(String peerHostPorta, String arquivo) throws RemoteException;
}
