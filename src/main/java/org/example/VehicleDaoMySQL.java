package org.example;

import java.sql.*;
import java.util.ArrayList;

public class VehicleDaoMySQL implements vehicleDAO {

  //Dades de connexió a la base de dades
  private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
  private static final String DB_ROUTE = "jdbc:oracle:thin:@localhost:1521:xe";
  private static final String DB_USER = "system";
  private static final String DB_PASS = "12345";

  //Sentències
  private static final String SELECT_FROM_VEHICLES = "SELECT * FROM vehicles_new";
  private static final String BUSCA_VEHICLES = "SELECT * FROM vehicles_new WHERE UPPER(license_plate) = UPPER(?)";
  private static final String INSERT_VEHICLES = "INSERT INTO vehicles_new (license_plate, type, times_used, is_competition, electric, has_trunk, number_of_doors) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?)";
  private static final String UPDATE_VEHICLES = "UPDATE vehicles_new SET license_plate = ?, is_competition = ?, electric = ?, has_trunk = ?, number_of_doors = ? WHERE license_plate = ?";
  private static final String DELETE_VEHICLE = "DELETE FROM vehicles_new WHERE license_plate = ?";


  //Connexió a la base de dades
  private Connection conn;

  public VehicleDaoMySQL() {
    try {
      Class.forName(DB_DRIVER);
      conn = DriverManager.getConnection(DB_ROUTE, DB_USER, DB_PASS);
    } catch (Exception e) {
      System.out.println("S'ha produit un error en intentar connectar amb la base de dades. Revisa els paràmetres");
      System.out.println(e);
    }
  }

  @Override
  public Vehicle getVehicle(String licensePlate) throws SQLException {
    Vehicle vehicleResult = null;


    // Preparar la consulta a la base de datos
    PreparedStatement ps = conn.prepareStatement(BUSCA_VEHICLES);
    ps.setString(1, licensePlate.trim()); // Asegurarse de eliminar espacios en blanco
    System.out.println("Parámetro de matrícula: " + licensePlate);

    // Ejecutar la consulta y recorrer los resultados
    ResultSet rs = ps.executeQuery();

    if (!rs.isBeforeFirst()) {  // Si no hay resultados
      System.out.println("No se encontraron resultados en la consulta para la matrícula: " + licensePlate);
      return null;
    } else {
      System.out.println("Consulta exitosa, procesando resultados.");
    }

    if (rs.next()) {
      String type = rs.getString("type").trim();  // Eliminar posibles espacios en blanco
      System.out.println("Tipo de vehículo encontrado: " + type);

      int timesUsed = rs.getInt("times_used");
      boolean isCompetition = "T".equals(rs.getString("is_competition").trim());

      switch (type.toLowerCase()) {
        case "car":
          int numberOfDoors = rs.getInt("number_of_doors");
          vehicleResult = new Car(licensePlate, timesUsed, isCompetition, numberOfDoors, type);
          break;
        case "motorbike": // Asegúrate que este es el valor que esperas
          boolean hasTrunk = "T".equals(rs.getString("has_trunk").trim());
          vehicleResult = new Motorbike(licensePlate, timesUsed, isCompetition, hasTrunk, type);
          break;
        case "bicycle":
          boolean isElectric = "T".equals(rs.getString("electric").trim());
          vehicleResult = new Bicycle(licensePlate, timesUsed, isCompetition, isElectric, type);
          break;
        default:
          System.out.println("Tipo de vehículo no reconocido: " + type);
          break;
      }
    }

    return vehicleResult;
  }

  @Override
  public ArrayList<Vehicle> getVehicles() throws SQLException {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    PreparedStatement ps = conn.prepareStatement(SELECT_FROM_VEHICLES);
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      // Obtener el tipo de vehículo
      String type = rs.getString("type");
      if (type == null) {
        System.out.println("El tipo de vehículo es nulo en la base de datos, omitiendo este registro.");
        continue;  // Salta a la siguiente iteración si el tipo es null
      }

      type = type.trim().toLowerCase();  // Eliminar posibles espacios y convertir a minúsculas
      String licensePlate = rs.getString("license_plate");
      int timesUsed = rs.getInt("times_used");
      boolean isCompetition = "T".equals(rs.getString("is_competition"));

      Vehicle vehicle = null;

      switch (type) {
        case "car":
          int numberOfDoors = rs.getInt("number_of_doors");
          vehicle = new Car(licensePlate, timesUsed, isCompetition, numberOfDoors, type);
          break;
        case "motorbike":
          boolean hasTrunk = "T".equals(rs.getString("has_trunk"));
          vehicle = new Motorbike(licensePlate, timesUsed, isCompetition, hasTrunk, type);
          break;
        case "bicycle":
          boolean isElectric = "T".equals(rs.getString("electric"));
          vehicle = new Bicycle(licensePlate, timesUsed, isCompetition, isElectric, type);
          break;
        default:
          System.out.println("Tipo de vehículo desconocido: " + type);
          break;
      }

      if (vehicle != null) {
        vehicles.add(vehicle);
      }
    }

