package model;


import java.sql.Time;

public class Block {
	private Long id;
	private Time startTime, endTime;
	
	public Block(Long id, Time startTime, Time endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public Long getId() {
		return id;
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public Time getEndTime() {
		return endTime;
	}
	
    @Override
    public String toString() {
        return "Bloque " + id + ": " + startTime + " - " + endTime;
    }
}
