package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.factory.ConnectionFactory;

public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
		// Establecer conexión a la base de datos
		try (Connection conexion = new ConnectionFactory().recuperaConexion();
				PreparedStatement statement = conexion.prepareStatement("UPDATE PRODUCTO SET " +
						" NOMBRE = ?" +
						", DESCRIPCION = ?" +
						", CANTIDAD = ?" +
						" WHERE ID = ?")) {
			// Establecer valores de los parámetros en la instrucción SQL
			statement.setString(1, nombre);
			statement.setString(2, descripcion);
			statement.setInt(3, cantidad);
			statement.setInt(4, id);

			// Ejecutar la instrucción SQL
			int updateCount = statement.executeUpdate();

			// Devolver el número de filas afectadas por la actualización
			return updateCount;
		} catch (SQLException e) {
			// Si ocurre una excepción, se hace rollback de la transacción y se relanza la excepción
			// para que sea manejada por el código que llamó a este método
			throw e;
		}
	}

	// >> ELIMINAR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public int eliminar(Integer id) throws SQLException {

		// Crea una nueva conexión a la BbDd
		try (Connection conexion = new ConnectionFactory().recuperaConexion();

				// Crea un objeto PreparedStatement a partir de la conexión establecida y
				// elimina el registro con el ID proporcionado
				PreparedStatement statement = conexion.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?")) {

			// Establece el valor del parámetro en la consulta SQL
			statement.setInt(1, id);

			// Ejecuta la consulta y obtiene el número de registros afectados
			int updateCount = statement.executeUpdate();

			// Devuelve el número de registros afectados por la consulta
			return updateCount;
		} catch (SQLException e) {

			// Si ocurre una excepción, se hace rollback de la transacción
			throw e;
		}
	}

	// >> LISTAR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/**
 * Obtiene la lista de todos los productos de la base de datos.
 * @return una lista de objetos Map que contiene los datos de cada producto.
 * @throws SQLException si ocurre un error al acceder a la base de datos.
 */
public List<Map<String, String>> listar() throws SQLException {
	
	// Crea una nueva conexión a la BbDd
	try (Connection conexion = new ConnectionFactory().recuperaConexion();
			PreparedStatement statement = conexion
					.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");
			ResultSet resultset = statement.executeQuery()) {

		// Crea un objeto ArrayList para almacenar los resultados
		ArrayList<Map<String, String>> resultado = new ArrayList<>();

		// Itera sobre cada fila del objeto ResultSet y crea un objeto Map para
		// almacenar los valores
		while (resultset.next()) {
			// Crea un objeto Map para almacenar los valores de cada fila
			Map<String, String> fila = new HashMap<>();

			// Agrega los valores de cada columna de la fila al objeto Map
			fila.put("ID", String.valueOf(resultset.getInt("ID")));
			fila.put("NOMBRE", resultset.getString("NOMBRE"));
			fila.put("DESCRIPCION", resultset.getString("DESCRIPCION"));
			fila.put("CANTIDAD", String.valueOf(resultset.getInt("CANTIDAD")));

			// Agrega el objeto Map a la lista de resultados
			resultado.add(fila);
		}

		// Devuelve la lista de objetos Map<String, String>
		return resultado;
	}
}


	// >> GUARDAR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public void guardar(Map<String, String> producto) throws SQLException {

		// Establece los valores de los parámetros en la consulta SQL
		String nombre = producto.get("NOMBRE");
		String descripcion = producto.get("DESCRIPCION");
		Integer cantidad = Integer.valueOf(producto.get("CANTIDAD"));
		Integer maximoCantidad = 50;
	
		// Crea una conexión a la BbDd utilizando la clase ConnectionFactory
		try (Connection conexion = new ConnectionFactory().recuperaConexion()) {
			conexion.setAutoCommit(false);
	
			// Crea un objeto PreparedStatement a partir de la conexión
			try (PreparedStatement statement = conexion.prepareStatement("INSERT INTO PRODUCTO" +
					"(nombre, descripcion, cantidad)"
					+ "VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS)) {
	
				try {
					do {
						// Calcula la cantidad de productos a guardar en esta iteración
						int cantidadParaGuardar = Math.min(cantidad, maximoCantidad);
	
						// Ejecuta el registro de un producto en la BbDd con los datos proporcionados
						ejecutaRegistro(nombre, descripcion, cantidadParaGuardar, statement);
	
						// Actualiza la cantidad restante de productos a guardar
						cantidad -= maximoCantidad;
	
					} while (cantidad > 0);
	
					// Si no hubo excepciones, se hace commit de la transacción
					conexion.commit();
					System.out.println("COMMIT");
	
				} catch (SQLException e) {
	
					// Si ocurre una excepción, se hace rollback de la transacción
					conexion.rollback();
					System.out.println("ROLLBACK");
					throw e;
				}
			}
		}
	}

// Este método establece los valores de los parámetros en la consulta SQL proporcionada,
// ejecuta la consulta y recupera las claves generadas por la inserción.
private void ejecutaRegistro(String nombre, String descripcion, Integer cantidad, PreparedStatement statement)
			throws SQLException {

		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);

		// Ejecuta la consulta SQL
		statement.execute();

		// Recupera las claves generadas por la inserción
		final ResultSet resultset = statement.getGeneratedKeys();

		try (resultset) {

			// Imprime las claves generadas por la inserción
			while (resultset.next()) {
				System.out.println(String.format(
						"Fue insertado el producto de ID %d",
						resultset.getInt(1)));
			}
		}
	}
}