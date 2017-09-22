import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;



/**
 * Grille de jeu rectangulaire d'au maximum MAX_LIGNES par
 * MAX_COLONNES qui permet d'obtenir s'il y eu un clic, la position
 * du clic et modifier le contenu de la case (couleur et texte).
 * 
 * Il est possible aussi d'ajouter des boutons de menu. Dans ce cas,
 * estBoutonMenu retourne vrai et getTexteMenu retourne le texte contenu
 * dans le bouton. Ces boutons sont créés en bas de la fenétre é partir d'un
 * tableau de String fourni au constructeur (mettre null si non désiré).
 * 
 * Utile pour des TP1 en inf111 (jeux tels Sudoku, Binero, 421, ...)
 * 
 * @author PBelisle
 * @since (copyright) PBelisle - A2017
 * @version FBourdeau - A2017
 */
public class GrilleGui implements Runnable {

    /*
     * STRATÉGIE : On met des boutons dans un panneau mais on les retient
     * aussi dans une grille. Une classe interne MonJButton hérite de JButton
     * à laquelle on ajoute des attributs pour retenir la position du bouton
     * dans la grille. Tout cela pour éviter la recherche du bouton lors d'un
     * clic.
     * 
     * Un booléen permet de retenir si un bouton a été cliqué et il
     * est remis à faux après une lecture de la position par son
     * accesseur.
     */

    // Deux modes de fermeture du gui. On quitte le programme ou on
    // dispose juste la fenêtre.
    public static final int  QUITTE        = JFrame.EXIT_ON_CLOSE;
    public static final int  DISPOSE       = JFrame.DISPOSE_ON_CLOSE;

    // Code des touches du clavier.
    public static final int  TOUCHE_GAUCHE = 37;
    public static final int  TOUCHE_DROITE = 39;
    private static final int TOUCHE_HAUT   = 41;

    // On compose dans un cadre.
    private JFrame           cadre         = new JFrame();

    // La grille qui est affichée (classe interne décrite à la fin).
    private MonJButton[][]   grille;

    // Retenir la taille de la grille.
    private int              nbLignes;
    private int              nbColonnes;

    // Les couleurs.
    private Color            couleurTexte;
    private Color            couleurFond;

    // La taille de l'écran.
    private Dimension        dimensionEcan = Toolkit.getDefaultToolkit().getScreenSize();

    // Retenir le tableau des options de menu.
    private String[]         tabMenus;

    // Pour les options de meus du panneau du bas.
    private boolean          estBoutonMenu;

    // Vaudra le texte du bouton cliqué s'il y a eu un clic sur un des boutons
    // de menu et il est mis à null après getOptionMenu.
    private String           optionClique;

    // Les panneaux d'affichage.
    private JPanel           panneauHaut;
    private JPanel           panneauPrincipal;

    // Garde la dernière touche appuyée. En haut au départ.
    private int              touche        = TOUCHE_HAUT;


    /**
     * /**
     * Crée une grille selon les dimensions reçues.
     * 
     * @param dimensionGrille
     *        le nombre de ligne (axe des Y) et de colonne (axe des X) dans
     *        la grille.
     * @param couleurTexte
     *        La couleur du texte dans chaque case.
     * @param couleurFond
     *        La couleur de chaque case.
     * @param options
     *        Les options du menu du bas
     */
    public GrilleGui(Dimension dimensionGrille, Color couleurTexte, Color couleurFond, String[] options) {

        // On retient la taille et les couleurs de la grille.
        this.nbLignes = (int) (dimensionGrille.height / Constantes.DIMENSION_CASE_Y);
        this.nbColonnes = (int) (dimensionGrille.width / Constantes.DIMENSION_CASE_X);

        // On conserve les caractéristiques des cases.
        this.couleurFond = couleurFond;
        this.couleurTexte = couleurTexte;

        // On retient les options du menu.
        this.tabMenus = options;

        // On crée le tableau 2D (vide).
        grille = new MonJButton[nbLignes][nbColonnes];

        // On retient qu'aucun clique n'a eu lieu.
        estBoutonMenu = false;

        // On affiche le cadre dans un thread.
        Thread t = new Thread(this);
        t.start();
    }


    /**
     * Retourne vrai si un des boutons de menu a été cliqué.
     * 
     * @param
     *        Aucun
     * 
     * @return Si un des boutons de menu a été cliqué.
     */
    public boolean optionMenuEstCliquee() {
        return estBoutonMenu;
    }


    /**
     * Retourne la chaine de caractère identifiant le dernier bouton sur
     * lequel l'utilisateur à cliquer. Null autrement.
     * 
     * @param
     *        Aucun
     * 
     * @return
     *         Le texte dans le bouton cliqué s'il y a lieu
     */
    public String getOptionMenuClique() {

        // On réinitialise le flag identifiant un clic dans le menu.
        if (estBoutonMenu) {
            estBoutonMenu = false;
        }

        // Il est important de remettre l'optionClique à null si
        // le clique n'est pas survenu sur un des options du menu.
        else {
            optionClique = null;
        }

        // On remet le focus sur le gui.
        panneauPrincipal.setFocusable(true);
        panneauPrincipal.requestFocusInWindow();

        return optionClique;
    }


