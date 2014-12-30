package padhead.mvg.com.padhead.solver;

import java.util.ArrayList;

import padhead.mvg.com.padhead.service.PADHeadOverlayService;

/**
 * Created by Max on 12/27/2014.
 */
public class PADHeadSolverAsync extends PADHeadSolver{
	protected PADHeadOverlayService.AsyncSolve async;
	int progress = 0;
	public void solve(String input, int maxPathLengthL, boolean allow8Dir, PADHeadOverlayService.AsyncSolve asyncTask, ArrayList<OrbWeight> externWeights) {
		async = asyncTask;
		for(OrbWeight w : externWeights){
			System.out.println(w.getType() + " " + w.getNormalWeight() + " " + w.getMassWeight());
		}
		super.solve(input, maxPathLengthL, allow8Dir, externWeights);
	}

	protected void solveBoard(PADBoard board){
		progress += 10;
		async.doPublish(progress);
		super.solveBoard(board);
	}

	protected void solveBoardStep(PADSolveState solveState) {
		progress += 5;
		if(progress > 85){
			progress = 85;
		}
		async.doPublish(progress);
		super.solveBoardStep(solveState);
	}

	protected void finish(ArrayList<PADSolution> solutions){
		progress = 90;
		async.doPublish(progress);
		super.finish(solutions);
		progress = 100;
		async.doPublish(progress);
	}

	protected ArrayList<PADSolution> simplifySolutions(ArrayList<PADSolution> solutions){
		progress = 95;
		async.doPublish(progress);
		return super.simplifySolutions(solutions);
	}
}
