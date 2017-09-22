/**
 * Cet enregistrement (champs publiques) regroupe la position du robot,
 * sa chaîne de balles et son nombre d'éléments significatifs.
 * 
 * @author PBelisle
 * @since (copyright) PBelisle - A2017
 * @version FBourdeau - A2017
 */
public class Robot {

    // Les balles d'énergie du robot.
    // La première case est la position du robot, et la position de la
    // première balle.
    // Les autres contiennent la position des autres balles.
    public Coordonnee[] tabBalles = new Coordonnee[Constantes.MAX_BALLES];
    public int          nbBalles;

}
