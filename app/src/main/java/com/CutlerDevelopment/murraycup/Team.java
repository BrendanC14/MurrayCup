package com.CutlerDevelopment.murraycup;

public class Team {

    private int ID;
    private String FirestoreReference;
    private String Name;
    private String Captain;
    private String Colour;
    private String Group;

    public Team(int theID, String theName, String theCaptain, String theColour, String fbRef) {
        this.ID = theID;
        this.Name = theName;
        this.Captain = theCaptain;
        this.Colour = theColour;
        this.FirestoreReference = fbRef;
    }

    public int GetID() {return this.ID;}
    public String GetFirestoreReference() {return this.FirestoreReference;}
    public String GetName() {
        return this.Name;
    }
    public String GetCaptain() {
        return this.Captain;
    }
    public String GetColour() {
        return this.Colour;
    }
    public String GetGroup() {
        return this.Group;
    }

    public void ChangeID(int newID) {this.ID = newID;}
    public void ChangeName(String newName) {
        this.Name = newName;
    }
    public void ChangeCaptain(String newCaptain) {
        this.Captain = newCaptain;
    }
    public void ChangeColour(String newColour) {
        this.Colour = newColour;
    }
    public void SetGroup(String newGroup) {
        this.Group = newGroup;
    }
}
