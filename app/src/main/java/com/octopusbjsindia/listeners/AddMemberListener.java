package com.octopusbjsindia.listeners;


import com.octopusbjsindia.models.events.Participant;

import java.util.ArrayList;

public interface AddMemberListener extends ProfileTaskListener {

    void showMember(ArrayList<Participant> memberList);

}