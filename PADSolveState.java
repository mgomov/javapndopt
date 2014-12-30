package padhead.mvg.com.padhead.solver;

import java.util.ArrayList;

/**
 * Created by Max on 12/27/2014.
 */
public class PADSolveState {
	public PADSolveState(int mxln, boolean allow8Dir, ArrayList<PADSolution> slns, ArrayList<OrbWeight> wgts){
		maxLength = mxln;
		if(allow8Dir){
			dirStep = 1;
		} else {
			dirStep = 2;
		}

		solutions = slns;
		weights = wgts;
	}

	private int maxLength;
	private int dirStep;
	private int p;
	private ArrayList<PADSolution> solutions;
	private ArrayList<OrbWeight> weights;

	public int maxLength(){
		return maxLength;
	}

	public int p(){
		return p;
	}

	public void incrementP(){
		p++;
	}

	public void setSolutions(ArrayList<PADSolution> slns){
		solutions = slns;
	}

	public ArrayList<PADSolution> getSolutions(){
		return solutions;
	}

	public ArrayList<OrbWeight> weights(){
		return weights;
	}

	public int dirStep(){
		return dirStep;
	}
}
