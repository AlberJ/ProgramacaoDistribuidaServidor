import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorTCP extends Thread {
	private static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private static Socket socket;
	private static ServerSocket servidor;

	public ServidorTCP(Socket socket) {
		ServidorTCP.socket = socket;
	}

	public boolean addCliente(Cliente cli) {
		for (Cliente c : ServidorTCP.clientes) {
			if (c.getNome().equals(cli.getNome())) {
				return false;
			}
		}
		clientes.add(cli);
		return true;
	}

	public void removeCliente(Cliente cli) {
		for (Cliente c : ServidorTCP.clientes) {
			if (c.getNome().equals(cli.getNome())) {
				clientes.remove(c);
			}
		}
	}

	public String listarCliente() {
		String lista = "Lista de Usuarios: \n";
		for (Cliente c : ServidorTCP.clientes) {
			lista += c.getNome() + "\n";
		}
		return lista;
	}

	public boolean renameCliente(String novo, String antigo) {
		// PROCURA SE O NOVO NOME JA EXITE
		for (Cliente c : clientes) {
			if (c.getNome().equals(novo)) {
				return false;
			}
		}
		// PROCURA PELO USUARIO NA LISTA PARA MUDAR SEU NOME
		for (Cliente c : clientes) {
			if (c.getNome().equals(antigo)) {
				c.setNome(novo);
				return true;
			}
		}
		// NÃO ACHOU NENHUM USUARIO COM O ANTIGO NOME
		return false;
	}

	public void enviaMensagemAll(String m, Cliente emisor) {
		for (Cliente c : clientes) {
			if (!c.getNome().equals(emisor.getNome())) {
				try {
					c.getOut().writeUTF(m);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void enviaMensagemUser(String m, String remetente, Cliente emisor) {
		boolean inexistente = true;
		for (Cliente c : clientes) {
			if (c.getNome().equals(remetente)) {
				try {
					c.getOut().writeUTF(m);
					inexistente = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (inexistente) {
			try {
				emisor.getOut().writeUTF("Usuário destino inexistente.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			servidor = new ServerSocket(6500);
			System.out.println("Servidor rodando...");

			while (true) {
				socket = servidor.accept();

				Thread thread = new ServidorTCP(socket);
				thread.start();
			}

		} catch (IOException e) {
			Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			Cliente cli = new Cliente(out);
			String msg = "";
			String linha;
			String comandos[] = null;

			while (cli.getNome().equals("")) {
				cli.setNome(in.readUTF());

				if (addCliente(cli)) {
					out.writeUTF("Renomeado com sucesso.");
				} else {
					msg = "Nome de usuário já em uso.";
					out.writeUTF(msg);
					cli.setNome("");
				}
			}
			
			out.writeUTF("Bem vindo "+cli.getNome());
			cli.setIp(socket.getInetAddress().getHostAddress());
			cli.setPorta(socket.getPort());
			do {
				linha = in.readUTF();
				msg = linha;
				comandos = linha.split(":");
				for (String c : comandos) {
					c.trim();
				}

				if (!msg.equals("")) {
					switch (comandos[0]) {
					case "bye":
						msg = comandos[0];
						out.writeUTF(msg);
						break;
					case "-all":
						LocalDateTime l = LocalDateTime.now();

						msg = cli.getIp() + ":" + cli.getPorta() + "/~" + comandos[1] + ": "
								+comandos[2]
								+ l.format(DateTimeFormatter.ofPattern("HH")) + "h"
								+ l.format(DateTimeFormatter.ofPattern("mm")) + " "
								+ l.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

						enviaMensagemAll(msg, cli);
						l = null;
						break;
					case "-user":
						LocalDateTime t = LocalDateTime.now();

						msg = cli.getIp() + ":" + cli.getPorta() + "/~" + comandos[1] + ": " 
								+ comandos[3]
								+ t.format(DateTimeFormatter.ofPattern("HH")) + "h"
								+ t.format(DateTimeFormatter.ofPattern("mm")) + " "
								+ t.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

						enviaMensagemUser(msg, comandos[2], cli);
						t = null;
						break;
					case "list":
						msg = listarCliente();
						out.writeUTF(msg);
						break;
					case "rename":
						if (renameCliente(comandos[1], comandos[2])) {
							msg = "Renomeado com sucesso.";
						} else {
							msg = "Nome de usuário já em uso.";
						}
						out.writeUTF(msg);
						break;

					default:
						out.writeUTF("Comando inválido!");
					}
				}
			} while (!comandos[0].equals("bye"));

			removeCliente(cli);
			ServidorTCP.socket.close();
			System.out.println(cli.getNome()+" desconectou!");
			
		} catch (IOException e) {
			System.out.println("Falha na Conexao..." + " IOException: " + e);
		}
	}
}
