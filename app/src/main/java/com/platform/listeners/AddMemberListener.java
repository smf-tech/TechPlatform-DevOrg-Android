package com.platform.listeners;


import com.platform.models.events.Participant;

import java.util.ArrayList;

public interface AddMemberListener extends ProfileTaskListener {

    void showMember(ArrayList<Participant> memberList);

}