package com.example.imdb_json.Controller;

import com.example.imdb_json.Class.Pelicula;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.List;

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

    private static final ObjectMapper json_mapper = new ObjectMapper();

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

            ObjectMapper mapper = new ObjectMapper();

            // Leemos el JSON como un Map genérico
            java.util.Map<?, ?> root = mapper.readValue(is, java.util.Map.class);

            // Obtenemos el array que está dentro de "peliculas"
            java.util.List<?> listaPeliculas = (java.util.List<?>) root.get("peliculas");

            // Convertimos la lista genérica a una lista de Pelicula
            java.util.List<Pelicula> peliculasLeidas = new java.util.ArrayList<>();
            for (Object obj : listaPeliculas) {
                // Cada elemento es un Map<String, Object>, lo convertimos directamente
                Pelicula p = mapper.convertValue(obj, Pelicula.class);
                peliculasLeidas.add(p);
            }

            // Cargamos las películas en la ListView
            peliculas.setAll(peliculasLeidas);

            if (!peliculas.isEmpty()) {
                ListView.getSelectionModel().select(0);
                mostrarPelicula(peliculas.get(0));
            }


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
