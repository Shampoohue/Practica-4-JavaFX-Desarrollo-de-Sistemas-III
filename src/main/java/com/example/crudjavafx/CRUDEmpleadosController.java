package com.example.crudjavafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CRUDEmpleadosController {

    @FXML private TextField idText;
    @FXML private TextField nombreText;
    @FXML private TextField puestoText;
    @FXML private TextField salarioText;
    @FXML private TableView<Empleado> tablaEmpleados;

    @FXML private TableColumn<Empleado, Integer> columnaId;
    @FXML private TableColumn<Empleado, String> columnaNombre;
    @FXML private TableColumn<Empleado, String> columnaPuesto;
    @FXML private TableColumn<Empleado, Double> columnaSalario;

    private ManejadorArrayListEmpleado manejadorEmpleados;
    private ObservableList<Empleado> listaObservable;

    @FXML
    public void initialize(){
        manejadorEmpleados = new ManejadorArrayListEmpleado();
        listaObservable = FXCollections.observableArrayList(manejadorEmpleados.getEmpleados());

        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaPuesto.setText("Puesto"); // encabezado
        columnaPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto")); // atributo al que mapea
        columnaSalario.setText("Salario");
        columnaSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));

        tablaEmpleados.setItems(listaObservable);

        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->{
            if(newSel != null){
                cargarSeleccionado(newSel);
            }
        });

        recargarDatos();

    }
    @FXML
    private void guardarEmpleado() {
        String nombre = nombreText.getText();
        String puesto = puestoText.getText();
        String salarioStr = salarioText.getText();
        if (nombre.isEmpty()){
            mostrarAlerta("Debe ingresar un nombre", "Error", Alert.AlertType.ERROR);
        } else if (puesto.isEmpty()) {
            mostrarAlerta("Debe ingresar un puesto", "Error", Alert.AlertType.ERROR);
            puestoText.requestFocus();
        }
        double salario;

        try{
            salario = Double.parseDouble(salarioStr);

        }
        catch(NumberFormatException e){
            mostrarAlerta("El salario debe ser un número válido", "Aviso", Alert.AlertType.INFORMATION);
            salarioText.requestFocus();
            return;
        }

        int idGuardar = Integer.parseInt(idText.getText());
        if(idGuardar==manejadorEmpleados.getSiguienteId()){
            manejadorEmpleados.insertar(nombre, puesto, salario);
        }
        else{ // significa que se está actualizando un empleado existente
            manejadorEmpleados.actualizar(idGuardar, nombre, puesto, salario);
        }

        recargarDatos();
    }

    private void cargarSeleccionado( Empleado empleado) {
            idText.setText(String.valueOf(empleado.getId()));
            nombreText.setText(empleado.getNombre());
            puestoText.setText(empleado.getPuesto());
            salarioText.setText(String.valueOf(empleado.getSalario()));
        }


    @FXML
    private void recargarDatos() {
        // TODO implementar metodo
        listaObservable.setAll(manejadorEmpleados.getEmpleados());
        limpiarForm();
    }



    @FXML
    private void limpiarForm() {
        // limpiar el texto de los TextField
        idText.setText(String.valueOf(manejadorEmpleados.getSiguienteId()));
        nombreText.clear();
        puestoText.clear();
        salarioText.clear();

        // deseleccionar el renglón seleccionado en la tabla
        tablaEmpleados.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        // TODO implementar alertas!!1!
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        Stage stage = (Stage) tablaEmpleados.getScene().getWindow();
        if(stage != null){
            alerta.initOwner(stage);
        }
        // alerta.show();
        alerta.showAndWait();
    }

    @FXML private void abrirSubVentana(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);

            //Crear la subventana
            Stage subStage = new Stage();
            subStage.setTitle("Subventana");
            subStage.setScene(scene);

            subStage.initOwner(tablaEmpleados.getScene().getWindow());
            subStage.initModality(Modality.WINDOW_MODAL); // bloquea la ventana principal
            subStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la subventata", "Error", Alert.AlertType.ERROR);
        }
    }

}
