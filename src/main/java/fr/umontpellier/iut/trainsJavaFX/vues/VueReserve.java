package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;

public class VueReserve extends FlowPane {

    private VueDuJeu vueDuJeu;

    public VueReserve() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/reserve.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void creerBindings(){
        chargerReserve();
    }

    private void chargerReserve() {
        for (Carte c : vueDuJeu.getJeu().getReserve())
            ajouterCarteReserve(c);
    }

    public void ajouterCarteReserve(Carte c) {
        StackPane stackPane = new StackPane();
        Label nbRestant = new Label();
        VueCarte v = new VueCarte(c, vueDuJeu.tailleGlobaleCartePropertyProperty());
        v.ajouterZoom(vueDuJeu);

        nbRestant.setStyle("-fx-background-color: white; -fx-background-radius: 15px 0px 0px 0px");
        stackPane.getChildren().addAll(v, nbRestant);
        StackPane.setAlignment(nbRestant, Pos.BOTTOM_RIGHT);

        nbRestant.textProperty().bind(vueDuJeu.getJeu().getTaillesPilesReserveProperties().get(c.getNom()).asString());

        v.setOnAction(e -> vueDuJeu.getJeu().uneCarteDeLaReserveEstAchetee(c.getNom()));
        this.getChildren().add(stackPane);
    }

    public void setJeu(VueDuJeu vueDuJeu) {
        this.vueDuJeu = vueDuJeu;
    }
}
