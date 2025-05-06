package model;

import java.sql.Date;

public class Loan {
    private long id, idHall, idUser, idEquipment;
    private Date startTime, endTime;
    private String software, type;

    public Loan(long id, long idHall, long idUser, long idEquipment, 
                Date startTime, Date endTime, String software, String type) {
        super();
        this.id = id;
        this.idHall = idHall;
        this.idUser = idUser;
        this.idEquipment = idEquipment;
        this.startTime = startTime;
        this.endTime = endTime;
        this.software = software;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public long getIdHall() {
        return idHall;
    }

    public long getIdUser() {
        return idUser;
    }

    public long getIdEquipment() {
        return idEquipment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getSoftware() {
        return software;
    }

    public String getType() {
        return type;
    }
}

