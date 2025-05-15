package model;

import java.sql.Date;

public class Loan {
    private Long id, idHall, idUser, idEquipment;
    private Date startTime, endTime;
    private String specs, type, state;

    public Loan(Long id, Long idHall, Long idUser, Long idEquipment, 
                Date startTime, Date endTime, String specs, String type, String state) {
        this.id = id;
        this.idHall = idHall;
        this.idUser = idUser;
        this.idEquipment = idEquipment;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specs = specs;
        this.type = type;
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

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getSpecs() {
        return specs;
    }

    public String getType() {
        return type;
    }
    
    public String getState() {
    	return state;
    }
}


