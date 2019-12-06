package com.octopus.listeners;


import com.octopus.models.events.Participant;

import java.util.ArrayList;

public interface AddMemberListener extends ProfileTaskListener {

    void showMember(ArrayList<Participant> memberList);

}