    return vehicles;
  }

  @Override
  public void saveVehicle(Vehicle v) throws SQLException {
    if (conn == null) {
      System.err.println("No es pot guardar el vehicle perquè la connexió a la base de dades no s'ha inicialitzat.");
      return;
    }

    PreparedStatement ps = conn.prepareStatement(INSERT_VEHICLES);

    ps.setString(1, v.getLicensePlate());

    String type = "";
    if (v instanceof Car) {
      type = "car";
    } else if (v instanceof Motorbike) {
      type = "motorbike";
    } else if (v instanceof Bicycle) {
      type = "bicycle";
    }
    ps.setString(2, type);

    ps.setInt(3, v.getTimesUsed());
    ps.setString(4, v.getisCompetetion() ? "T" : "F");

    // Variables para el control del tipo de vehículo
    if (v instanceof Car) {
      Car car = (Car) v;
      ps.setString(5, "F");
      ps.setString(6, "F");
      ps.setInt(7, car.getNumberDoors()); // Guardar el número de puertas correctamente
    } else if (v instanceof Motorbike) {
      Motorbike motorbike = (Motorbike) v;
      ps.setString(5, "F");
      ps.setString(6, motorbike.getishas_Trunk() ? "T" : "F");
      ps.setNull(7, Types.INTEGER); // Las motos no tienen puertas
    } else if (v instanceof Bicycle) {
      Bicycle bicycle = (Bicycle) v;
      ps.setString(5, bicycle.getisElectricalOrNonElectrical() ? "T" : "F");
      ps.setString(6, "F");
      ps.setNull(7, Types.INTEGER); // Las bicis no tienen puertas
    }

    ps.executeUpdate();
    System.out.println("Vehicle guardat correctament.");
  }

  @Override
  public void updateVehicle(Vehicle v) throws SQLException {
    PreparedStatement ps = conn.prepareStatement(UPDATE_VEHICLES);

    ps.setString(1, v.getLicensePlate());
    ps.setBoolean(2, v.getisCompetetion());

    if (v instanceof Car) {
      Car car = (Car) v;
      System.out.println("Número de puertas: " + car.getNumberDoors());
      ps.setBoolean(3, false);  // Els coches no són elèctrics
      ps.setBoolean(4, false);  // Els coches no tenen maleter
      ps.setInt(5, car.getNumberDoors());
    } else if (v instanceof Motorbike) {
      Motorbike motorbike = (Motorbike) v;
      ps.setBoolean(3, false);  // Les motos no són electricas
      ps.setBoolean(4, motorbike.getishas_Trunk());
      ps.setNull(5, Types.INTEGER);  // Les motos no tenen puertas
    } else if (v instanceof Bicycle) {
      Bicycle bicycle = (Bicycle) v;
      ps.setBoolean(3, bicycle.getisElectricalOrNonElectrical());
      ps.setBoolean(4, false);  // Les bicicletes no tenen maleter
      ps.setNull(5, Types.INTEGER); // Les bicicletes no tenen portes
    }

    ps.setString(6, v.getLicensePlate()); // On actualitzem per placa
    ps.executeUpdate();
  }

  public void deleteVehicle(String licensePlate) throws SQLException {
    PreparedStatement ps = conn.prepareStatement(DELETE_VEHICLE);
    ps.setString(1, licensePlate); // Asignar la matrícula del vehículo que queremos eliminar
    int rowsAffected = ps.executeUpdate(); // Ejecutar la sentencia SQL de eliminación

    if (rowsAffected > 0) {
      System.out.println("Vehicle eliminat correctament.");
    } else {
      System.out.println("No s'ha trobat cap vehicle amb aquesta matrícula per eliminar.");
    }
  }
}