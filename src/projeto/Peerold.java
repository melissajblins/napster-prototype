package projeto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import projeto.*;

public class Peerold {
	private String nome;
	private String ip;
	private int porta;
	private String path = "/home/mel/Transferências";
	private HashSet<String> arquivos;
	private int id;

	public Peerold(String nome, String ip, int porta, String path, HashSet<String> arquivos) {
		this.nome = nome;
		this.ip = ip;
		this.porta = porta;
		this.path = path;
		this.arquivos = arquivos;
	}

	public Peerold(String nomePeer, Object object) {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	public String getIp() {
		return this.ip;
	}

	public int getPorta() {
		return this.porta;
	}

	public HashSet<String> getArquivos() {
		return this.arquivos;
	}

	public String setArquivos(String nomeArquivo) {
		if (arquivos.contains(nomeArquivo)) {
			return "UPDATE_NOT_OK: O peer já possui esse arquivo!";
		} else {
			arquivos.add(nomeArquivo);
			return "UPDATE_OK: O peer não foi encontrado!";
		}
	}
	
	public String getPath() {
		return this.path;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Digite o nome do peer: ");
		String nomePeer = reader.readLine();

		Peerold peer = new Peerold(nomePeer, new HashSet<>());

		System.out.print("Digite o nome do arquivo para adicionar à biblioteca: ");
		String nomeArquivo = reader.readLine();
		peer.setArquivos(nomeArquivo);
		
		Registry registry = LocateRegistry.getRegistry(peer.getIp(), peer.getPorta());
		ServerInterface servidor = (ServerInterface) registry.lookup("servidor");

		int opcao = -1;
		do {
			exibirMenu();
			opcao = Integer.parseInt(reader.readLine());
			switch (opcao) {
			case 1:
				opcaoJoin(peer, servidor);
				break;
			case 2:
				opcaoUpdate(peer, servidor);
				break;
			case 3:
				opcaoSearch(peer, servidor);
				break;
			case 4:
				opcaoDownload(peer, servidor);
				break;
			case 0:
				System.out.println("Saindo...");
				break;
			default:
				System.out.println("Opção inválida. Tente novamente.");
			}

		} while (opcao != 0);

		reader.close();
	}

	private static void exibirMenu() {
		System.out.println("===== Napster Menu =====");
		System.out.println("Selecione uma opção:");
		System.out.println("1. JOIN");
		System.out.println("2. UPDATE");
		System.out.println("3. SEARCH");
		System.out.println("4. DOWNLOAD");
		System.out.println("0. SAIR");
		System.out.println("=======================");
	}

	private static void opcaoJoin(Peerold peer, ServerInterface servidor) throws Exception {
		String resposta = servidor.join(peer);
		System.out.println(resposta);
	}

	private static void opcaoUpdate(Peerold peer, ServerInterface servidor) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Digite o nome do arquivo: ");
		String nomearquivo = reader.readLine();

		String resposta = servidor.update(peer, nomearquivo);
		System.out.println(resposta);
		reader.close();
	}

	private static HashSet<Peerold> opcaoSearch(Peerold peer, ServerInterface servidor) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Digite o nome do arquivo: ");
		String nomearquivo = reader.readLine();

		HashSet<Peerold> peersComArquivo = servidor.search(nomearquivo);
		System.out.println("Peers com o arquivo: ");
		for (Peerold no : peersComArquivo) {
			System.out.println("Peer de nome: " + no.getNome() + " e id: " + no.getId());
		}
		reader.close();
		return peersComArquivo;
	}

	private static void opcaoDownload(Peerold peer, ServerInterface servidor) throws Exception {
		
	}
}
