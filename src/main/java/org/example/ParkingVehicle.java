    package org.example;

    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.Scanner;

    // Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
    // then press Enter. You can now see whitespace characters in your code.
    public class ParkingVehicle {
      // Instancia del DAO para interactuar con la base de datos
      private static final VehicleDaoMySQL vehicleDaoMySQL = new VehicleDaoMySQL();

      public static void main(String[] args) {
        // Inicialización del Scanner para leer entradas del usuario
        Scanner lector = new Scanner(System.in);
        int opcio = 0;  // Variable para almacenar la opción elegida en el menú

        // Bucle principal del menú
        do {
          mostrarMenu();  // Mostrar las opciones del menú
          opcio = lector.nextInt();  // Leer la opción elegida por el usuario

          // Evaluar la opción elegida y ejecutar la acción correspondiente
          switch (opcio) {
            case 1:
              registrarVehicle(); break;  // Registrar un nuevo vehículo
            case 2:
              buscarVehiclePerMatricula(); break;  // Buscar un vehículo por matrícula
            case 3:
              filtrarVehiclesPerTipus(); break;  // Filtrar vehículos por tipo
            case 4:
              filtrarVehiclesDeCompeticio(); break;  // Filtrar vehículos de competición
            case 5:
              filtrarCotxesPerNombreDePortes(); break;  // Filtrar coches por número de puertas
            case 6:
              actualizarVehicle(); break;  // Actualizar un vehículo existente
            case 7:
              eliminarVehicle(); break;  // Eliminar un vehículo
            case -1:
              System.out.println("Bye...");  // Mensaje de salida
              break;
            default:
              System.out.println("Opció no vàlida");  // Mensaje de error para opción inválida
          }

        } while (opcio != -1);  // Repetir hasta que el usuario elija salir
      }

      // Método para mostrar el menú de opciones
      private static void mostrarMenu() {
        System.out.println("\n--- Menú ---");
        System.out.println("1. Registrar vehicle");
        System.out.println("2. Buscar vehicle per matrícula");
        System.out.println("3. Filtrar vehicles per tipus");
        System.out.println("4. Filtrar vehicles de competició");
        System.out.println("5. Filtrar cotxes per nombre de portes");
        System.out.println("6. Actualitzar vehicle");
        System.out.println("7. Eliminar vehicle");
        System.out.println();
        System.out.println("[-1] Sortir de l'aplicació");
      }

      // Método para registrar un nuevo vehículo
      private static void registrarVehicle() {
        Scanner lector = new Scanner(System.in);
        System.out.println("Introdueix el tipus de vehicle (car, motorbike, bicycle): ");
        String type = lector.nextLine();  // Leer el tipo de vehículo

        System.out.println("Introdueix la matrícula: ");
        String matricula = lector.nextLine();  // Leer la matrícula

        System.out.println("Introdueix el nombre de vegades que s'ha utilitzat el vehicle: ");
        int timesUsed = lector.nextInt();  // Leer el número de veces que se ha utilizado

        System.out.println("És de competició? (true/false): ");
        boolean isCompetition = lector.nextBoolean();  // Leer si es de competición

        Vehicle vehicle = null;  // Inicializar la variable del vehículo

        // Crear un vehículo según el tipo introducido
        switch (type.toLowerCase()) {
          case "car":
            System.out.println("Introdueix el nombre de portes: ");
            int numberDoors = lector.nextInt();  // Leer el número de puertas
            vehicle = new Car(matricula, timesUsed, isCompetition, numberDoors, type);  // Crear un coche
            break;
          case "motorbike":
            System.out.println("Té maleter? (true/false): ");
            boolean hasTrunk = lector.nextBoolean();  // Leer si tiene maletero
            vehicle = new Motorbike(matricula, timesUsed, isCompetition, hasTrunk, type);  // Crear una moto
            break;
          case "bicycle":
            System.out.println("És elèctrica? (true/false): ");
            boolean isElectric = lector.nextBoolean();  // Leer si es eléctrica
            vehicle = new Bicycle(matricula, timesUsed, isCompetition, isElectric, type);  // Crear una bicicleta
            break;
          default:
            System.out.println("Tipus de vehicle no vàlid.");  // Mensaje de error si el tipo es inválido
            return;
        }

        // Intentar registrar el vehículo en la base de datos
        try {
          vehicleDaoMySQL.saveVehicle(vehicle);
          System.out.println("Vehicle registrat correctament.");  // Mensaje de éxito
        } catch (SQLException e) {
          System.out.println("Error en registrar el vehicle: " + e.getMessage());  // Mensaje de error en caso de fallo
        }
      }

      // 2. Método para buscar un vehículo por matrícula
      private static void buscarVehiclePerMatricula() {
        Scanner lector = new Scanner(System.in);
        System.out.println("Introdueix la matrícula del vehicle a cercar: ");
        String matricula = lector.nextLine().trim();  // Leer la matrícula y eliminar espacios en blanco

        // Intentar buscar el vehículo en la base de datos
        try {
          Vehicle vehicle = vehicleDaoMySQL.getVehicle(matricula);
          if (vehicle != null) {
            System.out.println("Vehicle trobat: " + vehicle);  // Mostrar vehículo encontrado
          } else {
            System.out.println("Consulta SQL executada correctament, però no s'han trobat resultats.");  // Mensaje si no se encuentra el vehículo
          }
        } catch (SQLException e) {
          System.out.println("Error en cercar el vehicle: " + e.getMessage());  // Mensaje de error en caso de fallo
        }
      }

      // 3. Método para filtrar vehículos por tipo
      private static void filtrarVehiclesPerTipus() {
        Scanner lector = new Scanner(System.in);
        System.out.println("Introdueix el tipus de vehicle a filtrar (car, motorbike, bicycle): ");
        String type = lector.nextLine().trim().toLowerCase();  // Leer el tipo y eliminar espacios

        // Intentar obtener todos los vehículos de la base de datos
        try {
          ArrayList<Vehicle> vehicles = vehicleDaoMySQL.getVehicles();
          ArrayList<Vehicle> vehiclesFiltrats = new ArrayList<>();  // Lista para almacenar vehículos filtrados

          // Filtrar los vehículos según el tipo introducido
          for (Vehicle v : vehicles) {
            if (v.getType().equalsIgnoreCase(type)) {
              vehiclesFiltrats.add(v);  // Añadir vehículo filtrado a la lista
            }
          }

          // Mostrar los vehículos filtrados o un mensaje si no se encuentra ninguno
          if (vehiclesFiltrats.isEmpty()) {
            System.out.println("No s'han trobat vehicles del tipus: " + type);
          } else {
            System.out.println("Vehicles del tipus " + type + ":");
            for (Vehicle v : vehiclesFiltrats) {
              System.out.println(v);  // Mostrar vehículos filtrados
            }
          }
        } catch (SQLException e) {
          System.out.println("Error en filtrar vehicles: " + e.getMessage());  // Mensaje de error en caso de fallo
        }
      }

      // 4. Método para filtrar vehículos de competición
      private static void filtrarVehiclesDeCompeticio() {
        try {
          ArrayList<Vehicle> vehicles = vehicleDaoMySQL.getVehicles();  // Obtener todos los vehículos
          vehicles.stream()  // Usar streams para filtrar
                  .filter(Vehicle::getisCompetetion)  // Filtrar vehículos de competición
                  .forEach(System.out::println);  // Mostrar los vehículos filtrados
        } catch (SQLException e) {
          System.out.println("Error en filtrar vehicles de competició: " + e.getMessage());  // Mensaje de error en caso de fallo
        }
      }

      // 5. Método para filtrar coches por número de puertas
      private static void filtrarCotxesPerNombreDePortes() {
        Scanner lector = new Scanner(System.in);
        System.out.println("Introdueix el nombre de portes a filtrar: ");
        int doors = lector.nextInt();  // Leer el número de puertas

        // Intentar obtener todos los vehículos de la base de datos
        try {
          ArrayList<Vehicle> vehicles = vehicleDaoMySQL.getVehicles();
          vehicles.stream()  // Usar streams para filtrar
                  .filter(v -> v instanceof Car && ((Car) v).getNumberDoors() == doors)  // Filtrar coches según el número de puertas
                  .forEach(System.out::println);  // Mostrar los vehículos filtrados
        } catch (SQLException e) {
          System.out.println("Error en filtrar cotxes per nombre de portes: " + e.getMessage());  // Mensaje de error en caso de fallo
        }
      }

      // 6. Método para actualizar un vehículo
      private static void actualizarVehicle() {
        Scanner lector = new Scanner(System.in);
        System.out.println("Introdueix la matrícula del vehicle a actualitzar: ");
        String matricula = lector.nextLine();  // Leer la matrícula del vehículo a actualizar

        // Intentar buscar el vehículo en la base de datos
        try {
          Vehicle vehicle = vehicleDaoMySQL.getVehicle(matricula);
          if (vehicle != null) {
            System.out.println("Vehicle trobat: " + vehicle);  // Mostrar el vehículo encontrado
            System.out.println("Introdueix noves dades per actualitzar.");  // Mensaje para indicar actualización

            // Pedir al usuario nuevos datos para actualizar el vehículo
            System.out.println("Introdueix la nova matrícula (deixa buit per mantenir la mateixa): ");
            String novaMatricula = lector.nextLine();
            if (!novaMatricula.isEmpty()) {
              vehicle.setLicensePlate(novaMatricula);  // Actualizar la matrícula si se proporciona una nueva
            }

            System.out.println("Introdueix el nou nombre de vegades que s'ha utilitzat el vehicle (deixa buit per mantenir el mateix): ");
            String nouTimesUsedInput = lector.nextLine();
            if (!nouTimesUsedInput.isEmpty()) {
              int nouTimesUsed = Integer.parseInt(nouTimesUsedInput);
              vehicle.setTimesUsed(nouTimesUsed);  // Actualizar el número de veces que se ha utilizado
            }

            System.out.println("És de competició? (true/false, deixa buit per mantenir el mateix): ");
            String nouEsCompeticioInput = lector.nextLine();
            if (!nouEsCompeticioInput.isEmpty()) {
              boolean nouEsCompeticio = Boolean.parseBoolean(nouEsCompeticioInput);
              vehicle.setCompetetion(nouEsCompeticio);  // Actualizar si es de competición
            }

            // Si el vehículo es un coche, pedir el nuevo número de puertas
            if (vehicle instanceof Car) {
              System.out.println("Introdueix el nou nombre de portes (deixa buit per mantenir el mateix): ");
              String nouNumPortesInput = lector.nextLine();
              if (!nouNumPortesInput.isEmpty()) {
                int nouNumPortes = Integer.parseInt(nouNumPortesInput);
                ((Car) vehicle).setNumberDoors(nouNumPortes);  // Actualizar el número de puertas
              }
            }

            // Actualiza el vehículo en la base de datos
            vehicleDaoMySQL.updateVehicle(vehicle);
            System.out.println("Vehicle actualitzat correctament.");  // Mensaje de éxito
          } else {
            System.out.println("Vehicle no trobat.");  // Mensaje si el vehículo no se encuentra
          }
        } catch (SQLException e) {
          System.out.println("Error en actualitzar el vehicle: " + e.getMessage());  // Mensaje de error en caso de fallo
        } catch (NumberFormatException e) {
          System.out.println("Error en format de nombre: " + e.getMessage());  // Mensaje de error si el formato de número es incorrecto
        }
      }

      // 7. Método para eliminar un vehículo
      private static void eliminarVehicle() {
        Scanner lector = new Scanner(System.in);
        System.out.println("Introdueix la matrícula del vehicle a eliminar: ");
        String matricula = lector.nextLine();  // Leer la matrícula del vehículo a eliminar

        // Intentar eliminar el vehículo de la base de datos
        try {
          vehicleDaoMySQL.deleteVehicle(matricula);
          System.out.println("Vehicle eliminat correctament.");  // Mensaje de éxito al eliminar
        } catch (SQLException e) {
          System.out.println("Error en eliminar el vehicle: " + e.getMessage());  // Mensaje de error en caso de fallo
        }
      }
    }