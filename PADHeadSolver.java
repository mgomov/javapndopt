package padhead.mvg.com.padhead.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Max on 12/27/2014.
 */
public class PADHeadSolver {
	int rows = 5;
	int cols = 6;

	float multiOrbBonus = 0.25f;
	float comboBonus = 0.25f;
	int maxSolutions = rows * cols * 8 * 2;

	PADBoard globalBoard = new PADBoard();

	int maxPathLength;
	boolean allow8Dir;
	protected ArrayList<OrbWeight> weights;

	public ArrayList<PADSolution> finalSolutions;

	public void solve(String input, int maxPathLengthL, boolean allow8Dir , ArrayList<OrbWeight> externWeights) {
		globalBoard = new PADBoard(input);
		maxPathLength = maxPathLengthL;
		this.allow8Dir = allow8Dir;

		// TODO: [DONE] allow customization
		weights = externWeights;
		solveBoard(globalBoard);
	}

	protected void solveBoard(PADBoard board) {
		//ArrayList<PADSolution> solutions = new ArrayList<PADSolution>(rows * cols);
		PADSolution[] solutions = new PADSolution[rows * cols];
		PADSolution seedSolution = new PADSolution(board.copy());
		inPlaceEvaluateSolution(seedSolution, weights);

		for (int i = 0, s = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j, ++s) {
				//solutions.set(s, new PADSolution(seedSolution, i, j));
				solutions[s] = new PADSolution(seedSolution, i, j);
			}
		}

		ArrayList<PADSolution> solns = new ArrayList<PADSolution>();

		for (int i = 0; i < rows * cols; i++) {
			solns.add(solutions[i]);
		}
		// TODO: [DONE] init solveState
		PADSolveState solveState = new PADSolveState(maxPathLength, allow8Dir, solns, weights);

