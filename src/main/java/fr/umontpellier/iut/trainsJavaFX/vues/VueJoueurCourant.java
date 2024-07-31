package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class VueJoueurCourant extends HBox {

    private ObjectProperty<IJoueur> joueurCourant;

    private VueDuJeu vueDuJeu;

    @FXML
    private Label nomJoueur;
    @FXML
    private Label argent;
    @FXML
    private Label pointsRails;

    @FXML
    private Label labelMain;
    @FXML
    private HBox main;

    @FXML
    private Label instruction;
    @FXML
    private Button boutonPasser;

    @FXML
    private StackPane pioche;


    public VueJoueurCourant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/joueurCourant.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        joueurCourant = new SimpleObjectProperty<>();
    }

    public void creerBindings() {
        bindDimensions();
        bindIcones();
        instruction.textProperty().bind(vueDuJeu.getJeu().instructionProperty());
        boutonPasser.setOnAction(e -> vueDuJeu.getJeu().passerAEteChoisi());

        joueurCourant.addListener(
                (observable, oldValue, newValue) -> {
                    if (oldValue != null)
                        unbindJoueur(oldValue);
                    bindJoueur(newValue);
                    mettreAJourAffichage(newValue);
                }
        );
    }

    private void bindDimensions() {
        instruction.maxWidthProperty().bind(this.prefWidthProperty().multiply(0.20));
        main.prefHeightProperty().bind(this.prefHeightProperty().multiply(0.6));
        main.prefWidthProperty().bind(this.prefWidthProperty().multiply(0.4));
    }

    private void bindIcones() {
        ImageView imagePasser = new ImageView("images/boutons/passer.png");
        imagePasser.setPreserveRatio(true);
        imagePasser.setFitHeight(50);
        boutonPasser.setGraphic(imagePasser);

        String racine = "images/icons/";
        VueDuJeu.ajouterImageSurLabel(racine + "coins.png", argent, 40);
        VueDuJeu.ajouterImageSurLabel(racine + "rail.png", pointsRails, 40);
        VueDuJeu.ajouterImageSurLabel(racine + "cards.png", labelMain, 50);
    }

    private final ListChangeListener<Carte> mainListener = c -> {
        c.next();
        if (c.wasRemoved())
            main.getChildren().remove(trouverVueCarte(c.getRemoved().get(0)));
    };

    private void bindJoueur(IJoueur joueur) {
        joueur.mainProperty().addListener(mainListener);
        argent.textProperty().bind(joueur.argentProperty().asString());
        pointsRails.textProperty().bind(joueur.pointsRailsProperty().asString());
    }

    private void unbindJoueur(IJoueur joueur) {
        joueur.mainProperty().removeListener(mainListener);
    }

    private void mettreAJourAffichage(IJoueur joueur) {
        chargerMainJoueur(joueur);
        chargerLaPioche(joueur);
        nomJoueur.setText(joueur.getNom());
        this.setStyle("-fx-background-color: " + CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur()) + ";");
    }

    private Node trouverVueCarte(Carte carteATrouver) {
        for (Node n : main.getChildren()) {
            VueCarte v = (VueCarte) n;
            if ((v.getCarte()).equals(carteATrouver))
                return v;
        }
        return null;
    }

    private void chargerMainJoueur(IJoueur joueurActuel) {
        main.getChildren().clear();
        for (Carte c : joueurActuel.mainProperty()) {
            VueCarte v = vueCarteAvecZoom(c);
            v.setOnAction(e -> joueurActuel.uneCarteDeLaMainAEteChoisie(c.getNom()));
            main.getChildren().add(v);
        }
    }

    public void chargerLaPioche(IJoueur joueur){
        Label nbRestant = new Label();
        nbRestant.textProperty().bind(joueur.piocheProperty().sizeProperty().asString());
        nbRestant.getStyleClass().add("main-font");
        nbRestant.setStyle("-fx-font-size: 50; -fx-text-fill: white");

        ImageView image = new ImageView("images/cartes/back.jpg");
        image.fitHeightProperty().bind(vueDuJeu.tailleGlobaleCartePropertyProperty().multiply(1.5));
        image.setPreserveRatio(true);

        pioche.getChildren().addAll(image,nbRestant);
        StackPane.setAlignment(nbRestant, Pos.CENTER);
    }

    public IJoueur getJoueurCourant() {
        return joueurCourant.get();
    }

    public ObjectProperty<IJoueur> joueurCourantProperty() {
        return joueurCourant;
    }

    public void setJeu(VueDuJeu vueDuJeu) {
        this.vueDuJeu = vueDuJeu;
    }

    public VueCarte vueCarteAvecZoom(ICarte carte) {
        VueCarte v = new VueCarte(carte, vueDuJeu.tailleGlobaleCartePropertyProperty());
        v.ajouterZoom(vueDuJeu);
        return v;
    }
}
