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
		e.ajouterPiece(p);
		e.ajouterPiece(r);
		e.ajouterPiece(d);
		e.ajouterPiece(c);
		System.out.println("Blanc : " + e.getPoints('B'));
		System.out.println("Noir : " + e.getPoints('N'));

		try {
			r.deplacement(new Position(0, 0));
		} catch (ExceptionPosition e1) {
			System.out.println("Coordonnees invalides");
		}

		try {
			p.deplacement(new Position(3, 5));
		} catch (ExceptionPosition e1) {
			System.out.println("Coordonnees invalides");
		}

		e.afficher();
		System.out.println(r);

	}
}
