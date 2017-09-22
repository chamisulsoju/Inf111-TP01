import java.awt.Color;



/**
 * Contient les déclarations de constantes globales pour le projet de jeu
 * "RobotBall".
 * 
 * @author PBelisle
 * @since (copyright) PBelisle - A2017
 * @version FBourdeau - A2017
 */
public class Constantes {

    // Constantes initiales pour les couleurs du jeu (valeurs arbitraires)
    public static final Color    COULEUR_BALLE_VIE    = Color.RED;
    public static final Color    COULEUR_MUR          = Color.BLUE;
    public static final Color    COULEUR_FOND_DEFAUT  = Color.WHITE;
    public static final Color    COULEUR_TEXTE_DEFAUT = Color.BLACK;

    // Nécessaire à l'affichage des options du menu dans GrilleGui.
    public static final String[] OPTIONS              = { "Nouvelle partie", "Recommencer" };

    // Position dans le tableau des options du menu précédent.
    public static final int      NOUVELLE_PARTIE      = 0;
    public static final int      RECOMMENCER          = 1;

    // Dimension des cases du gui (en pixels).
    public static final int      DIMENSION_CASE_Y     = 55;
    public static final int      DIMENSION_CASE_X     = 55;

    // Ajustement de départ du jeu
    public static final int      VITESSE_DEPART       = 300;
    public static final int      NB_BALLES_DEPART     = 5;

    // Le plus de balles que le robot peut contenir.
    public static final int      MAX_BALLES           = 100;
}
