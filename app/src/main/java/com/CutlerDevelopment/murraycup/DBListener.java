package com.CutlerDevelopment.murraycup;

public interface DBListener {

     void teamCreated(Team t);

     void teamModified(Team t);

     void teamRemoved(Team t);
}
