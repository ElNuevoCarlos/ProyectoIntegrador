package model;

import java.time.LocalDate;

public class Loans {
    private Long id;
    private LocalDate date;
    private String specs, state, location, nameHall, emailUser;

    public Loans(Long id, String nameHall, String location, String emailUser, 
    		LocalDate date, String specs, String state) {
        this.id = id;
        this.nameHall = nameHall;
        this.location = location;
        this.emailUser = emailUser;
        this.date = date;
        this.specs = specs;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public String getNameHall() {
        return nameHall;
    }
    public String getLocation() {
        return location;
    }

    public String getEmailUser() {
        return emailUser;
    }
    
    public String getSpecs() {
        return specs;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public String getState() {
    	return state;
    }
}


