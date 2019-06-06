package tpjava;

public abstract class Piece {
	protected char couleur;
	protected Position position;
	protected boolean hasMoved;

	/*
	 * Seul constructeur autorise pour instancier une piece.
	 */
	public Piece(char couleur, Position position) {
		this.couleur = couleur;
		this.position = position;
		this.hasMoved = false;
	}

	/*
	 * Renvoie un booleen qui indique si la piece peux se deplacer a la position
	 * indiquee. Ne prend pas en compte l'encombrement des cases.
	 */
	abstract boolean positionPossible(Position position);

	/*
	 * Renvoie le symbole (qui represente le type) de la piece. Ce sybole est
	 * utilise pour l'affichage en mode texte.
	 */
	abstract char getSymbole();

	/*
	 * Renvoie la valeur de la piece (pion : 1, cavalier/fou/tour : 3, dame : 9, roi
	 * : 20).
	 */
	abstract char getValeur();

	/*
	 * Change la position de la piece a celle indiquee. Attention : cette methode ne
	 * fait aucune verification !!
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/*
	 * Effectue le deplacement de la piece, en verifiant que le mouvement est
	 * possible pour la piece. Attention : cette methode ne verifie pas la presence
	 * d'autres pieces.
	 */
	void deplacement(Position position) throws ExceptionPosition {
		if (positionPossible(position)) {
			this.position = position;
			this.hasMoved = true;
		} else {
			throw new ExceptionPosition();
		}

	}

	/*
	 * Getter pour la position de la piece.
	 */
	public Position getPosition() {
		return position;
	}

	/*
	 * Getter pour la couleur de la piece.
	 */
	public char getCouleur() {
		return couleur;
	}

	/*
	 * Affiche les caracteristiques de la pieces (mais pas hasMoved). Utilise par
	 * l'interface en mode texte.
	 */
	@Override
	public String toString() {
		return "Piece : " + this.getSymbole() + " Couleur : " + couleur + " Position : " + position;
	}

	/*
	 * Renvoie les positions traversees par la piece pour faire le deplacement vers
	 * position. Sert a verifier que ces cases sont libres avant de faire un
	 * mouvement. Attention : ne verifie pas si le mouvement demande est possible.
	 * Par defaut, renvoie un tableau vide. A redefinir pour les classes qui ont un
	 * mouvement bloque par les autres pieces.
	 */
	public Position[] positionsIntermediaires(Position position) {
		return new Position[0];
	}

	/*
	 * Renvoie les cases intermediaires pour un mouvement en diagonale. A appeler
	 * dans la redefinition de positioIntermediaires() pour les classes qui peuvent
	 * faire un mouvement en diagonale. Peux renvoyer null si le mouvement n'est pas
	 * possible, ou s'il n'est pas une diagonale
	 */
	protected Position[] positionsIntermediairesDiagonale(Position position) {
		// verifie que le mouvement est autorise, et qu'il correspond a une diagonale
		if (this.positionPossible(position) && position.getX() != this.getPosition().getX()
				&& position.getY() != this.getPosition().getY()) {

			// par defaut, on initialise comme si x2>x1
			int x1 = this.position.getX() + 1;
			int x2 = position.getX() - 1;

			// xBigger et yBigger serviront pour connaitre le sens de la diagonale
			boolean x1Bigger, y1Bigger;
			x1Bigger = y1Bigger = false;

			// si ca n'est pas le cas, on inverse
			if (this.position.getX() > position.getX()) {

				x1Bigger = true;
				x1 = position.getX() + 1;
				x2 = this.position.getX() - 1;
			}

			// meme traitement pour y1 et y2
			int y1 = this.position.getY() + 1;
			int y2 = position.getY() - 1;

			if (this.position.getY() > position.getY()) {

				y1Bigger = true;
				y1 = position.getY() + 1;
				y2 = this.position.getY() - 1;
			}

			// si xBigger == yBigger, on est dans le cas d'une diagonale nw -> se
			if (x1Bigger == y1Bigger) {

				// on met les positions traversees dans un tableau (nb. : si le mouvement n'est
				// que d'une seule case, le tableau aura une taille de 0)
				Position[] positions = new Position[x2 - x1 + 1];
				for (int i = 0; x1 + i <= x2; i++) {
					Position intermediaire = new Position(x1 + i, y1 + i);
					positions[i] = intermediaire;
				}
				// on renvoie le tableau de positions
				return positions;
			}

			// dans le cas ou x1Bigger != y1Bigger, c'est une diagonale ne->sw
			Position[] positions = new Position[x2 - x1 + 1];
			for (int i = 0; x1 + i <= x2; i++) {

				// seules les valeurs des positions changent
				Position intermediaire = new Position(x1 + i, y2 - i);
				positions[i] = intermediaire;
			}

			// on renvoie le tableau de positions
			return positions;
		}

		// renvoie null si le mouvement n'est pas possible, ou si le mouvement n'est pas
		// une diagonale
		return null;

	}

