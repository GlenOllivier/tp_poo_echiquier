package tpjava;

public abstract class Piece {
	protected char couleur;
	protected Position position;
	protected boolean hasMoved;

	abstract boolean positionPossible(Position position);

	abstract char getSymbole();

	abstract char getValeur();

	public void setPosition(Position position) {
		this.position = position;
	}

	void deplacement(Position position) throws ExceptionPosition {
		if (positionPossible(position)) {
			this.position = position;
			this.hasMoved = true;
		} else {
			throw new ExceptionPosition();
		}

	}

	public Piece(char couleur, Position position) {
		this.couleur = couleur;
		this.position = position;
		this.hasMoved = false;
	}

	public Position getPosition() {
		return position;
	}

	public char getCouleur() {
		return couleur;
	}

	@Override
	public String toString() {
		return "Piece : " + this.getSymbole() + " Couleur : " + couleur + " Position : " + position;
	}

	public Position[] positionsIntermediaires(Position position) {
		return new Position[0];
	}

	protected Position[] positionsIntermediairesDiagonale(Position position) {
		if (this.positionPossible(position) && position.getX() != this.getPosition().getX()
				&& position.getY() != this.getPosition().getY()) {
			int x1 = this.position.getX() + 1;
			int x2 = position.getX() - 1;
			boolean x1Bigger, y1Bigger;
			x1Bigger = y1Bigger = false;

			if (this.position.getX() > position.getX()) {

				x1Bigger = true;
				x1 = position.getX() + 1;
				x2 = this.position.getX() - 1;
			}
			int y1 = this.position.getY() + 1;
			int y2 = position.getY() - 1;

			if (this.position.getY() > position.getY()) {

				y1Bigger = true;
				y1 = position.getY() + 1;
				y2 = this.position.getY() - 1;
			}
			if (x1Bigger == y1Bigger) {
				Position[] positions = new Position[x2 - x1 + 1];
				for (int i = 0; x1 + i <= x2; i++) {
					Position intermediaire = new Position(x1 + i, y1 + i);
					positions[i] = intermediaire;
				}
				return positions;
			}
			Position[] positions = new Position[x2 - x1 + 1];
			for (int i = 0; x1 + i <= x2; i++) {
				Position intermediaire = new Position(x1 + i, y2 - i);
				positions[i] = intermediaire;
			}
			return positions;
		}
		return null;

	}

	protected Position[] positionsIntermediairesLigne(Position position) {

		if (this.positionPossible(position)) {

			if (this.position.getX() == position.getX()) {

				int y1 = this.position.getY() + 1;
				int y2 = position.getY() - 1;

				if (this.position.getY() > position.getY()) {

					y1 = position.getY() + 1;
					y2 = this.position.getY() - 1;
				}

				Position[] positions = new Position[y2 - y1 + 1];
				for (int i = 0; y1 + i <= y2; i++) {
					Position intermediaire = new Position(this.position.getX(), y1 + i);
					positions[i] = intermediaire;
				}
				return positions;
			}
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
		return null;
	}

	public boolean attaquePossible(Position position) {
		return this.positionPossible(position);
	}

	void attaque(Position position) throws ExceptionPosition {
		if (attaquePossible(position)) {
			this.position = position;
			this.hasMoved = true;
		} else {
			throw new ExceptionPosition();
		}

	}
}
