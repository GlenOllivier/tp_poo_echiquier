package tpjava;

public abstract class Piece {
	protected char couleur;
	protected Position position;

	abstract boolean positionPossible(Position position);

	abstract char getSymbole();

	abstract char getValeur();

	void deplacement(Position position) throws ExceptionPosition {
		if (positionPossible(position)) {
			this.position = position;
		} else {
			throw new ExceptionPosition();
		}

	}

	public Piece(char couleur, Position position) {
		this.couleur = couleur;
		this.position = position;
	}

	@Override
	public String toString() {
		return "" + getSymbole() + couleur + position;
	}

	public Position getPosition() {
		return position;
	}

	public char getCouleur() {
		return couleur;
	}
}
