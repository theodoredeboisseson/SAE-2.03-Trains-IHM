package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 * <p>
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {
    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private VBox joueursNoms;
    @FXML
    private Button start;
    @FXML
    private ComboBox<String> plateauComboBox;
    @FXML
    private ImageView plateauView;


    private final ObservableList<String> nomsJoueurs;
    private Plateau plateauChoisi;

    public VueChoixJoueurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/choixJoueur.fxml"));
            loader.setController(this);
            loader.setRoot(new VBox());
            loader.load();
            this.setScene(new Scene(loader.getRoot()));
            this.setTitle("Choix des Joueurs");
        } catch (IOException e) {
            e.printStackTrace();
        }
        nomsJoueurs = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        ChampsNomsJoueurs(2);
        spinner.valueProperty().addListener(
                c -> ChampsNomsJoueurs(spinner.getValue())
        );
        start.setText("Commencer");
        start.setOnAction(e -> lancerPartie());
        plateauComboBox.getItems().addAll("Tokyo", "Osaka");
        plateauComboBox.setValue("Osaka");
        this.sizeToScene();
    }

    private void ChampsNomsJoueurs(int nbJoueurs) {
        joueursNoms.getChildren().clear();

        for (int i = 1; i <= nbJoueurs; i++) {
            TextField textField = new TextField();
            textField.setPromptText("Joueur " + i);
            joueursNoms.getChildren().add(textField);
        }
    }

    @FXML
    private void lancerPartie() {
        if (plateauComboBox.getValue().equals("Tokyo"))
            plateauChoisi = Plateau.TOKYO;
        else if (plateauComboBox.getValue().equals("Osaka"))
            plateauChoisi = Plateau.OSAKA;
        setListeDesNomsDeJoueurs();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> listener) {
        nomsJoueurs.addListener(listener);
    }

    public void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= spinner.getValue(); i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                showAlert("Erreur", "Tous les champs doivent être remplis !");
                break;
            } else {
                tempNamesList.add(name);
            }
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    public String getJoueurParNumero(int playerNumber) {
        if (playerNumber <= joueursNoms.getChildren().size()) {
            TextField textField = (TextField) joueursNoms.getChildren().get(playerNumber - 1);
            return textField.getText();
        }
        return null;
    }

    public Plateau getPlateau() {
        return plateauChoisi;
    }
}
