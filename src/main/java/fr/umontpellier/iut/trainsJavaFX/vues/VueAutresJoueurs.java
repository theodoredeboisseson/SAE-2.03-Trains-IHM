package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {

    private VueDuJeu vueDuJeu;

    public VueAutresJoueurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/autresJoueurs.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
    }

    public void creerBindings(){
        chargerListeJoueurs();
        bindDimensions();
    }

    private void bindDimensions() {
        for(Node n: this.getChildren()){
            VBox v = (VBox) n;
            v.minHeightProperty().bind(this.heightProperty().divide(vueDuJeu.getJeu().getJoueurs().size()));
        }
    }

    private void chargerListeJoueurs() {
        for (IJoueur joueur : vueDuJeu.getJeu().getJoueurs()) {
            VBox joueurBox = new VBox();
            joueurBox.setAlignment(Pos.CENTER);
            Label nomJoueur = new Label(joueur.getNom());
            nomJoueur.getStyleClass().add("main-font");

            String racine = "images/icons/";

            Label scoreJoueur = new Label();
            Label railsJoueur = new Label();
            VueDuJeu.ajouterImageSurLabel(racine + "stars.png", scoreJoueur, 50);
            VueDuJeu.ajouterImageSurLabel(racine + "cube_"+joueur.getCouleur().toString().toLowerCase()+".png", railsJoueur, 50);
            scoreJoueur.textProperty().bind(joueur.scoreProperty().asString("Score: %d"));
            railsJoueur.textProperty().bind(joueur.nbJetonsRailsProperty().asString("Jetons Rails: %d"));

            joueurBox.getChildren().addAll(nomJoueur, scoreJoueur, railsJoueur);
            joueurBox.setStyle("-fx-background-color: " + CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur()) + ";");

            joueurBox.getStyleClass().add("bordures");
            this.getChildren().add(joueurBox);
        }
    }

    public void setJeu(VueDuJeu vueDuJeu) {
        this.vueDuJeu = vueDuJeu;
    }
}
