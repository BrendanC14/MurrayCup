package com.CutlerDevelopment.murraycup.Models;

import java.util.ArrayList;

public class Team {

    private int ID;
    private String FirestoreReference;
    private String Name;
    private String Captain;
    private String Colour;
    private int Group;
    private ArrayList<Fixture> AllFixtures;

    public Team(int theID, String theName, String theCaptain, String theColour, int theGroup, String fbRef) {
        this.ID = theID;
        this.Name = theName;
        this.Captain = theCaptain;
        this.Colour = theColour;
        this.Group = theGroup;
        this.FirestoreReference = fbRef;
        this.AllFixtures = new ArrayList<>();
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
    public int GetGroup() {
        return this.Group;
    }
    public ArrayList<Fixture> GetAllFixtures() { return AllFixtures; }

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
    public void SetGroup(int newGroup) { this.Group = newGroup; }
    public void AddFixture(Fixture fixture) { AllFixtures.add(fixture); }
}
