package model;

import java.time.LocalDate;

public class LoanTable {
    private Long id;
    private LocalDate date;
    private String name, locationType, state, specs, capacity;
    
    public LoanTable(Long id, String name, LocalDate date, String locationType, String state, String specs, String capacity) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.locationType = locationType;
        this.state = state;
        this.specs = specs;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public LocalDate getDate() {
        return date;
    }


    public String getLocationType() {
        return locationType;
    }

    public String getState() {
        return state;
    }
    
    public String getSpecs() {
    	return specs;
    }
    
    public String getCapacity() {
    	return capacity;
    }
}
