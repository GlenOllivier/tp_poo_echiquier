package tpjava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Echiquier {
	Collection<Piece> pieces;

	public Echiquier() {
		pieces = new ArrayList<Piece>();
	}

	public void ajouterPiece(Piece p) {
		if (getPiece(p.getPosition()) != null) {
			return;
		}
		pieces.add(p);
	}

	public Piece getPiece(Position p) {
		Iterator<Piece> i = pieces.iterator();
		Piece tmp;
		while (i.hasNext()) {
			tmp = i.next();
			if (tmp.getPosition().equals(p)) {
				return tmp;
			}
		}
		return null;
	}

	public int getPoints(char couleur) {
		Iterator<Piece> i = pieces.iterator();
		Piece tmp;
		int points = 0;
		while (i.hasNext()) {
			tmp = i.next();
			if (tmp.getCouleur() == couleur) {
				points += tmp.getValeur();
			}
		}
		return points;
	}

	public void afficher() {
		System.out.println("   | A | B | C | D | E | F | G | H |");
		System.out.println("------------------------------------");
		for (int i = 0; i < 8; i++) {
			String ligne = " " + i + " |";
			for (int j = 0; j < 8; j++) {
				ligne += " ";
				Piece tmp = getPiece(new Position(i, j));
				if (tmp == null) {
					ligne += " ";
				} else {
					ligne += tmp.getSymbole();
				}
				ligne += " |";
			}
			System.out.println(ligne);
			System.out.println("   ---------------------------------");
		}
	}

	/*
	 * public int getPoints(char couleur) {} public void afficher() {} public void
	 * sauvegarde() {} public void chargement() {}
	 */
}
