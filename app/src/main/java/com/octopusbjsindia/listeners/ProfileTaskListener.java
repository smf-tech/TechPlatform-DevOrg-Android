package com.octopusbjsindia.listeners;

import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.Organization;
import com.octopusbjsindia.models.profile.OrganizationProject;
import com.octopusbjsindia.models.profile.OrganizationRole;

import java.util.List;

@SuppressWarnings("unused")
public interface ProfileTaskListener extends PlatformTaskListener {

    void showOrganizations(List<Organization> organizations);

    void showOrganizationProjects(List<OrganizationProject> organizationProjects);

    void showOrganizationRoles(List<OrganizationRole> organizationRoles);

    void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName);
}
