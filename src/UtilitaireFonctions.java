/**
 * Module de fonctions utilitaires.
 * 
 * @author PBelisle
 * @since (copyright) PBelisle - H2017
 * @version FBourdeau - A2017
 */
public class UtilitaireFonctions {

    /**
     * Retourne un entier choisit aléatoirement entre min et max.
     * Le paramètre min doit être plus petit que max.
     * 
     * @param min
     *        La plus petite valeur pouvant être générée.
     * @param max
     *        La plus grande valeur pouvant être générée.
     * 
     * @return
     *         Un nombre aléatoire entre min et max.
     */
    public static int nbAleatoire(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

}
