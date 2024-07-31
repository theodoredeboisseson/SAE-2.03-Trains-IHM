package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends Button {

    private final ICarte carte;

    public VueCarte(ICarte carte, IntegerProperty hauteur) {
        this.carte = carte;

        String source = ("images/cartes/" + carte.getNom() + ".jpg")
                .toLowerCase()
                .replace(" ", "_")
                .replace("é", "e")
                .replace("ô", "o");
        ImageView image = new ImageView(source);

        image.fitHeightProperty().bind(hauteur);
        image.setPreserveRatio(true);
        this.setPadding(new Insets(0));
        this.setGraphic(image);
        this.setCursor(Cursor.HAND);
    }

    public void ajouterZoom(VueDuJeu vueDuJeu) {
        IntegerProperty I = new SimpleIntegerProperty(vueDuJeu.heightProperty().multiply(.4).intValue());
        setOnMouseEntered(e -> vueDuJeu.getZoomCarte().getChildren().add(new VueCarte(carte,I)));
        setOnMouseExited(e -> vueDuJeu.getZoomCarte().getChildren().clear());
    }

    public ICarte getCarte() {
        return carte;
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

}
