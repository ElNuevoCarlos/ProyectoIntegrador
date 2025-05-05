package model;

import java.sql.Date;

public class ClassSchedule {
	private long id, idHall;
	private int day;
	private Date startTime, endTime;
	
	public ClassSchedule(long id, int day, Date startTime, Date endTime, long idHall) {
		super();
		this.id = id;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.idHall = idHall;
	}
	
	public long getId() {
		return id;
	}
	
	public int getDay() {
		return day;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public long getIdHall() {
		return idHall;
	}
}