	/*
	 * Renvoie les cases intermediaires pour un mouvement en ligne. A appeler dans
	 * la redefinition de positioIntermediaires() pour les classes qui peuvent faire
	 * un mouvement en ligne. Peux renvoyer null si le mouvement n'est pas possible,
	 * ou s'il n'est pas une ligne
	 */
	protected Position[] positionsIntermediairesLigne(Position position) {

		// on verifie que le mouvement est possible
		if (this.positionPossible(position)) {

			// x constant : ligne
			if (this.position.getX() == position.getX()) {

				// on regarde si y1 > y2 ou inversement
				int y1 = this.position.getY() + 1;
				int y2 = position.getY() - 1;

				if (this.position.getY() > position.getY()) {

					y1 = position.getY() + 1;
					y2 = this.position.getY() - 1;
				}

				// on met les cases traversees dans un tableau
				Position[] positions = new Position[y2 - y1 + 1];
				for (int i = 0; y1 + i <= y2; i++) {
					Position intermediaire = new Position(this.position.getX(), y1 + i);
					positions[i] = intermediaire;
				}

				// on renvoie le tableau de positions
				return positions;
			}

			// y constant : colonne
			// meme traitement
			if (this.position.getY() == position.getY()) {

				int x1 = this.position.getX() + 1;
				int x2 = position.getX() - 1;

				if (this.position.getX() > position.getX()) {

					x1 = position.getX() + 1;
					x2 = this.position.getX() - 1;
				}
				Position[] positions = new Position[x2 - x1 + 1];
				for (int i = 0; x1 + i <= x2; i++) {
					Position intermediaire = new Position(x1 + i, this.position.getY());
					positions[i] = intermediaire;
				}
				return positions;
			}
		}

		// on renvoie null si le mouvement n'est pas une ligne, ou s'il n'est pas
		// possible
		return null;
	}

	/*
	 * Methode qui teste si l'attaque vers une case est possible. Par defaut, se
	 * contente de verifier si le mouvement est possible. Attention : ne verifie pas
	 * la presence de pieces intermediaires ou a l'arrivee. A redefinir pour des
	 * pieces qui n'attaquen pas de la meme facon qu'elles se deplacent (Pions).
	 */
	public boolean attaquePossible(Position position) {
		return this.positionPossible(position);
	}

	/*
	 * Effectue une attaque vers la position indiquee. Verifie si l'attaque est
	 * possible avant d'attaquer. Attention : ne verifie pas les cases
	 * intermediaires avant d'attaquer, ni la presence d'une piece ennemie sur la
	 * case ciblee.
	 */
	void attaque(Position position) throws ExceptionPosition {
		if (attaquePossible(position)) {
			this.position = position;
			this.hasMoved = true;
		} else {
			throw new ExceptionPosition();
		}

	}
}
