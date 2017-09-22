import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.IOException;

import javax.swing.JOptionPane;



/**
 * Le RobotRoad est un jeu invent� mais inspir� d'une application sur un
 * t�l�phone (Snake Vs Blocks).
 * 
 * Il s'agit de d�placer un robot pour qu'il accumule des balles d'�nergie pour
 * les distribuer � des centres de distribution. Le robot doit toujours en
 * avoir sur lui sinon il n'a plus d'�nergie et s'autod�truit.
 * 
 * Il y a un nombre de centre maximum possibles pour une route. Le but est de
 * tous les remplir. Le d�tail du d�roulement du jeu est fourni dans
 * l'�nonc�.
 * 
 * Finalement, il est possible de rejouer une m�me partie ou de jouer une
 * nouvelle partie. Des statistiques sur les pointages et le nombre de balles
 * sont donn�es � la fin de la partie.
 * 
 * Dans le cadre du cours inf111
 * �cole de technologie sup�rieure
 * 
 * @author PBelisle
 * @since (copyright) PBelisle - A2017
 * @version FBourdeau - A2017
 */
public class DemarrerRobotBall {

    /**
     * Strat�gie globale : On utilise principalement le module UtiliraireJeu
     * qui contient les sous-programmes de d�placement et d'affichage de la grlle.
     * 
     * C'est ici qu'on g�re la boucle principale qui se termine si l'utilisateur
     * quitte, s'il r�ussit ou �choue le niveau. Dans cette boucle, on affiche les
     * obstacles et le robot. On obtient si l'utilisateur a press� sur une fl�che.
     * Selon la touche pes�e, on modifie la trajectoire du robot.
     * � chaque d�placement on �value s'il touche un obstacle et on ajuste
     * les balles ou les centres.
     * 
     */
    public static void main(String[] args) throws IOException {

        // Sert pour ne pas donner le message de fin de partie � chaque tour de boucle
        // en attendant que l'utilisateur clique sur un bouton de menu.
        boolean dejaAvise = false;

        // Les stats � maintenir durant le jeu.
        Statistiques stats = new Statistiques();

        // Cr�ation de l'interface graphique qui permet de jouer.
        Dimension dimensionEcran = Toolkit.getDefaultToolkit().getScreenSize();
        Color couleurTexte = Constantes.COULEUR_TEXTE_DEFAUT;
        Color couleurFond = Constantes.COULEUR_FOND_DEFAUT;
        String[] texteMenu = Constantes.OPTIONS;
        GrilleGui gui = new GrilleGui(dimensionEcran, couleurTexte, couleurFond, texteMenu);

        // On cr�e la grille contenant les balles et les obstacles.
        int nbLignes = gui.getNbLignes();
        int nbColonnes = gui.getNbColonnes();
        int[][] grille = new int[nbLignes][nbColonnes];
        UtilitaireJeu.obtenirNouvelleGrille(grille, nbLignes, nbColonnes);

        // Le cr�e le robot au centre de la grille en lui attribuant une certaine
        // �nergie. On s'assure qu'il n'y a pas d'obstacle juste au dessus du robot.
        int ligneCentre = nbLignes / 2;
        int colonneCentre = nbColonnes / 2;
        Robot robot = new Robot();
        init(robot, Constantes.NB_BALLES_DEPART, ligneCentre, colonneCentre);
        grille[ligneCentre - 1][colonneCentre] = UtilitaireJeu.VIDE;

        // Le jeu tourne � l'infinie et se termine si l'utilisateur gagne ou s'il quitte
        // en cliquant sur X.
        while (true) {

            // On affiche les obstacles, le robot et les balles dans le gui.
            UtilitaireJeu.afficherGrille(gui, grille);
            UtilitaireJeu.afficherRobot(gui, robot);

            // On v�rifie si l'utilisateur a cliqu� sur un des boutons du menu
            if (gui.optionMenuEstCliquee()) {
                gererMenu(gui, robot, grille, stats);
                dejaAvise = false;
            }

            // On effectue un tour lorsque le robot poss�de encore de l'�nergie et que le
            // niveaux n'est pas pas compl�ter.
            else if (robot.nbBalles > 0 && !UtilitaireJeu.niveauEstCompleter(grille)) {
                UtilitaireJeu.effectuerTour(gui, grille, robot, stats);
            }

            // S'il n'y a plus de balles, le robot meurt et la partie est termin�e.
            else {

                // On s'assure de montre le dialogue de fin de partie qu'une seule fois..
                if (!dejaAvise) {
                    dejaAvise = true;
                    afficherStats(stats);
                }
            }
        }
    }


