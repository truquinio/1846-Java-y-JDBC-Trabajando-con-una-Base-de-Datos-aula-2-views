package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

  final private Connection conexion;

  // CONSTR
  public ProductoDAO(Connection conexion) {
    this.conexion = conexion;
  }

  // >> GUARDAR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

  public void guardar(Producto producto) throws SQLException {

    // Usa la conexión desde el constructor
    try (conexion) {

      // Crea una conexión a la BbDd utilizando la clase ConnectionFactory
      // try (Connection conexion = new ConnectionFactory().recuperaConexion()) {
      // conexion.setAutoCommit(false);

      // Crea un objeto PreparedStatement a partir de la conexión
      final PreparedStatement statement = conexion.prepareStatement("INSERT INTO PRODUCTO" +
          "(nombre, descripcion, cantidad)"
          + "VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS);

      try (statement) {
        // Ejecuta el registro de un producto en la BbDd con los datos proporcionados
        ejecutaRegistro(producto, statement);

        // ejecutaRegistro(nombre, descripcion, cantidadParaGuardar, statement);

        // Si no hubo excepciones, se hace commit de la transacción
        conexion.commit();
        System.out.println("COMMIT");
      } catch (Exception e) {

        // Si ocurre una excepción, se hace rollback de la transacción
        conexion.rollback();
        System.out.println("ROLLBACK");
        throw e;
      }
    }
  }

  // Este método establece los valores de los parámetros en la consulta SQL
  // proporcionada, ejecuta la consulta y recupera las claves generadas por la
  // inserción.
  private void ejecutaRegistro(Producto producto, PreparedStatement statement)
      throws SQLException {

    statement.setString(1, producto.getNombre());
    statement.setString(2, producto.getDescripcion());
    statement.setInt(3, producto.getCantidad());

    // Ejecuta la consulta SQL
    statement.execute();

    // Recupera las claves generadas por la inserción
    final ResultSet resultset = statement.getGeneratedKeys();

    try (resultset) {

      // Imprime las claves generadas por la inserción
      while (resultset.next()) {

        producto.setId(resultset.getInt(1));

        System.out.println(String.format(
            "Fue insertado el producto %s", producto));
      }
    }
  }
}