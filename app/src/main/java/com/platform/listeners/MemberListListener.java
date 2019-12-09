package com.platform.listeners;

import com.android.volley.VolleyError;

public interface MemberListListener {
    void onMembersDeleted(String response);

    void onTaskMembersFetched(String response);

    void onMemberListUpdated(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
