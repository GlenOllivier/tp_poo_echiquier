package tpjava;

/*
 * Classe qui represente un Pion
 */
public class Pion extends Piece {

	public Pion(char couleur, Position position) {
		super(couleur, position);
	}

	/*
	 * Deplacement d'une case en colonne, ou de deux cases s'il n'a pas encore bouge
	 * (hasMoved).
	 */
	@Override
	boolean positionPossible(Position position) {
		return this.position.getY() == position.getY()
				&& (position.getX() == ((this.couleur == 'B') ? (this.position.getX() - 1) : (this.position.getX() + 1))
						|| (!this.hasMoved && position.getX() == ((this.couleur == 'B') ? (this.position.getX() - 2)
								: (this.position.getX() + 2))))
				&& position.getX() >= 0 && position.getY() >= 0 && position.getX() < 8 && position.getY() < 8;

	}

	/*
	 * Attaque differente du deplacement pour un pion, redefinition de la methode
	 * attaquePossible. Attaque d'une case en diagonale devant le pion.
	 */
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

	/*
	 * Deplacement en ligne, sert si le pion veux avancer de deux cases.
	 */
	@Override
	public Position[] positionsIntermediaires(Position position) {
		Position[] p = this.positionsIntermediairesLigne(position);
		return (p == null) ? new Position[0] : p;
	}
}