    /**
     * Retourne la valeur contenue dans une case de la grille.
     * 
     * @param y
     *        Le numéro de ligne da la case ciblée.
     * @param x
     *        Le numéro de colonne da la case ciblée.
     * 
     * @return
     *         Le contenu de la case en [y][x]
     */
    public String getValeur(int y, int x) {
        return grille[y][x].getText();
    }


    /**
     * Permet de modifier la valeur d'une case de la grille.
     * 
     * @param y
     *        Le numéro de ligne da la case ciblée.
     * @param x
     *        Le numéro de colonne da la case ciblée.
     * @param valeur
     *        La valeur à insérer dans la case
     *
     * @return
     *         Aucun
     */
    public void setTexte(int y, int x, String valeur) {

        /*
         * Puisque la grille est dans un Thread, on s'assure que celle-ci
         * est créée avant d'insérer la valeur.
         */
        if (y < nbLignes && grille[y][x] != null) {
            grille[y][x].setText(valeur);
        }

    }


    /**
     * Accesseur du nombre de lignes.
     * 
     * @param
     *        Aucun
     * 
     * @return
     *         Le nombre de lignes de la grille.
     */
    public int getNbLignes() {
        return nbLignes;
    }


    /**
     * Accesseur du nombre de colonnes.
     * 
     * @param
     *        Aucun
     * 
     * @return
     *         Le nombre de colonnes de la grille.
     */
    public int getNbColonnes() {
        return nbColonnes;
    }


    /**
     * Permet de changer la couleur de fond d'une case.
     * 
     * @param y
     *        Le numéro de ligne da la case ciblée.
     * @param x
     *        Le numéro de colonne da la case ciblée.
     * @param couleur
     *        La nouvelle couleur
     * 
     * @return
     *         Aucun
     */
    public void setCouleurFond(int y, int x, Color couleurFond) {

        /*
         * Puisque la grille est dans un Thread, on s'assure que celle-ci
         * est créée avant d'y accéder.
         */
        if (y < nbLignes && grille[y][x] != null) {
            grille[y][x].setBackground(couleurFond);
        }
    }


    /**
     * Permet de changer la couleur de texte d'une case.
     * 
     * @param y
     *        Le numéro de ligne da la case ciblée.
     * @param x
     *        Le numéro de colonne da la case ciblée.
     * @param couleur
     *        La nouvelle couleur
     * 
     * @return
     *         Aucun
     */
    public void setCouleurTexte(int y, int x, Color couleurTexte) {

        /*
         * Puisque la grille est dans un Thread, on s'assure que celle-ci
         * est créée avant d'y accéder.
         */
        if (y < nbLignes && grille[y][x] != null) {
            grille[y][x].setForeground(couleurTexte);
        }
    }


    /**
     * Retourne la dernière touche qui a été appuyée entre
     * TOUCHE_GAUCHE, TOUCHE_HAUT ou TOUCHE_DROITE.
     * 
     * @param
     *        Aucun
     * 
     * @return
     *         La dernière touche employé dans la grille.
     */
    public int getDerniereTouche() {

        /* On réinitilise l'attribut entre chaque appel. */
        int tmp = touche;
        touche = TOUCHE_HAUT;
        return tmp;
    }


