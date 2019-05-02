package com.platform.listeners;


import com.platform.models.events.Participant;

import java.util.List;

public interface AddMemberListener extends ProfileTaskListener {

    void showMember(List<Participant> memberList);

}