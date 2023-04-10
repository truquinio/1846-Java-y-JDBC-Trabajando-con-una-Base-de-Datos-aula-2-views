package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

  // declara una variable dataSource del tipo DataSource
  private DataSource dataSource;

  public ConnectionFactory() {
    // crea un nuevo objeto ComboPooledDataSource
    var pooledDataSource = new ComboPooledDataSource();

    // configura el objeto pooledDataSource con los valores necesarios para
    // conectarse a la base de datos
    pooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/control_de_stock?useTimeZone=true&serverTimeZone=UTC");
    pooledDataSource.setUser("root");
    pooledDataSource.setPassword("root");
    pooledDataSource.setMaxPoolSize(10);

    // asigna el objeto pooledDataSource a la variable dataSource
    this.dataSource = pooledDataSource;
  }

  public Connection recuperaConexion() throws SQLException {

    // devuelve una conexión a la base de datos a través del objeto dataSource
    return this.dataSource.getConnection();
  }
}



    // public Connection recuperaConexion() throws SQLException {
    // Establece una conexión con la base de datos "control_de_stock" c/ usuario
    // "root" y pass "root"
    // return DriverManager.getConnection(
    // "jdbc:mysql://localhost:3306/control_de_stock?useTimeZone=true&serverTimeZone=UTC",
    // "root", "root");}