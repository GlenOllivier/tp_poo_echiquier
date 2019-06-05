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

	public Piece getPiece(char symbole, char couleur) {
		Iterator<Piece> i = pieces.iterator();
		Piece tmp;
		while (i.hasNext()) {
			tmp = i.next();
			if (tmp.getCouleur() == couleur && tmp.getSymbole() == symbole) {
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
				Piece tmp = getPiece(new Position(i, j));
				if (tmp == null) {
					ligne += "   ";
				} else {
					if (tmp.getCouleur() == 'N') {
						ligne += "*" + tmp.getSymbole() + "*";
					} else {
						ligne += " " + tmp.getSymbole() + " ";
					}
				}
				ligne += "|";
			}
			System.out.println(ligne);
			System.out.println("   ---------------------------------");
		}
	}

	public Piece deplacer(Position depart, Position arivee, char couleur) {
		Piece p = getPiece(depart);
		if (p == null) {
			System.out.println("Pas de piece sur cette case");
			return null;
		}

		if (p.getCouleur() != couleur) {
			System.out.println("Cette piece appartient au joueur " + p.getCouleur());
			return null;
		}

		Position[] intermediaires = p.positionsIntermediaires(arivee);
		for (int i = 0; i < intermediaires.length; i++) {
			Piece tmp = getPiece(intermediaires[i]);
			if (tmp != null) {
				System.out.println("Une piece bloque le mouvement en " + tmp.getPosition());
				return null;
			}
		}

		Piece p2 = getPiece(arivee);
		if (p2 != null) {
			if (p2.getCouleur() == couleur) {
				return p2;
			}
			if (!p.attaquePossible(arivee)) {
				System.out.println("Attaque impossible");
				return null;
			}
			try {
				p.attaque(arivee);
			} catch (ExceptionPosition e) {
				System.out.println("Erreur de deplacement inconnue...");
				return null;
			}
			pieces.remove(p2);
			return p;
		}

		if (!p.positionPossible(arivee)) {
			System.out.println("Mouvement interdit");
			return null;
		}

		try {
			p.deplacement(arivee);
		} catch (ExceptionPosition e) {
			System.out.println("Erreur de deplacement inconnue...");
			return null;
		}
		return p;
	}

	/*
	 * public int getPoints(char couleur) {} public void afficher() {} public void
	 * sauvegarde() {} public void chargement() {}
	 */
}
