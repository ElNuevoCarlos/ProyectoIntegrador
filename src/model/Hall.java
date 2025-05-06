package model;

public class Hall {
    private long id;
    private String name, location, ability, state;

    public Hall(long id, String name, String location, String ability, String state) {
        super();
        this.id = id;
        this.name = name;
        this.location = location;
        this.ability = ability;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getAbility() {
        return ability;
    }

    public String getState() {
        return state;
    }
}

