package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.alura.jdbc.factory.ConnectionFactory;

public class PruebaDelete {

  public static void main(String[] args) throws SQLException {
    
    // Se establece una conexión a la base de datos.
		Connection conexion = new ConnectionFactory().recuperaConexion();

		// Se crea un objeto Statement para enviar comandos SQL a la base de datos.
		Statement statement = conexion.createStatement();

		// Se ejecuta una consulta SQL que elimina un registro específico de la tabla.
		statement.execute("DELETE FROM PRODUCTO WHERE ID = 99");

		System.out.println(statement.getUpdateCount());
  }
  
}
