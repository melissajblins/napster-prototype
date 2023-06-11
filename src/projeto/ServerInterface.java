package projeto;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.HashSet;

public interface ServerInterface {

	String join(Peerold peer) throws Exception;

	String update(Peerold peer, String arquivo) throws Exception;

	HashSet<Peerold> search(String arquivo) throws Exception;

	public String download(String ip, int porta, String path, String nomearquivo) throws UnknownHostException, IOException;
}
