package tpjava;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Echiquier e = new Echiquier();
		Roi r = new Roi('N', new Position(0, 4));
		Pion p = new Pion('B', new Position(4, 5));
		Dame d = new Dame('B', new Position(3, 3));
		Cavalier c = new Cavalier('B', new Position(2, 2));
		e.ajouterPiece(c);
		e.ajouterPiece(r);
		e.ajouterPiece(p);
		e.ajouterPiece(d);

		e.afficher();
		try {
			c.deplacement(new Position(0, 1));
		} catch (ExceptionPosition e1) {
			System.out.println("Coordonnees invalides");
		}
		e.afficher();
	}
}
