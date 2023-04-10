package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

public class ProductoController {
	// Define la clase ProductoController

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {

		// Define un método para modificar un producto en la base de datos,
		// recibiendo los parámetros nombre, descripción, cantidad e id.
		try (Connection conexion = new ConnectionFactory().recuperaConexion();
				PreparedStatement statement = conexion.prepareStatement("UPDATE PRODUCTO SET " +
						" NOMBRE = ?" +
						", DESCRIPCION = ?" +
						", CANTIDAD = ?" +
						" WHERE ID = ?")) {

			// Establece la conexión a la base de datos y prepara una sentencia SQL para
			// actualizar el producto con el ID dado
			statement.setString(1, nombre);
			statement.setString(2, descripcion);
			statement.setInt(3, cantidad);
			statement.setInt(4, id);

			// Establece los valores de los parámetros en la sentencia SQL
			int updateCount = statement.executeUpdate();

			// Ejecuta la sentencia SQL y devuelve el número de filas afectadas
			return updateCount;
		} catch (SQLException e) {

			// Si ocurre una excepción, hace un rollback de la transacción y relanza la
			// excepción para que sea manejada por el código que llamó al método
			throw e;
		}
	}

	// >> ELIMINAR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Define un método para eliminar un producto de la base de datos,
	// recibiendo el parámetro id.
	public int eliminar(Integer id) throws SQLException {

		try (Connection conexion = new ConnectionFactory().recuperaConexion();
				PreparedStatement statement = conexion.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?")) {

			// Establece la conexión a la base de datos y prepara una sentencia SQL para
			// eliminar el producto con el ID dado
			statement.setInt(1, id);

			// Establece el valor del parámetro en la sentencia SQL
			int updateCount = statement.executeUpdate();

			// Ejecuta la sentencia SQL y devuelve el número de filas afectadas
			return updateCount;
		} catch (SQLException e) {

			// Si ocurre una excepción, hace un rollback de la transacción y relanza la
			// excepción para que sea manejada por el código que llamó al método
			throw e;
		}
	}

	// >> LISTAR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/*
	 * Obtiene la lista de todos los productos de la base de datos.
	 * 
	 * @return una lista de objetos Map que contiene los datos de cada producto.
	 * 
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public List<Map<String, String>> listar() throws SQLException {

		// Crea una nueva conexión a la BbDd usando la clase ConnectionFactory
		try (Connection conexion = new ConnectionFactory().recuperaConexion();

				// Prepara una sentencia SQL con los campos ID, NOMBRE, DESCRIPCION y CANTIDAD
				PreparedStatement statement = conexion
						.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");

				// Ejecuta la sentencia SQL y almacena los resultados en un objeto ResultSet
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

	// >> GUARDAR PRODUCTO DAO >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public void guardar(Producto producto) throws SQLException {

		// Crea un objeto de tipo PersistenciaProducto y obtiene una conexión a la base
		// de datos
		ProductoDAO productoDao = new ProductoDAO(new ConnectionFactory().recuperaConexion());

		// Llama al método "guardarProducto" de PersistenciaProducto, pasando el objeto
		// Producto como parámetro.
		// Este método se encarga de realizar la inserción en la base de datos.
		productoDao.guardar(producto);
	}
}

/*
 * public void guardar(Producto producto) throws SQLException {
 * 
 * // Establece los valores de los parámetros en la consulta SQL
 * String nombre = producto.getNombre();
 * String descripcion = producto.getDescripcion();
 * Integer cantidad = producto.getCantidad();
 */

/*
 * public void guardar(Map<String, String> producto) throws SQLException {
 * 
 * // Establece los valores de los parámetros en la consulta SQL
 * String nombre = producto.get("NOMBRE");
 * String descripcion = producto.get("DESCRIPCION");
 * Integer cantidad = Integer.valueOf(producto.get("CANTIDAD"));
 * Integer maximoCantidad = 50;
 * 
 * try {
 * do {
 * // Calcula la cantidad de productos a guardar en esta iteración
 * int cantidadParaGuardar = Math.min(cantidad, maximoCantidad);
 * 
 * // Ejecuta el registro de un producto en la BbDd con los datos proporcionados
 * ejecutaRegistro(producto, statement);
 * 
 * // ejecutaRegistro(nombre, descripcion, cantidadParaGuardar, statement);
 * 
 * // Actualiza la cantidad restante de productos a guardar
 * cantidad -= maximoCantidad;
 * 
 * } while (cantidad > 0);
 */

/*
 * private void ejecutaRegistro(String nombre, String descripcion, Integer
 * cantidad, PreparedStatement statement) throws SQLException {
 * 
 * statement.setString(1, nombre);
 * statement.setString(2, descripcion);
 * statement.setInt(3, cantidad);
 * 
 * // Ejecuta la consulta SQL
 * statement.execute();
 * 
 * // Recupera las claves generadas por la inserción
 * final ResultSet resultset = statement.getGeneratedKeys();
 * 
 * try (resultset) {
 * 
 * // Imprime las claves generadas por la inserción
 * while (resultset.next()) {
 * System.out.println(String.format(
 * "Fue insertado el producto de ID %d",
 * resultset.getInt(1)));
 * }
 * }
 * }
 * }
 */

/*
 * SE MOVIÓ EL MÉTODO 'GUARDAR' Y 'EJECUTA REGISTRO' A PERSISTENCIA PRODUCTO
 * 
 * public void guardarProducto(Producto producto) throws SQLException {
 * // Crea una conexión a la BbDd utilizando la clase ConnectionFactory
 * try (Connection conexion = new ConnectionFactory().recuperaConexion()) {
 * conexion.setAutoCommit(false);
 * 
 * // Crea un objeto PreparedStatement a partir de la conexión
 * final PreparedStatement statement =
 * conexion.prepareStatement("INSERT INTO PRODUCTO" +
 * "(nombre, descripcion, cantidad)"
 * + "VALUES (?, ?, ?)",
 * Statement.RETURN_GENERATED_KEYS);
 * 
 * try (statement) {
 * // Ejecuta el registro de un producto en la BbDd con los datos proporcionados
 * ejecutaRegistro(producto, statement);
 * 
 * // ejecutaRegistro(nombre, descripcion, cantidadParaGuardar, statement);
 * 
 * // Si no hubo excepciones, se hace commit de la transacción
 * conexion.commit();
 * System.out.println("COMMIT");
 * } catch (Exception e) {
 * 
 * // Si ocurre una excepción, se hace rollback de la transacción
 * conexion.rollback();
 * System.out.println("ROLLBACK");
 * throw e;
 * }
 * }
 * }
 * 
 * // Este método establece los valores de los parámetros en la consulta SQL
 * // proporcionada, ejecuta la consulta y recupera las claves generadas por la
 * // inserción.
 * private void ejecutaRegistro(Producto producto, PreparedStatement statement)
 * throws SQLException {
 * 
 * statement.setString(1, producto.getNombre());
 * statement.setString(2, producto.getDescripcion());
 * statement.setInt(3, producto.getCantidad());
 * 
 * // Ejecuta la consulta SQL
 * statement.execute();
 * 
 * // Recupera las claves generadas por la inserción
 * final ResultSet resultset = statement.getGeneratedKeys();
 * 
 * try (resultset) {
 * 
 * // Imprime las claves generadas por la inserción
 * while (resultset.next()) {
 * 
 * producto.setId(resultset.getInt(1));
 * 
 * System.out.println(String.format(
 * "Fue insertado el producto %s", producto));
 * }
 * }
 * }
 */