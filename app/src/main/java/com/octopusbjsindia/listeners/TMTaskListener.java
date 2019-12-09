package com.octopusbjsindia.listeners;

import com.octopusbjsindia.models.tm.PendingRequest;

import java.util.List;

@SuppressWarnings("unused")
public interface TMTaskListener extends PlatformTaskListener {

    void showPendingRequests(List<PendingRequest> pendingRequestList);

    void updateRequestStatus(String response, PendingRequest pendingRequest);
}
