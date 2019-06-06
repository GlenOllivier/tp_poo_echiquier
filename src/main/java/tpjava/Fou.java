package tpjava;

/*
 * Classe qui represente un Fou
 */
public class Fou extends Piece {

	public Fou(char couleur, Position position) {
		super(couleur, position);
	}

	/*
	 * Deplacement en diagonale.
	 */
	@Override
	boolean positionPossible(Position position) {
		return (this.position.getX() != position.getX() && this.position.getY() != position.getY())
				&& (Math.abs(this.position.getX() - position.getX()) == Math
						.abs(this.position.getY() - position.getY()))
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8;
	}

	@Override
	char getSymbole() {
		return 'F';
	}

	@Override
	char getValeur() {
		return 3;
	}

	/*
	 * Redefinition de positionIntermediaires pour correspondre a un deplacement en
	 * diagonale.
	 */
	@Override
	public Position[] positionsIntermediaires(Position position) {
		Position[] p = this.positionsIntermediairesDiagonale(position);
		return (p == null) ? new Position[0] : p;
	}
}
