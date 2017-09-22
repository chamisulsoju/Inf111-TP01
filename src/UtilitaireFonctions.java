/**
 * Module de fonctions utilitaires.
 * 
 * @author PBelisle
 * @since (copyright) PBelisle - H2017
 * @version FBourdeau - A2017
 */
public class UtilitaireFonctions {

    /**
     * Retourne un entier choisit al�atoirement entre min et max.
     * Le param�tre min doit �tre plus petit que max.
     * 
     * @param min
     *        La plus petite valeur pouvant �tre g�n�r�e.
     * @param max
     *        La plus grande valeur pouvant �tre g�n�r�e.
     * 
     * @return
     *         Un nombre al�atoire entre min et max.
     */
    public static int nbAleatoire(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

}
