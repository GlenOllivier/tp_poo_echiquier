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

		// verification qu'aucune piece ne bloque le deplacement
		Position[] intermediaires = p.positionsIntermediaires(arivee);
		for (int i = 0; i < intermediaires.length; i++) {
			Piece tmp = getPiece(intermediaires[i]);
			if (tmp != null) {
				System.out.println("Une piece bloque le mouvement en " + tmp.getPosition());
				return null;
			}
		}

		// test pour savoir s'il y a une piece a l'arivee
		Piece p2 = getPiece(arivee);
		if (p2 != null) {

			// verification que la piece a l'arivee est ennemie
			if (p2.getCouleur() == couleur) {
				return p2;
			}

			// verification que l'attaque est possible
			if (!p.attaquePossible(arivee)) {
				System.out.println("Attaque impossible");
				return null;
			}

			// tentative d'attaque
			try {
				p.attaque(arivee);
			} catch (ExceptionPosition e) {
				System.out.println("Erreur de deplacement inconnue...");
				return null;
			}

			// on enleve la piece attaquee
			pieces.remove(p2);

			// test d'echec apres l'attaque
			Piece p3 = echec(couleur);
			if (p3 != null) {

				// en cas d'echec, restauration de l'etat avant l'attaque
				p.setPosition(depart);
				pieces.add(p2);
				System.out.println("Mouvement impossible, echec - " + p3);
				return null;
			}

			// on renvoie la piece de depart si l'attaque est reussie
			return p;
		}

		// test si le mouvement est autorise (cas ou il n'y a pas de piece a l'arivee)
		if (!p.positionPossible(arivee)) {
			System.out.println("Mouvement interdit");
			return null;
		}

		// tentative de deplacement
		try {
			p.deplacement(arivee);
		} catch (ExceptionPosition e) {
			System.out.println("Erreur de deplacement inconnue...");
			return null;
		}

		// test d'echec apres le deplacement
		Piece p3 = echec(couleur);
		if (p3 != null) {

			// en cas d'echec, restauration de l'etat avant le deplacement
			p.setPosition(depart);
			System.out.println("Mouvement impossible, echec - " + p3);
			return null;
		}

		// on renvoie la piece de depart si le mouvement est reussi
		return p;
	}

	/*
	 * Methode qui permet de savoir si le roi du joueur de la couleur specifiee est
	 * en echec. Renvoie une des pieces qui menace le roi, ou null s'il n'y en a
	 * pas.
	 */
	public Piece echec(char couleur) {
		Piece p = getPiece('R', couleur);
		Iterator<Piece> i = pieces.iterator();
		Piece tmp;

		// test de l'attaque du roi pour toutes les pieces de l'autre couleur
		while (i.hasNext()) {
			tmp = i.next();
			if (tmp.getCouleur() != couleur && tmp.attaquePossible(p.getPosition())) {

				// si l'attaque est possible, teste si une piece bloque l'attaque
				Piece bloquante = null;
				Position[] intermediaires = tmp.positionsIntermediaires(p.getPosition());
				for (int j = 0; j < intermediaires.length; j++) {
					if (getPiece(intermediaires[j]) != null) {
						bloquante = getPiece(intermediaires[j]);
					}
				}

				// si aucune piece ne bloque, l'attaque est possible et on renvoie la piece
				// attaquante
				if (bloquante == null) {
					return tmp;
				}
			}
		}

		// si aucune piece ne peux attaquer, il n'y a pas d'echec
		return null;
	}

	/*
	 * Methode qui permet de savoir si le roi de la couleur specifiee est en echec
	 * et mat. Renvoie true si c'est le cas, false sinon.
	 */
	public boolean mat(char couleur) {

		// s'il n'y a pas d'echec, il n'y a pas mat
		if (echec(couleur) == null) {
			return false;
		}

		// en cas d'echec, test de tous les deplacements et attaques possibles pour
		// voir si une d'entre elles permet de ne plus etre en echec
		Iterator<Piece> i = pieces.iterator();
		Piece tmp;
		while (i.hasNext()) {
			tmp = i.next();

			// verification que la piece est de la bonne couleur avant de tester des
			// deplacements
			if (tmp.getCouleur() == couleur) {

				// parcours de toutes les cases du plateau pour chaque piece
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {

						// sauvegarde de l'etat avant deplacement pour pouvoir le restaurer
						Position arivee = new Position(x, y);
						Position depart = new Position(tmp.getPosition().getX(), tmp.getPosition().getY());

						// test de bloquage du deplacement
						Position[] intermediaires = tmp.positionsIntermediaires(arivee);
						boolean isBlocked = false;
						for (int j = 0; j < intermediaires.length; j++) {
							Piece tmp2 = getPiece(intermediaires[j]);
							if (tmp2 != null) {
								isBlocked = true;
							}
						}

						// si le deplacement est bloque, on stoppe cette iteration
						if (isBlocked) {
							continue;
						}

						// recherche de piece a l'arivee
						Piece p2 = getPiece(arivee);
						if (p2 != null) {

							// si la piece a l'arivee est de la couleur du joueur en echec, on stoppe
							// l'iteration
							if (p2.getCouleur() == couleur) {
								continue;
							}

							// sinon, tentative d'attaquer
							try {
								tmp.attaque(arivee);
							} catch (ExceptionPosition e) {
								continue;
							}

							// en cas d'attaque reussie, on met la piece attaquee loin pour ne pas perturber
							// le test d'echec. Si on l'enlevais le la liste, ça poserais probleme de la
							// remettre dans la liste alors qu'on est en train de la parcourir.
							p2.setPosition(new Position(30, 100));

							// test d'echec dans la nouvelle configuration
							Piece p3 = echec(couleur);

							// restauration du placement de depart
							tmp.setPosition(depart);
							p2.setPosition(arivee);

							// s'il n'y a plus echec dans la nouvelle configuration, ce n'est pas un mat et
							// on sort
							if (p3 == null) {
								return false;
							}

							// sinon, on stoppe l'iteration
							continue;
						}

						// s'il n'y a pas de piece a l'arivee, on regarde si le deplacement est possible
						if (!tmp.positionPossible(arivee)) {

							// s'il n'est pas possible, on stoppe l'iteration
							continue;
						}

						// tentative de deplacement
						try {
							tmp.deplacement(arivee);
						} catch (ExceptionPosition e) {
							continue;
						}

						// test d'echec dans la nouvelle configuration
						Piece p3 = echec(couleur);

						// restauration du placement de depart
						tmp.setPosition(depart);

						// s'il n'y a plus echec dans la nouvelle configuration, ce n'est pas un mat et
						// on sort
						if (p3 == null) {
							return false;
						}
					}
				}
			}
		}

		// si aucune piece ne peux effectuer de deplacement qui arrete l'echec du roi,
		// c'est un mat
		return true;
	}

}
