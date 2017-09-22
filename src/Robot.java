/**
 * Cet enregistrement (champs publiques) regroupe la position du robot,
 * sa cha�ne de balles et son nombre d'�l�ments significatifs.
 * 
 * @author PBelisle
 * @since (copyright) PBelisle - A2017
 * @version FBourdeau - A2017
 */
public class Robot {

    // Les balles d'�nergie du robot.
    // La premi�re case est la position du robot, et la position de la
    // premi�re balle.
    // Les autres contiennent la position des autres balles.
    public Coordonnee[] tabBalles = new Coordonnee[Constantes.MAX_BALLES];
    public int          nbBalles;

}
