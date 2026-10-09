package com.example.crudjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class CRUDEmpleadosApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CRUDEmpleados-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        primaryStage.setTitle("Aplicación CRUD Empleados JavaFX");
        //stage.centerOnScreen(); // centrar covenant
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("mongus.png")));
        // import javafx.scene.image.Image;
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
