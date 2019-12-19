package com.CutlerDevelopment.murraycup.Interfaces;

import com.CutlerDevelopment.murraycup.Models.Team;

public interface TeamDBListener {

     void teamCreated(Team t);

     void teamModified(Team t);

     void teamRemoved(Team t);
}
