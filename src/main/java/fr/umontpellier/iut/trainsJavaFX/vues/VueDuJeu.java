package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 * <p>
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends StackPane {

    private final IJeu jeu;

    @FXML
    private HBox upperLeftSection;
    @FXML
    private VueCartesNonJouables cartesNonJouables;
    @FXML
    private VuePlateau plateau;

    @FXML
    private VueJoueurCourant joueurCourant;

    @FXML
    private VBox reserveEtAutresJoueurs;
    @FXML
    private VueReserve reserve;
    @FXML
    private VueAutresJoueurs autresJoueurs;

    @FXML
    private HBox zoomCarte;

    @FXML
    private VueResultats resultats;

    private final IntegerProperty tailleGlobaleCarteProperty;
    private final IntegerProperty tailleGlobalePolice;

    public VueDuJeu(IJeu jeu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/jeu.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.jeu = jeu;
        tailleGlobaleCarteProperty = new SimpleIntegerProperty();
        tailleGlobalePolice = new SimpleIntegerProperty();
        this.setBackground(new Background(new BackgroundImage(new Image("css/card_background.jpg"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(640, 360, false, false, false, false))));
    }

    public void creerBindings() {
        bindDimensions();
        initialiser();
        getJeu().finDePartieProperty().addListener(e -> {
            if (getJeu().finDePartieProperty().get())
                resultats.build(getJoueurGagnant().getNom().toUpperCase());
        });
    }

    public void bindDimensions(){
        plateau.prefWidthProperty().bind(widthProperty().multiply(.58));
        plateau.prefHeightProperty().bind(heightProperty().multiply(.80)); //

        joueurCourant.prefWidthProperty().bind(widthProperty().multiply(.79));
        joueurCourant.prefHeightProperty().bind(heightProperty().multiply(.20)); //
        cartesNonJouables.prefWidthProperty().bind(widthProperty().multiply(.21));
        cartesNonJouables.prefHeightProperty().bind(heightProperty().multiply(.80)); //
        reserveEtAutresJoueurs.prefWidthProperty().bind(widthProperty().multiply(.21));
        reserveEtAutresJoueurs.prefHeightProperty().bind(heightProperty());
        reserve.prefHeightProperty().bind(heightProperty().multiply(0.5));
        autresJoueurs.prefHeightProperty().bind(heightProperty().multiply(0.5));

        tailleGlobaleCarteProperty.bind(heightProperty().multiply(0.12));
        tailleGlobalePolice.bind(heightProperty().multiply(0.12));
    }

    private void initialiser(){
        cartesNonJouables.setJeu(this);
        cartesNonJouables.creerBindings();
        cartesNonJouables.joueurCourantProperty().bind(jeu.joueurCourantProperty());

        plateau.creerBindings();

        joueurCourant.setJeu(this);
        joueurCourant.creerBindings();
        joueurCourant.joueurCourantProperty().bind(jeu.joueurCourantProperty());

        reserve.setJeu(this);
        reserve.creerBindings();

        autresJoueurs.setJeu(this);
        autresJoueurs.creerBindings();
    }

    public static void ajouterImageSurLabel(String sourceImage, Label label, int hauteur){
        ImageView image = new ImageView(sourceImage);
        image.setFitHeight(hauteur);
        image.setPreserveRatio(true);
        label.setGraphic(image);
    }

    public IJeu getJeu() {
        return jeu;
    }



    public HBox getZoomCarte() {
        return zoomCarte;
    }

    public IntegerProperty tailleGlobaleCartePropertyProperty() {
        return tailleGlobaleCarteProperty;
    }

    public IJoueur getJoueurGagnant(){
        IJoueur joueur = getJeu().getJoueurs().get(0);
        for (IJoueur j : getJeu().getJoueurs())
            if (j.getScoreTotal()> joueur.getScoreTotal())
                joueur = j;
        return joueur;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));
}
