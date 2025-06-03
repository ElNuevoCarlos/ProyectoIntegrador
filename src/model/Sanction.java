package model;

import java.time.LocalDate;

public class Sanction {
    private Long id, idLoanDevice;
    private String typeSanction, description, state;
    private LocalDate endDate;
    private int amount;
    
    public Sanction(Long id, String typeSanction, String description, LocalDate endDate, 
    		int amount, String state, long idLoanDevice) {
        super();
        this.id = id;
        this.typeSanction = typeSanction;
        this.description = description;
        this.endDate = endDate;
        this.amount = amount;
        this.state = state;
        this.idLoanDevice = idLoanDevice;
    }
    
    public void setId(Long id) {
		this.id = id;
	}

	public void setIdLoanDevice(Long idLoanDevice) {
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

	public void setEndDate(LocalDate endDate) {
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getAmount() {
        return amount;
    }

    public long getIdLoanDevice() {
        return idLoanDevice;
    }
}

