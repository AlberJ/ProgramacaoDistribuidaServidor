import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecebeMensagem extends Thread {
	private Socket socket;
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private static Cliente cliente;
	
	public RecebeMensagem(Socket s) {
		this.socket = s;
	}

	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String msg = "";
			String linha;
			String comandos[] = null;
			String nome = "";
			
			
			this.cliente.setNome(nome);
			out.writeUTF("Bem vindo " + cliente.getNome());
			
			
			do {
//				if (!nome.equals("")) {
					linha = in.readUTF();
					msg = linha;
					System.out.println(msg);
					comandos = linha.split(":");
					for (String c : comandos) {
						c.trim();
					}

					if (!msg.equals("")) {
						switch (comandos[0]) {
						case "bye":
							msg = comandos[0];
							System.out.println("pegou comando bye!");
							break;
						case "-all":
							msg = comandos[0];
							System.out.println("pegou comando -all!");
							break;
						case "-user":
							msg = comandos[0];
							System.out.println("pegou comando -user!");
							break;
						case "list":
							msg = comandos[0];
							System.out.println("pegou comando list!");
							break;
						case "rename": // ISSO AKI FICA NO LADO DO SERVIDOR E NO
										// CLIENTE
							msg = comandos[0];
							System.out.println("pegou comando rename!");
							break;

						default:
							System.out.println("Comando inválido!");
						}

						// SERVIDOR ECOA MENSAGEM
						out.writeUTF("eco " + msg);
					}
//				} else {
//					nome = in.readUTF();
//					out.writeUTF("Bem vindo " + nome);
//				}

			} while (!comandos[0].equals("bye"));

		} catch (IOException ex) {
			Logger.getLogger(RecebeMensagem.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public boolean addCliente(Cliente cli){
		for (Cliente c : this.clientes){
			if(c.getNome().equals(cli.getNome())){
				return false;
			}else{
				cliente = cli;
				clientes.add(cli);
				return true;
			}				
		}
		return false;
	}
	
	public void removeCliente(Cliente cli){
		for (Cliente c : this.clientes){
			if(c.getNome().equals(cli.getNome())){
				clientes.remove(c);
			}
		}
	}
}
