
public abstract class TimeConversions {

	public static long millisecondToDays(long ms) {
		return ms / (1000 * 60 * 60 * 24);
	}
	
	public static long millisecondToHours(long ms) {
		return ms / (1000 * 60 * 60);
	}

	public static long millisecondToMinutes(long ms) {
		return ms / (1000 * 60);
	}
	
	public static long millisecondToSeconds(long ms) {
		return ms / 1000;
	}
}
