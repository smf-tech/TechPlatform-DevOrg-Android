package com.platform.listeners;

import com.platform.models.tm.PendingRequest;

import java.util.List;

@SuppressWarnings("unused")
public interface TMTaskListener extends PlatformTaskListener {

    void showPendingRequests(List<PendingRequest> pendingRequestList);
}
