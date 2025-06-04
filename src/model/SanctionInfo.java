package model;

import java.time.LocalDate;

public class SanctionInfo {
    private String typeSanction;
    private String description;
    private String state;
    private LocalDate endDate;
    private int amount;

    private LocalDate dateLoan;

    private String emailClient;

    public SanctionInfo(String typeSanction, String description, String state, LocalDate endDate, LocalDate dateLoan, int amount, String emailClient) {
        this.typeSanction = typeSanction;
        this.description = description;
        this.state = state;
        this.endDate = endDate;
        this.amount = amount;
        this.dateLoan = dateLoan;
        this.emailClient = emailClient;
    }

    public String getTypeSanction() { 
    	return typeSanction; 
    }
    
    public String getDescription() { 
    	return description; 
    }
    
    public String getState() { 
    	return state; 
    }
    
    public LocalDate getEndDate() { 
    	return endDate; 
    }
    
    public int getAmount() { 
    	return amount;
    }
    public LocalDate getDateLoan() { 
    	return dateLoan; 
    }
    
    public String getEmailClient() { 
    	return emailClient; 
   }
}