    /**
     * Effectue une pause en mode multi-taches.
     * 
     * @param temps
     *        Le temps de la pause en millisecondes.
     * 
     * @return
     *         Aucun
     */
    public void pause(int temps) {
        try {
            Thread.sleep(temps);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Création du GUI et implémentation des listeners nécessaire à
     * l'intéraction de l'utilisateur
     * 
     * @param
     *        Aucun
     * 
     * @return
     *         Aucun
     */
    @Override
    public void run() {

        // On initialise le GUI en mode plein écran et on établit le
        // mode de fermeture.
        cadre.setExtendedState(JFrame.MAXIMIZED_BOTH);
        cadre.setDefaultCloseOperation(QUITTE);

        // Obtention de la référence sur le contentPane pour éviter
        // des appels superflus.
        panneauPrincipal = (JPanel) cadre.getContentPane();

        // On crée le panneau contenant la grille.
        panneauHaut = new JPanel();

        // On crée les boutons de menu s'il y en a (FlowLayout par défaut).
        if (tabMenus != null) {
            JPanel panneauBas = new JPanel();

            // On calcul les dimensions des deux panneaux
            Dimension dimensionHaut = new Dimension(dimensionEcan.width, (int) (dimensionEcan.height * .8));
            Dimension dimensionBas = new Dimension(dimensionEcan.width, (int) (dimensionEcan.height * .1));

            // On fixe les dimensions des deux panneaux pour l'allure de la fenêtre.
            panneauHaut.setMinimumSize(dimensionHaut);
            panneauHaut.setMaximumSize(dimensionHaut);
            panneauHaut.setPreferredSize(dimensionHaut);
            panneauBas.setMinimumSize(dimensionBas);
            panneauBas.setMaximumSize(dimensionBas);
            panneauBas.setPreferredSize(dimensionBas);

            // On fixe le layout de la grille.
            panneauHaut.setLayout(new GridLayout(nbLignes, nbColonnes));

            // On crée le menu dans le panneau du bas
            ajouterMenu(panneauBas);

            // On insère les panneaux dans le GUI
            panneauPrincipal.add(panneauHaut, BorderLayout.PAGE_START);
            panneauPrincipal.add(panneauBas, BorderLayout.PAGE_END);
        }

        // Le panneau du centre est plein écran s'il n'y a pas de menu.
        else {
            panneauPrincipal.add(panneauHaut);
        }

        // On crée toutes les cases de la grille
        ajouterBoutons(panneauHaut);

        // Permet d'intercepter les actions sur les touches du clavier.
        panneauPrincipal.setFocusable(true);
        panneauPrincipal.requestFocusInWindow();
        panneauPrincipal.addKeyListener(new EcouteurTouche());

        cadre.setVisible(true);
    }


    /**
     * Ajoute les boutons de menu au panneau.
     * 
     * @param panneau
     *        Le panneau ou les boutons du menus sont créer.
     * 
     * @return
     *         Aucun
     */
    private void ajouterMenu(JPanel panneau) {

        JButton boutonCourant;      // Le bouton à ajouter

        // On crée tous les boutons décris lors de l'instanciation de la grille.
        for (int i = 0; i < tabMenus.length; i++) {
            boutonCourant = new JButton(tabMenus[i]);

            // On ajoute un listner pour détecter les clics dans le menu (au lieu de la
            // grille).
            boutonCourant.addActionListener(new listnerBoutonMenu());

            // On ajoute le bouton
            panneau.add(boutonCourant);
        }

    }


    /**
     * Crée et ajoute les boutons (chaque case) dans la grille et dans le panneau.
     * 
     * @param panneau
     *        Le panneau qui contiendra la grille de case
     * 
     * @return
     *         Aucun.
     */
    private void ajouterBoutons(JPanel panneau) {
        // On crée la grille 2D de case.
        for (int i = 0; i < nbLignes; i++)
            for (int j = 0; j < nbColonnes; j++) {

                // On crée chaque chaque et on l'ajoute à la grille
                grille[i][j] = new MonJButton(i, j, " ", couleurTexte, couleurFond);
                panneau.add(grille[i][j]);
            }
    }


    /**
     * Classe interne qui permet d'identifier un clic sur un des boutons du menu.
     * 
     * @author FBourdeau
     * @since (copyright) FBourdeau - A2017
     * @version FBourdeau - A2017
     */
    private class listnerBoutonMenu implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            optionClique = ((JButton) e.getSource()).getText();
            estBoutonMenu = true;
        }
    }


    /**
     * Classe interne qui ajoute à un JButton la position (x,y) ou il se trouve
     * dans la grille.
     * 
     * Cela évite de chercher cette position lors d'un clic.
     * 
     * @author PBelisle
     * @since (copyright) PBelisle
     * @version FBourdeau - A2017
     */
    private class MonJButton extends JButton {

        private static final long serialVersionUID = 1L;

        // La taille du texte dans le bouton
        private static final int  TAILLE_TEXTE     = 15;


        /**
         * Constructeur du bouton afin de spécifier sa position du bouton et sa valeur.
         * 
         * @param y
         *        Le numéro de ligne du bouton.
         * @param x
         *        Le numéro de colonne du bouton.
         * @param valeur
         *        La valeur à afficher dans le bouton
         * 
         * @return
         *         Une référence vers le nouveau bouton
         */
        private MonJButton(int ligne, int colonne, String valeur, Color couleurTexte, Color couleurFond) {

            // On crée le JButton.
            super(valeur);

            // Pour utilisation avec un mac. sinon les couleurs ne s'affichent
            // pas.
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // On fixe les caractéristiques de fond du bouton.
            setBackground(couleurFond);

            // On fixe les caractéristiques du texte du bouton
            setForeground(couleurTexte);
            setFont(new Font(null, Font.BOLD, TAILLE_TEXTE));
            setEnabled(false);
        }
    }


    /**
     * Classe interne qui permet de gérer le déplacement des objets dans la grille
     * en retenant les touches appuyées par l'utilisateur.
     * 
     * @author PBelisle
     * @since (copyright) PBelisle
     * @version FBourdeau - A2017
     */
    private class EcouteurTouche implements KeyListener {

        public void keyTyped(KeyEvent e) {
        }


        @Override
        public void keyPressed(KeyEvent e) {
            touche = e.getExtendedKeyCode();
        }


        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
