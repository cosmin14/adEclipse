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
			
		    switch ( opcion ) {
		      case 1:
		    	  System.out.println ( "\nListando articulos...\n" );
		    	  JOptionPane.showMessageDialog(null, listArticlos());
			      break;
		      case 2:
		    	  System.out.println ( "\nListando categorias...\n" );
		    	  JOptionPane.showMessageDialog(null, listCategorias());
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
			resultado += resultSet.getString("id")+ " - " + resultSet.getString("nombre") + " - " + resultSet.getString("precio") + "€\n";
		}
		
		connect().close();
		return resultado;
	}
	
	
	public static String listCategorias() throws SQLException{
	
		//Llamo directamente a la funcion connect() que crea la conexion y devuelve variable de tipo Connection.
		Statement statement = (Statement) connect().createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM categoria");
		String resultado = "";
		while (resultSet.next()) {
			resultado += resultSet.getString("id") + " - " + resultSet.getString("nombre")+"\n";
		}
		connect().close();
		return resultado;
	}
	
	
	public static void newArticle() throws SQLException{
		
		String nombre, precioStr, categoriaStr;
		BigDecimal precio;
		int categoria;
		
		nombre = JOptionPane.showInputDialog("Introduce nombre");
		
		if (nombre == null) {
			Menu();
		}else{
			precioStr = JOptionPane.showInputDialog("Introduce precio");
			if (precioStr == null) {
				Menu();
			}else{
				categoriaStr = JOptionPane.showInputDialog(listCategorias() + "Introduce categoria");
				if (categoriaStr == null) {
					Menu();
				}else{
					String query = ("INSERT INTO articulo (nombre, categoria, precio) VALUES (?, ?, ?)");
					
					if (nombre.isEmpty() || precioStr.isEmpty() || categoriaStr.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
						newArticle();
					}else{
						
						try {
							precio = new BigDecimal(precioStr);
							categoria = Integer.parseInt(categoriaStr);
							
							PreparedStatement sentencia = connect().prepareStatement(query);
							sentencia.setString(1, nombre);
							sentencia.setInt(2, categoria);
							sentencia.setBigDecimal(3, precio);
							
							sentencia.execute();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error al guardar el articulo", "Error", JOptionPane.ERROR_MESSAGE);
						}//TRY
						
					}//IF
				}// IF categoria
			}// IF precio
		}// IF nombre
		
	}
	
	
	public static void deleteArticle() throws SQLException{
		String idStr;
		int id;
		
		idStr = JOptionPane.showInputDialog(listArticlos() + "Id del articulo a eliminar:");
		id = Integer.parseInt(idStr);
		
		String query = ("DELETE FROM `articulo` WHERE id="+id);
		
		if (idStr == null) {
			Menu();
		}else{
			try {				
				PreparedStatement sentencia = connect().prepareStatement(query);
				sentencia.execute();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al guardar el articulo", "Error", JOptionPane.ERROR_MESSAGE);
			}//TRY
		}
	}
	
	
}
