package com.platform.listeners;

import com.platform.models.profile.Location;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;

import java.util.List;

@SuppressWarnings("unused")
public interface ProfileTaskListener extends PlatformTaskListener {

    void showOrganizations(List<Organization> organizations);

    void showOrganizationProjects(List<OrganizationProject> organizationProjects);

    void showOrganizationRoles(List<OrganizationRole> organizationRoles);

    void showJurisdictionLevel(List<Location> jurisdictionLevels, String levelName);
}
