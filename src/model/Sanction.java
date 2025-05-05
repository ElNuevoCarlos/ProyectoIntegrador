package model;

import java.sql.Date;

public class Sanction {
    private long id, idLoanHall, idLoanDevice;
    private String typeSanction, description, state;
    private Date sanctionDate, endDate;
    private int amount;
    
    public Sanction(long id, String typeSanction, String description, String state, 
                    Date sanctionDate, Date endDate, int amount, long idLoanHall, long idLoanDevice) {
        super();
        this.id = id;
        this.typeSanction = typeSanction;
        this.description = description;
        this.state = state;
        this.sanctionDate = sanctionDate;
        this.endDate = endDate;
        this.amount = amount;
        this.idLoanHall = idLoanHall;
        this.idLoanDevice = idLoanDevice;
    }
    
    public long getId() {
        return id;
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

    public Date getSanctionDate() {
        return sanctionDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getAmount() {
        return amount;
    }

    public long getIdLoanHall() {
        return idLoanHall;
    }

    public long getIdLoanDevice() {
        return idLoanDevice;
    }
}

