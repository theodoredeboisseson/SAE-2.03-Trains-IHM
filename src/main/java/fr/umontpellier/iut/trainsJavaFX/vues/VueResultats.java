package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class VueResultats extends VBox {

    private VueDuJeu vueDuJeu;

    private Label label;


    public void build(String nomJoueur) {
        label = new Label("BRAVO !!!\n" + nomJoueur + "\nRESSORT VAINQUEUR DE LA PARTIE !");
        label.setStyle("-fx-text-fill: white; -fx-text-alignment: CENTER");
        getChildren().add(label);
        setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: black");
    }

}