    /**
     * Affiche les statistiques du jeu.
     * 
     * @param stats 
     *  		L'enregistrement qui contient les statistiques.
     *
	 * @return
	 *          Aucun
	 */
    private static void afficherStats(Statistiques stats) {
		/*
		 * Strat�gie ; On utilise javax.swing.JOptionPane au lieu d'afficher dans la GUI.    
		 */
        JOptionPane.showMessageDialog(null, "Partie termin�e");

        String msg = "Pointage : " + String.valueOf(stats.pointage) + "\n";
        
        msg += "Nombre de balles maximum obtenu : " + String.valueOf(stats.maxBalles) + "\n";
        
        msg += "Record :" + String.valueOf(stats.hautPointage) + "\n";
        JOptionPane.showMessageDialog(null, msg);

	}


    /**
     * Cette proc�dure g�re les actions pour le clique d'un bouton de menu.
     * 
     * @param gui
     *        L'interface graphique dans lequel les cliques ont lieux.
     * @param robot
     *        Les caract�ristiques du robot du jeu
     * @param grille
     *        La repr�sentation des objets contenu dans la grille de jeu
     * @param stats
     *        Les statistiques de la parties
     * 
     * @return
     *         Aucun
     */
    private static void gererMenu(GrilleGui gui, Robot robot, int[][] grille, Statistiques stats) {


        // On identifie le bouton sur lequel l'utilisateur � cliqu�.
        String reponse = gui.getOptionMenuClique();

        // On obtient/calcul les caract�ristiques de la grille.
        int nbLignes = gui.getNbLignes();
        int nbColonnes = gui.getNbColonnes();
        int ligneCentre = nbLignes / 2;
        int colonneCentre = nbColonnes / 2;

        // On g�re la requ�te d'une nouvelle partie
        if (reponse.equals(Constantes.OPTIONS[Constantes.NOUVELLE_PARTIE])) {


            UtilitaireJeu.viderGrille(grille);
            UtilitaireJeu.obtenirNouvelleGrille(grille, nbLignes, nbColonnes);

            init(robot, Constantes.NB_BALLES_DEPART, ligneCentre, colonneCentre);

            // On ne touche pas aux autres champs pour retenir leur valeur.
            stats.pointage = 0;


        }

        // On g�re la requ�te o� l'utilisateur veut recommencer la m�me partie
        else {

            // On red�mare la m�me partie en �crasant la grille de jeux avec la grille
            // d'origine.
            UtilitaireJeu.restaurerGrilleDepart(grille);

            // On replace le robot au centre de la grille.
            init(robot, Constantes.NB_BALLES_DEPART, ligneCentre, colonneCentre);

            // On efface les donn�es concernant la partie en cours..
            stats.pointage = 0;
            stats.maxBalles = 0;
        }
    }


    /**
     * Cette proc�dure initialise les balles d'�nergies conserv�es par le robot.
     * 
     * @param robot
     *        Le robot � initialis�.
     * @param nbBallesDepart
     *        Le nombre de balle d'�nergie avec lesquels le robot d�bute le jeu.
     * @param ligne
     *        La ligne o� le robot est plac� dans la grille.
     * @param colonne
     *        La colonne o� le robot est plac� dans la grille.
     * 
     * @return
     *         Aucun
     */
    public static void init(Robot robot, int nbBallesDepart, int ligne, int colonne) {

        // On conserve le nombre de balles train�es par le robot.
        robot.nbBalles = nbBallesDepart;

        // On conserve la position de chaque balle train�e par le robot.
        for (int i = 0; i < nbBallesDepart; i++, ligne++) {
            robot.tabBalles[i] = new Coordonnee();
            robot.tabBalles[i].ligne = ligne;
            robot.tabBalles[i].colonne = colonne;
        }
    }

}
