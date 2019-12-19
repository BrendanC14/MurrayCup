package com.CutlerDevelopment.murraycup.Models;

public class Team {

    private int id;
    private String name;
    private String captain;
    private String colour;
    private String firestoreReference;
    private String group;

    public Team(int theID, String theName, String theCaptain, String theColour, String fbRef) {
        this.id = theID;
        this.name = theName;
        this.captain = theCaptain;
        this.colour = theColour;
        this.firestoreReference = fbRef;
    }

    public int getID() {
        return id;
    }

    public String getFirestoreReference() {
        return firestoreReference;
    }

    public String getName() {
        return name;
    }

    public String getCaptain() {
        return captain;
    }

    public String getColour() {
        return colour;
    }

    public String getGroup() {
        return group;
    }

    public void changeID(int newID) {
        id = newID;
    }

    public void changeName(String newName) {
        name = newName;
    }

    public void changeCaptain(String newCaptain) {
        captain = newCaptain;
    }

    public void changeColour(String newColour) {
        colour = newColour;
    }

    public void setGroup(String newGroup) {
        group = newGroup;
    }
}
