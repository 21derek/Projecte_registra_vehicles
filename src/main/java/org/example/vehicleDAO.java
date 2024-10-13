package org.example;

import java.sql.SQLException;
import java.util.ArrayList;

public interface vehicleDAO {
  /**
   * Intenta retornar un Vehicle donat un codiVehicle passat com a paràmetre
   * @param licensePlate clau que s'utilitza per seleccionar el Vehicle demanat.
   * @return El Vehicle que correspont a licensePlate. Si el vehicle no existeix o n'hi ha més d'un torna null.
   * @throws SQLException
   */
  Vehicle getVehicle(String licensePlate) throws SQLException;

  /**
   * Es recuperen tots els Vehicles de la base de dades i ens retornen en una llista
   * @return la llista de vehicles que es troben a la base de dades.
   * @throws SQLException
   */
  ArrayList<Vehicle> getVehicles() throws SQLException;

  /**
   * Guarda el Vehicle v a la base de dades. Si el vehicle ja existeix ens llança una excepció.
   * @param v Vehicle a guardar.
   * @throws SQLException
   */
  void saveVehicle(Vehicle v) throws SQLException;

  /**
   * Actualitza els valors del Vehicle v als camps del Vehicle de la BD que té el mateix licensePlate
   * @param v Vehicle que en base al seu licensePlate s'actualitzarà a la BD
   * @throws SQLException
   */
  void updateVehicle(Vehicle v) throws SQLException;

  /**
   * Elimina el Vehicle amb la matrícula passada com a paràmetre
   * @param licensePlate matrícula del Vehicle a eliminar
   * @throws SQLException
   */
  void deleteVehicle(String licensePlate) throws SQLException;
}

