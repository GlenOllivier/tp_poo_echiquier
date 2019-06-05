package tpjava;

public class Pion extends Piece {

	public Pion(char couleur, Position position) {
		super(couleur, position);
	}

	@Override
	boolean positionPossible(Position position) {
		return this.position.getY() == position.getY()
				&& position.getX() == ((this.couleur == 'B') ? (this.position.getX() - 1) : (this.position.getX() + 1))
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8;

	}

	@Override
	public boolean attaquePossible(Position position) {
		return Math.abs(this.position.getY() - position.getY()) == 1
				&& position.getX() == ((this.couleur == 'B') ? (this.position.getX() - 1) : (this.position.getX() + 1))
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8;
	}

	@Override
	char getSymbole() {
		return 'P';
	}

	@Override
	char getValeur() {
		return 1;
	}
}
