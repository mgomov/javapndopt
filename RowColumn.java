package padhead.mvg.com.padhead.solver;

/**
 * Created by Max on 12/27/2014.
 */
public class RowColumn {
	public int row;
	public int col;

	public RowColumn(int x, int y) {
		row = x;
		col = y;
	}

	public RowColumn copy() {
		return new RowColumn(row, col);
	}

	public boolean eq(RowColumn other) {
		if (this.row == other.row && this.col == other.col) {
			return true;
		}
		return false;
	}
}
