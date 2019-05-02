package com.platform.listeners;

import com.platform.models.events.Member;

import java.util.List;

public interface AddMemberListener extends ProfileTaskListener {

    void showMember(List<Member> memberList);

}