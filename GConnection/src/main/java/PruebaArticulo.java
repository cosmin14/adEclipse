import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class PruebaArticulo {
	
	public static void main(String[] args) throws SQLException {
		
		Menu();
		
	}
	
	public static void Menu() throws SQLException {
	    
		Scanner lector = new Scanner ( System.in );
	    
		int opcion = 0;
		do {
			opcion = Integer.parseInt(JOptionPane.showInputDialog(""
					+ "\t===============================\n"
					+ "\t              MENU 			\n"
					+ "\t===============================\n"
					+ "\t      1) Listar Articulos\n"
		    		+ "\t      2) Lista Categorias\n"
		    		+ "\t      3) Nuevo Articulo\n"
		    		+ "\t      4) Eliminar Articulo\n"
		    		+ "\t      0) Salir\n"
		    		+ "Seleccion: "));
			
			/*System.out.println("\t================================\n"
							 + "\t              MENU 				\n"
							 + "\t================================");
			System.out.println ( ""
		    		+ "\t      1) Listar Articulos\n"
		    		+ "\t      2) Lista Categorias\n"
		    		+ "\t      3) Nuevo Articulo\n"
		    		+ "\t      4) Eliminar Articulo\n"
		    		+ "\t      0) Salir"
		    		);
		    System.out.print ( "Seleccion: " );*/
		    
		    //opcion = Integer.parseInt(lector.nextLine());
			
		    switch ( opcion ) {
		      case 1:
		    	  System.out.println ( "\nListando articulos...\n" );
		    	  JOptionPane.showMessageDialog(null, listArticlos());
			      //listArticlos();
			      break;
		      case 2:
		    	  System.out.println ( "\nListando categorias...\n" );
		    	  JOptionPane.showMessageDialog(null, listCategorias());
			      //listCategorias();
			      break;
		      case 3:
			      newArticle();
			      break;
		      case 4:
			      deleteArticle();
			      break;
		      case 0:
			      System.out.println ( "Cerrando app..." );
			      break;
		      default:
			      System.err.println ( "Unrecognized option" );
			      break;
		    }
		} while ( opcion  != 0);
	    
		
	  }
	
	public static Connection connect() throws SQLException{
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost/dbprueba","1DAM","1dam");
		return connection;
	}
	
	
	public static String listArticlos() throws SQLException{
		
		//Llamo directamente a la funcion connect() que crea la conexion y devuelve variable de tipo Connection.
		Statement statement = (Statement) connect().createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM articulo");
		String resultado = "";
		while (resultSet.next()) {
			//System.out.println(resultSet.getString("id")+ " - " + resultSet.getString("nombre"));
			resultado += resultSet.getString("id")+ " - " + resultSet.getString("nombre") + " - " + resultSet.getString("precio") + "�\n";
		}
		//JOptionPane.showMessageDialog(null, resultado);
		
		connect().close();
		return resultado;
	}
	
	
	public static String listCategorias() throws SQLException{
	
		//Llamo directamente a la funcion connect() que crea la conexion y devuelve variable de tipo Connection.
		Statement statement = (Statement) connect().createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM categoria");
		String resultado = "";
		while (resultSet.next()) {
			//System.out.println(resultSet.getString("id") + " - " + resultSet.getString("nombre"));
			resultado += resultSet.getString("id") + " - " + resultSet.getString("nombre")+"\n";
		}
		//JOptionPane.showMessageDialog(null,resultado);
		connect().close();
		return resultado;
	}
	
	
	public static void newArticle() throws SQLException{
		
		String nombre, precioStr, categoriaStr;
		BigDecimal precio;
		int categoria;
		
		Scanner lector = new Scanner(System.in);
		
//		System.out.print("Introduce nombre: ");
//		nombre = lector.nextLine();
//		
//		System.out.print("Introduce precio: ");
//		precio = lector.nextInt();
//		lector.nextLine();
//		
//		System.out.println ( "\n   Categorias: " );
//		System.out.println ( "-------------------" );
//		listCategorias();
//		
//		System.out.println();
//		System.out.print("Introduce el id de una categoria: ");
//		categoria = lector.nextInt();
//		lector.nextLine();
		
		nombre = JOptionPane.showInputDialog("Introduce nombre");
		precioStr = JOptionPane.showInputDialog("Introduce precio");
		
		//listCategorias();
		
		categoriaStr = JOptionPane.showInputDialog(listCategorias() + "Introduce categoria");
		
		precio = new BigDecimal(precioStr);
		categoria = Integer.parseInt(categoriaStr);
		
		String query = ("INSERT INTO articulo (nombre, categoria, precio) VALUES (?, ?, ?)");
		
		PreparedStatement sentencia = connect().prepareStatement(query);
		sentencia.setString(1, nombre);
		sentencia.setInt(2, categoria);
		sentencia.setBigDecimal(3, precio);
		
		sentencia.execute();	
	}
	
	
	public static void deleteArticle() throws SQLException{
		int id;
		
		Scanner lector = new Scanner(System.in);
		
		System.out.println ( "\n    Articulos: " );
		System.out.println ( "-------------------" );
		listArticlos();
		System.out.println();
		
		System.out.print("Introduce el id del articulo a eliminar: ");
		id = Integer.parseInt(lector.nextLine());
		
		String query = ("DELETE FROM `articulo` WHERE id="+id);
		PreparedStatement sentencia = connect().prepareStatement(query);
		sentencia.execute();
		
		System.out.println("\nEliminando articulo...\n");
		
		System.out.println ( "\n    Articulos actualizados: " );
		System.out.println ( "---------------------------------\n" );
		listArticlos();
		System.out.println();
		
		
	}
	
	
}
