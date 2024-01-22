package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//1:38:11

public class Logic {
	static Scanner kb = new Scanner(System.in);
	
//conexão
	public static Connection connect() throws SQLException {
		String USER = "pc";
		String PASSWORD = "()fabricinho111";
		String URL = "jdbc:mysql://localhost:3306/jmysql?useSSL=false&allowPublicKeyRetrieval=true";

		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		System.out.println();
		return connection;
	}
	
	
	public static void disconnect(Connection conn) {
		if(conn != null)
			try {
				conn.close();
			} catch(SQLException e) {
				System.out.println("Não foi possível fechar a conexão");
				e.printStackTrace();
			}
	}
	

//crud
	public static void create() {
		System.out.print("Nome do produto: ");
		String name = kb.nextLine();
		
		System.out.print("Preço do produto: ");
		float price = kb.nextFloat();
		
		System.out.print("Quantidade em estoque: ");
		short stock = kb.nextShort();
		
		String CREATE = 
			"insert into produtos (nome, preco, estoque) values (?, ?, ?)";
		
		try {
			Connection conn = connect();
			PreparedStatement save = conn.prepareStatement(CREATE);
			
			save.setString(1, name);
			save.setFloat(2, price);
			save.setShort(3, stock);
			
			save.executeUpdate();
			save.close();
			
			disconnect(conn);
			System.out.println(
				"O produto " + name + " foi criado e inserido com sucesso");
			
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("erro criando produto");
			System.exit(-12);
		}
	}
	

	public static void read() {
		String SEARCH_ALL = "select * from produtos";
		
		try {
			Connection conn = connect();
			PreparedStatement products = conn.prepareStatement(SEARCH_ALL, ResultSet.TYPE_SCROLL_INSENSITIVE);
			ResultSet res = products.executeQuery();

			if(res.next()) {
				System.out.println("Listando Produtos...");
				System.out.println();
				
				while(res.next()) {
					System.out.println("ID: " + res.getInt(1));
					System.out.println("Produtos: " + res.getString(2));
					System.out.println("Preço: " + res.getFloat(3));
					System.out.println("Estoque: " + res.getInt(4));
					System.out.println("-----------------------------");
				}
				
			} else {
				System.out.println("Não existem produtos cadastrados");
			}
			products.close();
			disconnect(conn);
			
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao buscar produtos");
			System.exit(-12);
		}
	}
	
	
	public static void update() {
		System.out.print("Código do produto: ");
		byte id = (byte) Integer.parseInt(kb.nextLine());
		
		String SEARCH_ID = "select * from produtos where id=?";
		
		try {
			Connection conn = connect();
			PreparedStatement prod = conn.prepareStatement(SEARCH_ID);
			prod.setByte(1, id);
			ResultSet res = prod.executeQuery();
			
			if(res.next()) {
				System.out.print("Nome do produto: ");
				String name = kb.nextLine();
				
				System.out.print("Preço do produto: ");
				float price = kb.nextFloat();
				
				System.out.print("Quantidade em estoque: ");
				short stock = kb.nextShort();
				
				String UPDATE = 
					"update produtos set nome=?, preco=?, estoque=? where id=?";
				PreparedStatement upd = conn.prepareStatement(UPDATE);
				
				upd.setString(1, name);
				upd.setFloat(2, price);
				upd.setShort(3, stock);
				upd.setByte(4, id);
				
				upd.executeUpdate();
				upd.close();
				
				disconnect(conn);
				System.out.println(
						"O produto " + name + " foi atualizado com sucesso");
				
			} else {
				System.out.println("Não existe produto com esse id");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao buscar produtos");
			System.exit(-12);
		}
	}
	
	
	public static void destroy() {
		String DESTROY = "delete from produtos where id=?";
		String SEARCH_ID = "select * from produtos where id=?";
		
		System.out.print("Código do produto: ");
		byte id = (byte) Integer.parseInt(kb.nextLine());
		
		try {
			Connection conn = connect();
			PreparedStatement prod = conn.prepareStatement(SEARCH_ID);
			prod.setByte(1, id);
			ResultSet res = prod.executeQuery();
			
			if(res.next()) {
				PreparedStatement del = conn.prepareStatement(DESTROY);
				
				del.setByte(1, id);
				
				del.executeUpdate();
				del.close();
				
				disconnect(conn);
				System.out.println(
						"O produto foi deletado com sucesso");
				
			} else {
				System.out.println("Não existe produto com esse id");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("erro ao deletar produto");
			System.exit(-12);
		}
	}
	
	
// "main"
	public static void menu() {
		System.out.println("~~~~~~~~ Gerenciamento de Produtos ~~~~~~~~");
		System.out.println("Opções:");
		System.out.println("1. Inserir Produtos");
		System.out.println("2. Listar Produtos");
		System.out.println("3. Atualizar Produtos");
		System.out.println("4. Deletar Produtos");
		System.out.println();
		System.out.print("Selecione a opção escolhida: ");
		
		byte option = Byte.parseByte(kb.nextLine());
		if(option == 1) {
			create();
		} else if(option == 2) {
			read();
		} else if(option == 3) {
			update();
		} else if(option == 4) {
			destroy();
		} else {
			System.out.println("Opção inválida");
		}
	}
}
