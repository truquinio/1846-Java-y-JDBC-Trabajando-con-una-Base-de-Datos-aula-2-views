package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.SQLException;

import com.alura.jdbc.factory.ConnectionFactory;

public class PruebaConexion {

    public static void main(String[] args) throws SQLException {

        // Crea una nueva conexión a la base de datos
		Connection conexion = new ConnectionFactory().recuperaConexion();

        System.out.println("Cerrando la conexión");

        conexion.close();
    }
}
