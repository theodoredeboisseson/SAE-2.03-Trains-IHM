package fr.umontpellier.iut.trainsJavaFX.mecanique.etatsJoueur.defausse;

import fr.umontpellier.iut.trainsJavaFX.mecanique.Joueur;

import java.util.List;

public class DefausseCarteEnMainPuisPioche extends DefausseCarteEnMain {

    public DefausseCarteEnMainPuisPioche(Joueur joueurCourant, List<String> nomsCartesEnMain) {
        super(joueurCourant, nomsCartesEnMain);
        getJeu().instructionProperty().setValue("Défaussez une carte de votre main");
    }

    @Override
    public void suiteAction() {
        joueurCourant.piocherEnMain();
    }

}
