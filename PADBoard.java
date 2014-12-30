package padhead.mvg.com.padhead.solver;

/**
 * Created by Max on 12/27/2014.
 */
public class PADBoard {
	private Orb[][] board;

	public PADBoard() {
		board = new Orb[5][6];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				//board[i][j] = new Orb('u');
			}
		}
	}

	public PADBoard(String in) {
		board = new Orb[5][6];
		int idx = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				board[i][j] = new Orb(in.charAt(idx));
				idx++;
			}
		}
	}

	public PADBoard(Orb[][] obs) {
		board = new Orb[5][6];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				if (obs[i][j] != null)
					board[i][j] = obs[i][j].copy();
			}
		}
	}

	public Orb[][] getBoard() {
		return board;
	}

	public PADBoard copy() {
		return new PADBoard(board);
	}
	
	public String toString(){
		String s = "\nBoard:\n";
		
		for(int i = 0; i < 5; i++){
			for(int j = 0 ; j < 6; j++){
				s += (board[i][j].getType() + "\t");
			}
			s += "\n";
		}
		
		return s;
	}
}
