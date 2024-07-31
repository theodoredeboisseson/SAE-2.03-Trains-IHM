package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class VueCartesNonJouables extends VBox {

    private ObjectProperty<IJoueur> joueurCourant;

    private VueDuJeu vueDuJeu;

    @FXML
    private Label labelCartesRecues;
    @FXML
    private StackPane cartesRecues;

    @FXML
    private Label labelCartesDefausse;
    @FXML
    private StackPane cartesDefausse;

    @FXML
    private Label labelCartesEnJeu;
    @FXML
    private StackPane cartesEnJeu;

    public VueCartesNonJouables() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/cartesNonJouables.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        joueurCourant = new SimpleObjectProperty<>();
    }

    public void creerBindings() {
        bindIcones();
        joueurCourant.addListener(
                (observable, oldValue, newValue) -> {
                    if (oldValue != null)
                        unbindJoueur(oldValue);
                    bindJoueur(newValue);
                    mettreAJourAffichage(newValue);
                }
        );
        bindDimensions();
    }

    public void bindDimensions() {
        cartesRecues.prefHeightProperty().bind(this.heightProperty().multiply(0.30));
        cartesDefausse.prefHeightProperty().bind(this.heightProperty().multiply(0.30));
        cartesEnJeu.prefHeightProperty().bind(this.heightProperty().multiply(0.30));
    }

    private void bindIcones() {
        String racine = "images/icons/";
        VueDuJeu.ajouterImageSurLabel(racine + "cartesRecues.png",labelCartesRecues,50);
        VueDuJeu.ajouterImageSurLabel("images/boutons/defausse.png",labelCartesDefausse,50);
        VueDuJeu.ajouterImageSurLabel(racine + "cartesEnJeu.png",labelCartesEnJeu,50);
    }

    private final ListChangeListener<Carte> cartesRecuesListener = c -> {
        c.next();
        if (c.wasAdded())
            for (Carte carte : c.getAddedSubList())
                ajouterCarteRecue(carte);
    };

    private final ListChangeListener<Carte> cartesEnJeuListener = c -> {
        c.next();
        if (c.wasAdded())
            for (Carte carte : c.getAddedSubList())
                ajouterCarteEnJeu(carte);
        if (c.wasRemoved())
            for (Carte carte : c.getRemoved())
                retirerCarteEnJeu(carte);
    };

    private void bindJoueur(IJoueur joueur) {
        joueur.cartesRecuesProperty().addListener(cartesRecuesListener);
        joueur.cartesEnJeuProperty().addListener(cartesEnJeuListener);
        cartesDefausse.addEventHandler(
                MouseEvent.MOUSE_CLICKED,
                e -> joueur.laDefausseAEteChoisie()
        );
    }

    private void unbindJoueur(IJoueur joueur) {
        joueur.cartesRecuesProperty().removeListener(cartesRecuesListener);
        joueur.cartesEnJeuProperty().removeListener(cartesEnJeuListener);
    }

    private void mettreAJourAffichage(IJoueur joueur) {
        cartesRecues.getChildren().clear();
        chargerCartesDefausse(joueur);
        chargerCartesEnJeu(joueur);
    }

    private void chargerCartesDefausse(IJoueur joueur) {
        chargerCartesGenerique(cartesDefausse, joueur.defausseProperty());
    }

    private void chargerCartesEnJeu(IJoueur joueur) {
        chargerCartesGenerique(cartesEnJeu, joueur.cartesEnJeuProperty());
    }

    public void chargerCartesGenerique(StackPane conteneur, ListeDeCartes cartes) {
        conteneur.getChildren().clear();
        for (Carte c : cartes)
            ajouterCarteGenerique(c,conteneur);
    }

    private void ajouterCarteRecue(Carte c) {
        ajouterCarteGenerique(c, cartesRecues);
    }

    private void ajouterCarteEnJeu(Carte c) {
        VueCarte v = ajouterCarteGenerique(c, cartesEnJeu);
        v.setOnAction(e -> joueurCourant.getValue().uneCarteEnJeuAEteChoisie(c.getNom()));
    }

    private VueCarte ajouterCarteGenerique(Carte c, StackPane conteneur) {
        VueCarte v = vueCarteAvecZoom(c);
        v.setTranslateX(vueDuJeu.tailleGlobaleCartePropertyProperty().multiply(.1).get() * conteneur.getChildren().size());
        StackPane.setAlignment(v, Pos.CENTER_LEFT);
        conteneur.getChildren().add(v);
        return v;
    }

    private void retirerCarteEnJeu(Carte carte) {
        for (Node n : cartesEnJeu.getChildren()) {
            VueCarte v = (VueCarte) n;
            if (v.getCarte().equals(carte)) {
                cartesEnJeu.getChildren().remove(n);
                break;
            }
        }
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
