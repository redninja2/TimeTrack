import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class Duration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long duration;

	public Duration(Date start, Date end) {
		super();
		this.duration = end.getTime() - start.getTime();
	}
	
	/**
	 * 
	 * @return duration as milliseconds.
	 */
	public long getAsMilliseconds() {
		return this.duration;
	}

	public long getDayPart() {
		return TimeConversions.millisecondToDays(this.duration);
	}

	public long getHourPart() {
		return this.getAsHours() % 24;
	}

	public long getMinutePart() {
		return this.getAsMinutes() % 60;
	}

	public long getSecondPart() {
		return this.getAsSeconds() % 60;
	}
	
	public long getMillisecondPart() {
		return this.getAsMilliseconds() % 1000;
	}
	
	public long getAsDays() {
		return TimeConversions.millisecondToDays(this.duration);
	}
	
	public long getAsHours() {
		return TimeConversions.millisecondToHours(this.duration);
	}

	public long getAsMinutes() {
		return TimeConversions.millisecondToMinutes(this.duration);
	}
	
	public long getAsSeconds() {
		return TimeConversions.millisecondToSeconds(this.duration);
	}
	
	@Override
	public String toString() {
		DecimalFormat nf = new DecimalFormat("00");

		if (this.getDayPart() == 0) {
			return nf.format(this.getHourPart()) + ":" + nf.format(this.getMinutePart()) + ":" + nf.format(this.getSecondPart());
		}

		return String.valueOf(this.getDayPart()) + " days " + nf.format(this.getHourPart()) + ":" + nf.format(this.getMinutePart()) + ":"
				+ nf.format(this.getSecondPart());
	}
}
