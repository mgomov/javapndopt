package padhead.mvg.com.padhead.solver;

/**
 * Created by Max on 12/27/2014.
 */
public class Match {
	private Orb orb;
	private int count;

	public Match(Orb o, int c) {
		orb = o;
		count = c;
	}

	public char getType() {
		return orb.getType();
	}

	public int getCount() {
		return count;
	}

	public Orb getOrb() {
		return orb;
	}

	public Match copy() {
		return new Match(orb, count);
	}

	public boolean eq(Match other) {
		if (this.orb.getType() == other.orb.getType() && this.count == other.count) {
			return true;
		}

		return false;
	}
}
