package tpjava;

import java.util.Scanner;

public class Partie {
	private Echiquier echiquier;
	char[] joueurs;
	int compteurTours;
	Piece piece;
	Scanner scanner;

	/*
	 * Constructeur qui instancie une partie d'echecs avec un joueur 'N' et 'B',
	 * ouvre un Scanner, instancie une echiquier avec les bonnes pieces pour les
	 * deux joueurs.
	 */
	public Partie() {

		scanner = new Scanner(System.in);
		echiquier = new Echiquier();
		joueurs = new char[2];
		joueurs[0] = 'N';
		joueurs[1] = 'B';
		compteurTours = 0;

		// ajout des pieces a l'echiquier
		for (int i = 0; i < joueurs.length; i++) {

			// Pions
			for (int j = 0; j < 8; j++) {
				Pion p = new Pion(joueurs[i], new Position(1 + i * 5, j));
				echiquier.ajouterPiece(p);
			}
			// Tours
			for (int j = 0; j < 2; j++) {
				Tour t = new Tour(joueurs[i], new Position(i * 7, j * 7));
				echiquier.ajouterPiece(t);
			}
			// Cavaliers
			for (int j = 0; j < 2; j++) {
				Cavalier c = new Cavalier(joueurs[i], new Position(i * 7, 1 + j * 5));
				echiquier.ajouterPiece(c);
			}
			// Fous
			for (int j = 0; j < 2; j++) {
				Fou f = new Fou(joueurs[i], new Position(i * 7, 2 + j * 3));
				echiquier.ajouterPiece(f);
			}
			Roi r = new Roi(joueurs[i], new Position(i * 7, 4));
			echiquier.ajouterPiece(r);

			Dame d = new Dame(joueurs[i], new Position(i * 7, 3));
			echiquier.ajouterPiece(d);
		}
	}

	/*
	 * Methode qui gere le deroulement d'une partie d'echecs. La partie se finis en
	 * cas de mat (en cas de pat, le programme est infini)
	 */
	public void lancer() {

		while (true) {

			compteurTours++;

			// affichage de l'echiquier
			echiquier.afficher();
			System.out.println("Tour " + compteurTours + " - joueur " + joueurs[compteurTours % 2]);

			// detection d'echec ppour le joueur actuel
			Piece p2 = echiquier.echec(joueurs[compteurTours % 2]);
			if (p2 != null) {
				System.out.println("Echec ! - " + p2);

				// detection de mat, et arret de la partie le cas echeant
				if (echiquier.mat(joueurs[compteurTours % 2])) {
					break;
				}
			}

			// selection de la piece
			Position p = null;

			while (p == null) {

				p = scanPosition("Veuillez choisir une piece :");
				if (echiquier.getPiece(p) == null) {
					System.out.println("Pas de piece a cette position.");
					p = null;
				} else if (echiquier.getPiece(p).getCouleur() != joueurs[compteurTours % 2]) {
					System.out.println("Ce n'est pas votre piece.");
					p = null;
				}
			}
			piece = echiquier.getPiece(p);

			p = null;

			// selection de la position/choix d'un nouvelle piece
			while (p == null) {
				System.out.println(piece);
				p = scanPosition("Veuillez choisir case :");
				Piece tmp = echiquier.getPiece(p);

				if (tmp != null) {
					if (tmp.getCouleur() == piece.getCouleur()) {
						piece = tmp;
						p = null;
						continue;
					}
				}

				// tentative du deplacement demande. on repasse dans la boucle si le mouvement
				// est interdit.
				tmp = echiquier.deplacer(piece.getPosition(), p, joueurs[compteurTours % 2]);
				if (tmp == null) {
					p = null;
				}
			}

		}

		// fin de la partie
		echiquier.afficher();
		System.out.println("... et mat !");
		System.out.println(
				"Victoire du joueur " + (echiquier.getPiece('R', joueurs[0]) == null ? joueurs[0] : joueurs[1]));
		scanner.close();
	}

	// demande des coordonnees au joueur, sous la forme A0
	private Position scanPosition(String message) {
		System.out.println(message);
		String buffer = scanner.next();
		while (buffer.length() < 2) {
			System.out.println("Coordonnees trop courtes");
			buffer = scanner.next();
		}
		int x = Character.getNumericValue(buffer.charAt(1));
		int y = buffer.toUpperCase().charAt(0) - 65;
		return new Position(x, y);
	}
}
