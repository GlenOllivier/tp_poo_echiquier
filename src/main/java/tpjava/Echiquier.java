package tpjava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/*
 * Classe qui represente un echiquier. Contient la liste des pieces presentes sur l'echiquier.
 */
public class Echiquier {
	Collection<Piece> pieces;

	/*
	 * Constructeur qui instancie un echiquier vide.
	 */
	public Echiquier() {
		pieces = new ArrayList<Piece>();
	}

	/*
	 * Ajoute une piece sur l'echiquier si la case est disponible.
	 */
	public void ajouterPiece(Piece p) {
		if (getPiece(p.getPosition()) != null) {
			return;
		}
		pieces.add(p);
	}

	/*
	 * Renvoie la piece a la position demandee, ou null s'il n'y en a pas.
	 */
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

	/*
	 * Renvoie la premiere piece du symbole et de la couleur demandee. Sert par
	 * exemple a recuperer le roi pour tester les echecs.
	 */
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

	/*
	 * Renvoie la somme de points de la couleur demandee. Les points fonctionnent
	 * comme indique dans Piece
	 */
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

	/*
	 * Affichage de l'echiquier en mode texte. Ne gere pas la presence de plusieurs
	 * pieces sur une case, car ca n'est pas sense arriver. Les pices du joueur 'N'
	 * sont entourees de *.
	 */
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

	/*
	 * Methode qui effectue le deplacement d'une piece pour un joueur. Verifie si le
	 * deplacement est possible, s'il est bloque par certaines pieces, si'l met le
	 * roi allie en echec... Renvoie null si le deplacement n'est pas autorise apres
	 * tous les tests
	 */
	public Piece deplacer(Position depart, Position arivee, char couleur) {

		// verification qu'il y a bien une piece sur la case de depart
		Piece p = getPiece(depart);
		if (p == null) {
			System.out.println("Pas de piece sur cette case");
			return null;
		}

		// verification que la piece appartient au joueur dont c'est le tour
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
			Piece p3 = echec(couleur);
			if (p3 != null) {
				p.setPosition(depart);
				pieces.add(p2);
				System.out.println("Mouvement impossible, echec - " + p3);
				return null;
			}
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
		Piece p3 = echec(couleur);
		if (p3 != null) {
			p.setPosition(depart);
			System.out.println("Mouvement impossible, echec - " + p3);
			return null;
		}
		return p;
	}

	public Piece echec(char couleur) {
		Piece p = getPiece('R', couleur);
		Iterator<Piece> i = pieces.iterator();
		Piece tmp;
		while (i.hasNext()) {
			tmp = i.next();
			if (tmp.getCouleur() != couleur && tmp.attaquePossible(p.getPosition())) {
				Piece bloquante = null;
				Position[] intermediaires = tmp.positionsIntermediaires(p.getPosition());
				for (int j = 0; j < intermediaires.length; j++) {
					if (getPiece(intermediaires[j]) != null) {
						bloquante = getPiece(intermediaires[j]);
					}
				}
				if (bloquante == null) {
					return tmp;
				}
			}
		}
		return null;
	}

	public boolean mat(char couleur) {
		if (echec(couleur) == null) {
			return false;
		}
		Iterator<Piece> i = pieces.iterator();
		Piece tmp;
		while (i.hasNext()) {
			tmp = i.next();
			if (tmp.getCouleur() == couleur) {
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {
						Position arivee = new Position(x, y);
						Position depart = new Position(tmp.getPosition().getX(), tmp.getPosition().getY());
						Position[] intermediaires = tmp.positionsIntermediaires(arivee);
						boolean isBlocked = false;
						for (int j = 0; j < intermediaires.length; j++) {
							Piece tmp2 = getPiece(intermediaires[j]);
							if (tmp2 != null) {
								isBlocked = true;
							}
						}
						if (isBlocked) {
							continue;
						}
						Piece p2 = getPiece(arivee);
						if (p2 != null) {
							if (p2.getCouleur() == couleur) {
								continue;
							}
							try {
								tmp.attaque(arivee);
							} catch (ExceptionPosition e) {
								continue;
							}
							p2.setPosition(new Position(30, 100));
							Piece p3 = echec(couleur);
							tmp.setPosition(depart);
							p2.setPosition(arivee);

							if (p3 == null) {
								return false;
							}
							continue;
						}
						if (!tmp.positionPossible(arivee)) {
							continue;
						}

						try {
							tmp.deplacement(arivee);
						} catch (ExceptionPosition e) {
							continue;
						}
						Piece p3 = echec(couleur);
						tmp.setPosition(depart);
						if (p3 == null) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

}
