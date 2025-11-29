package model;

public class Professor {
    private String id;
    private String name;
    private String department;

    public Professor(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + department + ")";
    }

}