		solveBoardStep(solveState);
	}

	protected void solveBoardStep(PADSolveState solveState) {
		if (solveState.p() >= solveState.maxLength()) {
			finish(solveState.getSolutions());
			return;
		}
		solveState.incrementP();
		solveState.setSolutions(evolveSolutions(solveState.getSolutions(), solveState.weights(), solveState.dirStep()));

		// TODO [UPDATE - irrelevant callback] work with callbacks
		// solve_state.step_callback(solve_state.p, solve_state.max_length);

		// TODO setTimeout funkiness, not sure if correct
		solveBoardStep(solveState);
	}

	protected void finish(ArrayList<PADSolution> solutions) {
		solutions = simplifySolutions(solutions);
		finalSolutions = solutions;
	}

	protected ArrayList<PADSolution> simplifySolutions (ArrayList<PADSolution> solutions){
		ArrayList<PADSolution> simplified = new ArrayList<PADSolution>();

		for(PADSolution solution : solutions){
			for(int s = simplified.size() - 1; s >= 0; --s){
				PADSolution simplifiedSolution = simplified.get(s);
				if(simplifiedSolution.getInitCursor().eq(solution.getInitCursor())){
					if(simplifiedSolution.cmpMatches(solution)){
						continue;
					}
				}
			}
			simplified.add(solution);
		}
		return simplified;
	}

	private ArrayList<PADSolution> evolveSolutions(ArrayList<PADSolution> solutions, ArrayList<OrbWeight> weights, int dirStep) {
		ArrayList<PADSolution> newSolutions = new ArrayList<PADSolution>();
		for (PADSolution s : solutions) {
			if (!s.isDone()) {
				for (int dir = 0; dir < 8; dir += dirStep) {
					if (canMoveOrbInSolution(s, dir)) {
						PADSolution solution = s.copy();
						inPlaceSwapOrbInSolution(solution, dir);
						inPlaceEvaluateSolution(solution, weights);
						newSolutions.add(solution);
					}
				}
				s.setDone();
			}
		}

		solutions.addAll(newSolutions);
		// TODO: [DONE] sort solutions by weight... //solutions.sort(function(a, b) {return b.weight - a.weight});

		Collections.sort(solutions, new Comparator<PADSolution>() {
			public int compare(PADSolution s1, PADSolution s2) {
				if (s1.getWeight() > s2.getWeight())
					return -1;
				else if (s1.getWeight() < s2.getWeight())
					return 1;
				else
					return 0;
			}
		});
		int sliceidx = maxSolutions;
		if(sliceidx > solutions.size()){
			sliceidx = solutions.size();
		}
		return new ArrayList<PADSolution>(solutions.subList(0, sliceidx));
	}

	private boolean canMoveOrbInSolution(PADSolution solution, int dir) {
		if (solution.getPath().size() > 0) {
			if (solution.getPath().get(solution.getPath().size() - 1) == (dir + 4) % 8) {
				return false;
			}
		}

		return canMoveOrb(solution.getCursor(), dir);
	}

	private boolean canMoveOrb(RowColumn rc, int dir) {
		switch (dir) {
			case 0:
				return rc.col < cols - 1;
			case 1:
				return rc.row < rows - 1 && rc.col < cols - 1;
			case 2:
				return rc.row < rows - 1;
			case 3:
				return rc.row < rows - 1 && rc.col > 0;
			case 4:
				return rc.col > 0;
			case 5:
				return rc.row > 0 && rc.col > 0;
			case 6:
				return rc.row > 0;
			case 7:
				return rc.row > 0 && rc.col < cols - 1;
		}
		return false;
	}

	private void inPlaceSwapOrbInSolution(PADSolution solution, int dir) {
		PADBoard board = inPlaceOrbSwap(solution.getBoard(), solution.getCursor(), dir);
		solution.setCursor(__rc);
		solution.getPath().add(dir);
	}

	private RowColumn __rc;

	private PADBoard inPlaceOrbSwap(PADBoard board, RowColumn rc, int dir) {
		RowColumn oldRC = rc.copy();
		inPlaceMoveRc(rc, dir);
		Orb originalType = board.getBoard()[oldRC.row][oldRC.col];
		board.getBoard()[oldRC.row][oldRC.col] = board.getBoard()[rc.row][rc.col];
		board.getBoard()[rc.row][rc.col] = originalType;
		__rc = rc;
		return board;
	}

	void inPlaceMoveRc(RowColumn rc, int dir) {
		switch (dir) {
			case 0:
				rc.col += 1;
				break;
			case 1:
				rc.row += 1;
				rc.col += 1;
				break;
			case 2:
				rc.row += 1;
				break;
			case 3:
				rc.row += 1;
				rc.col -= 1;
				break;
			case 4:
				rc.col -= 1;
				break;
			case 5:
				rc.row -= 1;
				rc.col -= 1;
				break;
			case 6:
				rc.row -= 1;
				break;
			case 7:
				rc.row -= 1;
				rc.col += 1;
				break;
		}
	}

	private PADBoard __matchBoard;

	private PADBoard inPlaceEvaluateSolution(PADSolution solution, ArrayList<OrbWeight> weights) {
		PADBoard currentBoard = solution.getBoard().copy();

		// TODO: [DONE] determine type of allMatches
		ArrayList<Match> allMatches = new ArrayList<Match>();

		while (true) {
			ArrayList<Match> matches = findMatches(currentBoard);

			// TODO: [DONE] determine what this means
			if (matches.size() == 0) {
				break;
			}

			inPlaceRemoveMatches(currentBoard, __matchBoard);
			inPlaceDropEmptySpaces(currentBoard);

			// TODO: [DONE] determine what this means
			allMatches.addAll(matches);
		}

		solution.setWeight(computeWeight(allMatches, weights));
		solution.setMatches(allMatches);
		return currentBoard;
	}

	PADBoard inPlaceRemoveMatches(PADBoard board, PADBoard matchBoard) {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				if (matchBoard.getBoard()[i][j] != null) {
					board.getBoard()[i][j] = new Orb('u');
				}
			}
		}
		return board;

	}

	PADBoard inPlaceDropEmptySpaces(PADBoard board) {
		for (int j = 0; j < cols; ++j) {
			int desti = rows - 1;
			for (int srci = rows - 1; srci >= 0; --srci) {
				if (board.getBoard()[srci][j].getType() != 'u') {
					board.getBoard()[desti][j] = board.getBoard()[srci][j].copy();
					--desti;
				}
			}
			for (; desti >= 0; --desti) {
				board.getBoard()[desti][j].setType('u');
			}
		}
		return board;
	}

	// TODO: [DONE] determine type of matches, then finish this function
	public float computeWeight(ArrayList<Match> matches, ArrayList<OrbWeight> weights) {
		float totalWeight = 0f;
		for (Match m : matches) {
			OrbWeight w = getOrbWeight(m.getType(), weights);
			float baseWeight = 0f;
			if (m.getCount() >= 5) {
				baseWeight = w.getMassWeight();
			} else {
				baseWeight = w.getNormalWeight();
			}
			float lmultiOrbBonus = (m.getCount() - 3f) * multiOrbBonus + 1f;
			totalWeight += lmultiOrbBonus * baseWeight;
		}
		float lcomboBonus = (matches.size() - 1f) * comboBonus + 1f;

		return totalWeight * lcomboBonus;
	}

	public OrbWeight getOrbWeight(char c, ArrayList<OrbWeight> wgts) {
		for (int i = 0; i < wgts.size(); i++) {
			if (wgts.get(i).getType() == c) {
				return wgts.get(i);
			}
		}
		System.out.println("Big problem... Orb weight not found in getOrbWeight!");
		return null;
	}

	public ArrayList<Match> findMatches(PADBoard board) {
		PADBoard matchBoard = new PADBoard();

		for (int i = 0; i < rows; ++i) {
			Orb prev1Orb = new Orb('u');
			Orb prev2Orb = new Orb('u');
			for (int j = 0; j < cols; ++j) {
				Orb currentOrb = board.getBoard()[i][j].copy();
				if (prev1Orb.eq(prev2Orb) && prev2Orb.eq(currentOrb) && currentOrb.getType() != 'u') {
					matchBoard.getBoard()[i][j] = currentOrb.copy();
					matchBoard.getBoard()[i][j - 1] = currentOrb.copy();
					matchBoard.getBoard()[i][j - 2] = currentOrb.copy();
				}
				prev1Orb = prev2Orb.copy();
				prev2Orb = currentOrb.copy();
			}
		}

		for (int j = 0; j < cols; ++j) {
			Orb prev1Orb = new Orb('u');
			Orb prev2Orb = new Orb('u');
			for (int i = 0; i < rows; ++i) {
				Orb currentOrb = board.getBoard()[i][j].copy();
				if (prev1Orb.eq(prev2Orb) && prev2Orb.eq(currentOrb) && currentOrb.getType() != 'u') {
					matchBoard.getBoard()[i][j] = currentOrb.copy();
					matchBoard.getBoard()[i - 1][j] = currentOrb.copy();
					matchBoard.getBoard()[i - 2][j] = currentOrb.copy();
				}
				prev1Orb = prev2Orb.copy();
				prev2Orb = currentOrb.copy();
			}
		}

		PADBoard scratchBoard = matchBoard.copy();

		ArrayList<Match> matches = new ArrayList<Match>();
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				Orb currentOrb;
				if(scratchBoard.getBoard()[i][j] == null){
					currentOrb = null;
				} else {
					currentOrb  = scratchBoard.getBoard()[i][j].copy();
				}

				if (currentOrb != null /*&& currentOrb.getType() != ('u')*/) {
					ArrayList<RowColumn> stack = new ArrayList<RowColumn>();
					stack.add(new RowColumn(i, j));
					int count = 0;
					while (stack.size() > 0) {
						RowColumn n = stack.remove(stack.size() - 1);
						if (scratchBoard.getBoard()[n.row][n.col] != null && scratchBoard.getBoard()[n.row][n.col].getType() == currentOrb.getType()) {
							count++;
							scratchBoard.getBoard()[n.row][n.col] = null;
							if (n.row > 0) {
								stack.add(new RowColumn(n.row - 1, n.col));
							}
							if (n.row < rows - 1) {
								stack.add(new RowColumn(n.row + 1, n.col));
							}
							if (n.col > 0) {
								stack.add(new RowColumn(n.row, n.col - 1));
							}
							if (n.col < cols - 1) {
								stack.add(new RowColumn(n.row, n.col + 1));
							}
						}
					}
					matches.add(new Match(currentOrb.copy(), count));
				}
			}
		}
		// TODO: [DONE - global var] determine how to handle this tuple return
		__matchBoard = matchBoard;
		return matches;
	}
}
