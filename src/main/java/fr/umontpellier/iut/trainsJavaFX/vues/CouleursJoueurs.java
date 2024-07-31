package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.CouleurJoueur;

import java.util.Map;

public class CouleursJoueurs {
    public static Map<CouleurJoueur, String> couleursBackgroundJoueur = Map.of(
            CouleurJoueur.JAUNE, "#FFB31A",
            CouleurJoueur.ROUGE, "#AF3149",
            CouleurJoueur.BLEU, "#646B99",
            CouleurJoueur.VERT, "#55B78B"
    );

}