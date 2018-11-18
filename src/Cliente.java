import java.io.DataOutputStream;

public class Cliente {
	private String nome;
	private DataOutputStream out;
	private String ip;
	private int porta;
	
	public Cliente(DataOutputStream out){
		this.out = out;
		this.nome = "";
	}
	
	public DataOutputStream getOut() {
		return out;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
