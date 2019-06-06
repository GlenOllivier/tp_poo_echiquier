package tpjava;

/*
 * Classe qui represente un cavalier
 */
public class Cavalier extends Piece {

	public Cavalier(char couleur, Position position) {
		super(couleur, position);
	}

	/*
	 * Deplacement en L
	 */
	@Override
	boolean positionPossible(Position position) {
		return (((Math.abs(this.position.getY() - position.getY()) == 2
				&& Math.abs(this.position.getX() - position.getX()) == 1)
				|| (Math.abs(this.position.getX() - position.getX()) == 2
						&& Math.abs(this.position.getY() - position.getY()) == 1))
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8);
	}

	@Override
	char getSymbole() {
		return 'C';
	}

	@Override
	char getValeur() {
		return 3;
	}

}
