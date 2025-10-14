package com.example.imdb_json.Controller;

import com.example.imdb_json.Class.Pelicula;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PeliculasController {

    @FXML
    private DatePicker DatePicker;

    @FXML
    private ListView<Pelicula> ListView;

    @FXML
    private TextField txtDirector;

    @FXML
    private TextField txtGenero;

    @FXML
    private TextField txtTitulo;

    private final ObservableList<Pelicula> peliculas = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        ListView.setItems(peliculas);

        ListView.setOnMouseClicked(event ->{
            Pelicula seleccionada = ListView.getSelectionModel().getSelectedItem();
            mostrarPelicula(seleccionada);
        });
    }

    private void mostrarPelicula(Pelicula p) {
        if (p == null) return; //Evitar error si no hay seleccion

        txtTitulo.setText(p.getTitulo());
        txtGenero.setText(p.getGenero());
        txtDirector.setText(p.getDirector());

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DatePicker.setValue(LocalDate.parse(p.getFecha(), formatter));
        } catch (Exception e) {
            DatePicker.setValue(null);
        }
    }

    @FXML
    void onClickImportar(ActionEvent event) {
        try(InputStream is = getClass().getResourceAsStream("/JSON/peliculas.json")){
            if (is==null){
                mostrarError("Archivo no encontrado", "No se pudo encontrar el archivo .json");
                return;
            }

            //Leer JSON con gson

        }catch (Exception e){
            mostrarError("Error al importar JSON", e.getMessage());
        }
    }

    private void mostrarError(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
