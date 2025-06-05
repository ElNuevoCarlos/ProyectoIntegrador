package model;

import java.time.LocalDate;

public class SanctionInfo {
    private String typeSanction;
    private String description;
    private String state;
    private LocalDate endDate;
    private int amount;
    private Long idLoan;

    private LocalDate dateLoan;

    private String emailClient;

    public SanctionInfo(String typeSanction, String description, String state, LocalDate endDate, LocalDate dateLoan, int amount, String emailClient, Long idLoan) {
        this.typeSanction = typeSanction;
        this.description = description;
        this.state = state;
        this.endDate = endDate;
        this.amount = amount;
        this.dateLoan = dateLoan;
        this.emailClient = emailClient;
        this.idLoan = idLoan;
    }

    public String getTypeSanction() { 
    	return typeSanction; 
    }
    public void setTypeSanction(String newTypeSanction) { 
    	this.typeSanction = newTypeSanction;
    }
    
    public String getDescription() { 
    	return description; 
    }
    public void setDescription(String newDescription) { 
    	this.description = newDescription;
    }
    
    public String getState() { 
    	return state; 
    }
    public void setState(String newState) { 
    	this.state = newState;
    }
    
    public LocalDate getEndDate() { 
    	return endDate; 
    }
    
    public int getAmount() { 
    	return amount;
    }
    public void setAmount(int newAmount) { 
    	this.amount = newAmount;
    }
    
    public LocalDate getDateLoan() { 
    	return dateLoan; 
    }
    
    public String getEmailClient() { 
    	return emailClient; 
   }
    public Long getidLoan() {
    	return idLoan;
    }
}