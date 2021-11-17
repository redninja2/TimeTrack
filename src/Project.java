import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Project implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean tracking;
	private ArrayList<TimePeriod> logTimes;

	public Project(String name) {
		super();
		this.name = name;
		this.tracking = false;
		this.logTimes = new ArrayList<TimePeriod>();
	}

	public String getName() {
		return this.name;
	}

	public long getTotalHours() {
		return TimeConversions.millisecondToHours(this.getTotalTimeAsMilliseconds());
	}

	public long getTotalTimeAsMilliseconds() {
		long totalMs = 0;
		for (TimePeriod tp : logTimes) {
			totalMs += tp.getDuration().getAsMilliseconds();
		}
		return totalMs;
	}

	public void startTracking() {
		if (!this.tracking) {
			logTimes.add(new TimePeriod());
			this.tracking = true;
		}
	}

	public void stopTracking() {
		if (this.tracking) {
			for (int i = logTimes.size() - 1; i >= 0; i--) {
				// Search through the time periods in reverse. Most recent should be the current
				// active one.
				TimePeriod temp = logTimes.get(i);
				if (temp.isActive()) {
					temp.endPeriod();
					this.tracking = false;
					return;
				}
			}
		}

	}
	
	public String getFormattedTime() {
		DecimalFormat nf = new DecimalFormat("00");
		long ms = this.getTotalTimeAsMilliseconds();
		long days = TimeConversions.millisecondToDays(ms);
		long hh = TimeConversions.millisecondToHours(ms) % 24;
		long mi = TimeConversions.millisecondToMinutes(ms) % 60;
		long ss = TimeConversions.millisecondToSeconds(ms) % 60;
		String ret = "";
		if (days != 0) {
			ret = Long.toString(days) + " days ";
		}
		return ret + nf.format(hh) + ":" + nf.format(mi) + ":" + nf.format(ss);
	}
	
	public boolean isTracking() {
		return this.tracking;
	}

	@Override
	public String toString() {
		return this.name + ": " + this.getTotalHours() + " hours";
	}
}
