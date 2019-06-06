package tpjava;

public class Tour extends Piece {

	public Tour(char couleur, Position position) {
		super(couleur, position);
	}

	/*
	 * Deplacement en ligne.
	 */
	@Override
	boolean positionPossible(Position position) {
		return (this.position.getX() != position.getX() || this.position.getY() != position.getY())
				&& (this.position.getX() == position.getX() || this.position.getY() == position.getY())
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8;
	}

	@Override
	char getSymbole() {
		return 'T';
	}

	@Override
	char getValeur() {
		return 3;
	}

	/*
	 * Redefinition de positionIntermediaires pour correspondre a un deplacement en
	 * ligne.
	 */
	@Override
	public Position[] positionsIntermediaires(Position position) {
		Position[] p = this.positionsIntermediairesLigne(position);
		return (p == null) ? new Position[0] : p;
	}
}
