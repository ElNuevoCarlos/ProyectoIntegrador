package model;

import java.sql.Timestamp;

public class LoanTable {
    private Long id;
    private Timestamp date;
    private String name, locationType, state;
    
    public LoanTable(Long id, String name, Timestamp date, String locationType, String state) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.locationType = locationType;
        this.state = state;
    }

    public Long getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public Timestamp getDate() {
        return date;
    }


    public String getLocationType() {
        return locationType;
    }

    public String getState() {
        return state;
    }
}
