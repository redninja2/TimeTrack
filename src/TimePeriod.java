import java.io.Serializable;
import java.util.Date;

public class TimePeriod implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date startDateTime;
	private Date endDateTime;
	private Duration duration;
	private boolean isActive;

	public TimePeriod(Date startDateTime, Date endDateTime) {
		super();
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.calculateDuration();
		this.isActive = false;
	}

	public TimePeriod() {
		super();
		this.startDateTime = new Date();
		this.isActive = true;
	}

	public void endPeriod() {
		this.endDateTime = new Date();
		this.calculateDuration();
		this.isActive = false;
	}
	
	public boolean isActive() {
		return this.isActive;
	}

	public Date getStartDate() {
		return this.startDateTime;
	}

	public Date getEndDate() {
		if (this.isActive) {
			return null;
		}
		return this.endDateTime;
	}

	public Duration getDuration() {
		if (this.isActive) {
			return new Duration(this.startDateTime,new Date());
		}
		return this.duration;
	}

	private void calculateDuration() {
		if (this.isActive) {
			this.duration = null;
		}
		this.duration = new Duration(this.startDateTime, this.endDateTime);
	}
}
