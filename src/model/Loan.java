

package model;

import java.time.LocalDate;

public class Loan {
    private Long id, idHall, idUser, idEquipment;
    private LocalDate date;
    private String specs, state;

    public Loan(Long id, Long idHall, Long idUser, Long idEquipment, 
    		LocalDate date, String specs, String state) {
        this.id = id;
        this.idHall = idHall;
        this.idUser = idUser;
        this.idEquipment = idEquipment;
        this.date = date;
        this.specs = specs;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public Long getIdHall() {
        return idHall;
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdEquipment() {
        return idEquipment;
    }

    public LocalDate getDate() {
        return date;
    }


    public String getSpecs() {
        return specs;
    }
    
    public String getState() {
    	return state;
    }
}


