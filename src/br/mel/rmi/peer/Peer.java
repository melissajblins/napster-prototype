package br.mel.rmi.peer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import br.mel.rmi.servidor.Servidor;

public class Peer implements Runnable {

	private final String tcpHost;
	private final Integer tcpPorta;
	private final Servidor servidorRmi;
	private final String caminhoArquivos;

	public Peer(String tcpHost, String tcpPorta, String hostRmi, String caminhoArquivos)
			throws RemoteException, NotBoundException {
		Registry reg = LocateRegistry.getRegistry();
		this.tcpHost = tcpHost;
		this.tcpPorta = Integer.valueOf(tcpPorta);
		this.servidorRmi = (Servidor) reg.lookup("rmi://" + hostRmi + "/servidor");
		this.caminhoArquivos = caminhoArquivos;
	}

	private List<String> lerArquivos() {
		List<String> arquivos = new ArrayList<>();
		File pasta = new File(this.caminhoArquivos);
		if (!pasta.isDirectory()) {
			System.out.println("Não é um diretório válido.");
		} else {
			for (File file : pasta.listFiles()) {
				arquivos.add(file.getName());
			}
		}

		return arquivos;
	}

	private static void exibirMenu() {
		System.out.println("===== Napster Menu =====");
		System.out.println("Selecione uma opção:");
		System.out.println("1. JOIN");
		System.out.println("2. SEARCH");
		System.out.println("3. DOWNLOAD");
		System.out.println("0. SAIR");
		System.out.println("=======================");
	}

	public void join() throws Exception {
		String tcpHostPorta = this.tcpHost + ":" + this.tcpPorta;
		String resposta = this.servidorRmi.join(tcpHostPorta, lerArquivos());
		if (resposta.equals("JOIN_OK: O peer foi cadastrado com sucesso!")) {
			System.out.println("Sou peer " + tcpHostPorta + " com arquivos " + lerArquivos());
		}
	}

	private List<String> search() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Digite o nome do arquivo: ");
		String nomearquivo = reader.readLine();

		List<String> resposta = this.servidorRmi.search(nomearquivo);
		System.out.println("Peers com arquivo solicitado: ");
		for (String arquivo : resposta) {
			System.out.println(arquivo);
		}
		return resposta;
	}

	public void download() throws UnknownHostException, IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Digite o host: ");
		String peerHost = reader.readLine();

		System.out.println("Digite a porta: ");
		int peerPort = Integer.parseInt(reader.readLine());

		System.out.println("Digite o nome do arquivo: ");
		String nomeArquivo = reader.readLine();

		Socket socket = new Socket(peerHost, peerPort);

		OutputStream os = socket.getOutputStream();
		DataOutputStream writer = new DataOutputStream(os);

		writer.writeBytes(nomeArquivo + "\n");
		writer.flush();

		InputStream is = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream(this.caminhoArquivos + "/" + nomeArquivo);

		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = is.read(buffer)) != -1) {
			fos.write(buffer, 0, bytesRead);
		}

		fos.close();
		is.close();
		socket.close();

		String tcpHostPorta = this.tcpHost + ":" + this.tcpPorta;
		System.out.println(this.servidorRmi.update(tcpHostPorta, nomeArquivo));
		System.out.println("Arquivo " + nomeArquivo + " baixado com sucesso na pasta " + caminhoArquivos);
	}

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(this.tcpPorta)) {
			while (true) {
				System.out.println("Esperando conexão...");

				Socket no = serverSocket.accept(); // Blocking
				System.out.println("Conexão aceita...");

				ThreadAtendimento thread = new ThreadAtendimento(no, this.caminhoArquivos);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class ThreadAtendimento extends Thread {

		private Socket node;
		private String caminhoArquivos;

		public ThreadAtendimento(Socket node, String caminhoArquivos) {
			this.node = node;
			this.caminhoArquivos = caminhoArquivos;

		}

		public void run() {
			try {
				InputStreamReader is = new InputStreamReader(node.getInputStream());
				BufferedReader reader = new BufferedReader(is);

				String nomeArquivo = reader.readLine();
				System.out.println("Nome arquivo para envio: " + nomeArquivo);

				FileInputStream fis = new FileInputStream(this.caminhoArquivos + "/" + nomeArquivo);
				BufferedInputStream bis = new BufferedInputStream(fis);

				OutputStream os = this.node.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);

				byte[] buffer = new byte[1024];
				int bytesRead;

				while ((bytesRead = bis.read(buffer)) != -1) {
					dos.write(buffer, 0, bytesRead);
				}

				dos.flush();
				bis.close();
				dos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		Peer peer = new Peer(args[0], args[1], args[2], args[3]);

		Thread t1 = new Thread(peer, "peer");
		t1.start();

		int opcao = -1;
		do {
			Thread.sleep(999);
			exibirMenu();
			opcao = Integer.parseInt(reader.readLine());
			switch (opcao) {
			case 1:
				peer.join();
				break;
			case 2:
				peer.search();
				break;
			case 3:
				peer.download();
				break;
			case 0:
				System.out.println("Saindo...");
				break;
			default:
				System.out.println("Opção inválida. Tente novamente.");
			}

		} while (opcao != 0);

		System.exit(0);
	}
}
