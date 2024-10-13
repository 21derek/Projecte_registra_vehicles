package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ParkingVehicleGUI extends JFrame {
  private static final VehicleDaoMySQL vehicleDaoMySQL = new VehicleDaoMySQL();

  public ParkingVehicleGUI() {
    setTitle("Gestió de Vehicles");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Crear panel
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1)); // Layout vertical

    // Botones
    JButton btnBuscar = new JButton("Buscar Vehicle");
    JButton btnActualizar = new JButton("Actualizar Vehicle");
    JButton btnEliminar = new JButton("Eliminar Vehicle");

    // Añadir acción a los botones
    btnBuscar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        buscarVehicle();
      }
    });

    btnActualizar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        actualizarVehicle();
      }
    });

    btnEliminar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        eliminarVehicle();
      }
    });

    // Agregar botones al panel
    panel.add(btnBuscar);
    panel.add(btnActualizar);
    panel.add(btnEliminar);

    // Agregar panel a la ventana
    add(panel);
  }

  private void buscarVehicle() {
    String matricula = JOptionPane.showInputDialog(this, "Introdueix la matrícula del vehicle a cercar:");
    if (matricula != null && !matricula.trim().isEmpty()) {
      try {
        Vehicle vehicle = vehicleDaoMySQL.getVehicle(matricula.trim());
        if (vehicle != null) {
          JOptionPane.showMessageDialog(this, "Vehicle trobat: " + vehicle);
        } else {
          JOptionPane.showMessageDialog(this, "Vehicle no trobat.");
        }
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error en cercar el vehicle: " + e.getMessage());
      }
    }
  }

  private void actualizarVehicle() {
    String matricula = JOptionPane.showInputDialog(this, "Introdueix la matrícula del vehicle a actualitzar:");
    if (matricula != null && !matricula.trim().isEmpty()) {
      try {
        Vehicle vehicle = vehicleDaoMySQL.getVehicle(matricula.trim());
        if (vehicle != null) {
          // Proceso de actualización
          String nuevaMatricula = JOptionPane.showInputDialog(this, "Nova matrícula (deixa buit per mantenir):");
          if (nuevaMatricula != null && !nuevaMatricula.trim().isEmpty()) {
            vehicle.setLicensePlate(nuevaMatricula);
          }

          String nouTimesUsedInput = JOptionPane.showInputDialog(this, "Nou nombre de vegades que s'ha utilitzat (deixa buit per mantenir):");
          if (nouTimesUsedInput != null && !nouTimesUsedInput.trim().isEmpty()) {
            int nouTimesUsed = Integer.parseInt(nouTimesUsedInput);
            vehicle.setTimesUsed(nouTimesUsed);
          }

          String nouEsCompeticioInput = JOptionPane.showInputDialog(this, "És de competició? (true/false, deixa buit per mantenir):");
          if (nouEsCompeticioInput != null && !nouEsCompeticioInput.trim().isEmpty()) {
            boolean nouEsCompeticio = Boolean.parseBoolean(nouEsCompeticioInput);
            vehicle.setCompetetion(nouEsCompeticio);
          }

          // Actualizar el vehículo en la base de datos
          vehicleDaoMySQL.updateVehicle(vehicle);
          JOptionPane.showMessageDialog(this, "Vehicle actualitzat correctament.");
        } else {
          JOptionPane.showMessageDialog(this, "Vehicle no trobat.");
        }
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error en actualitzar el vehicle: " + e.getMessage());
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error en format de nombre: " + e.getMessage());
      }
    }
  }

  private void eliminarVehicle() {
    String matricula = JOptionPane.showInputDialog(this, "Introdueix la matrícula del vehicle a eliminar:");
    if (matricula != null && !matricula.trim().isEmpty()) {
      try {
        vehicleDaoMySQL.deleteVehicle(matricula.trim());
        JOptionPane.showMessageDialog(this, "Vehicle eliminat correctament.");
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error en eliminar el vehicle: " + e.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ParkingVehicleGUI gui = new ParkingVehicleGUI();
      gui.setVisible(true);
    });
  }
}

