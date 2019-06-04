package tpjava;

public class Dame extends Piece {

	public Dame(char couleur, Position position) {
		super(couleur, position);
	}

	@Override
	boolean positionPossible(Position position) {
		return (this.position.getX() != position.getX() || this.position.getY() != position.getY())
				&& ((this.position.getX() == position.getX() || this.position.getY() == position.getY()
						|| (Math.abs(this.position.getX() - position.getX()) == Math
								.abs(this.position.getY() - position.getY()))))
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8;
	}

	@Override
	char getSymbole() {
		return 'D';
	}

	@Override
	char getValeur() {
		return 9;
	}
}
