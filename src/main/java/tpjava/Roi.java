package tpjava;

/*
 * Classe qui represente un Roi
 */
public class Roi extends Piece {

	public Roi(char couleur, Position position) {
		super(couleur, position);
	}

	/*
	 * Deplacement d'une case en ligne ou en diagonale.
	 */
	@Override
	boolean positionPossible(Position position) {
		return ((this.position.getX() != position.getX() || this.position.getY() != position.getY())
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8
				&& position.getX() >= this.position.getX() - 1 && position.getY() >= this.position.getY() - 1
				&& position.getX() <= this.position.getX() + 1 && position.getY() <= this.position.getY() + 1);
	}

	@Override
	char getSymbole() {
		return 'R';
	}

	@Override
	char getValeur() {
		return 20;
	}
}
