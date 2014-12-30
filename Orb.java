package padhead.mvg.com.padhead.solver;

/**
 * Created by Max on 12/27/2014.
 */
public class Orb {
	private char type;

	public Orb(){
		type = 'u';
	}

	public Orb(char t){
		type = t;
	}

	public char getType(){
		return type;
	}

	public void setType(char t){
		type = t;
	}
	
	public boolean eq(Orb other){
		if(this.type == other.type){
			return true;
		}
		return false;
	}
	
	public Orb copy(){
		return new Orb(type);
	}
}
