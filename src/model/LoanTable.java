package model;

import java.sql.Timestamp;

public class LoanTable {
    private Long id;
    private Timestamp startTime, endTime;
    private String name, locationType, state;
    
    public LoanTable(Long id, String name, Timestamp startTime, Timestamp endTime, String locationType, String state) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationType = locationType;
        this.state = state;
    }

    public Long getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getState() {
        return state;
    }
}
