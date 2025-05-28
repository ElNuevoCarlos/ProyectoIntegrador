package model;

import java.sql.Date;

public class Sanction {
    private long id, idLoanDevice;
    private String typeSanction, description, state;
    private Date sanctionDate, endDate;
    private int amount;
    
    public Sanction(long id, String typeSanction, String description, Date sanctionDate, Date endDate, 
    		int amount, String state, long idLoanDevice) {
        super();
        this.id = id;
        this.typeSanction = typeSanction;
        this.description = description;
        this.sanctionDate = sanctionDate;
        this.endDate = endDate;
        this.amount = amount;
        this.state = state;
        this.idLoanDevice = idLoanDevice;
    }
    
    public void setId(long id) {
		this.id = id;
	}

	public void setIdLoanDevice(long idLoanDevice) {
		this.idLoanDevice = idLoanDevice;
	}

	public void setTypeSanction(String typeSanction) {
		this.typeSanction = typeSanction;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setSanctionDate(Date sanctionDate) {
		this.sanctionDate = sanctionDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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

    public long getIdLoanDevice() {
        return idLoanDevice;
    }
}

