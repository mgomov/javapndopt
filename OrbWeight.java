package padhead.mvg.com.padhead.solver;

/**
 * Created by Max on 12/27/2014.
 */
public class OrbWeight {
	private char type;
	private float normalWeight;
	private float massWeight;

	public OrbWeight(char t, float nw, float mw){
		type = t;
		normalWeight = nw;
		massWeight = mw;
	}

	public float getMassWeight(){
		return massWeight;
	}

	public float getNormalWeight(){
		return normalWeight;
	}

	public void setNormalWeight(float nw){
		normalWeight = nw;
	}

	public void setMassWeight(float mw){
		massWeight = mw;
	}


	public char getType(){
		return type;
	}
}
