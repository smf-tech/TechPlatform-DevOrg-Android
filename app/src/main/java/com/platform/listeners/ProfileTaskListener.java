package com.platform.listeners;

import com.platform.models.profile.JurisdictionLevel;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.models.profile.State;

import java.util.List;

@SuppressWarnings("unused")
public interface ProfileTaskListener extends PlatformTaskListener {

    void showOrganizations(List<Organization> organizations);

    void showOrganizationProjects(List<OrganizationProject> organizationProjects);

    void showOrganizationRoles(List<OrganizationRole> organizationRoles);

    void showStates(List<State> states);

    void showJurisdictionLevel(List<JurisdictionLevel> jurisdictionLevels, int level, String levelName);
}
