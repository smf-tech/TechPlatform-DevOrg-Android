package com.octopus.listeners;

import com.octopus.models.profile.JurisdictionLocation;
import com.octopus.models.profile.Organization;
import com.octopus.models.profile.OrganizationProject;
import com.octopus.models.profile.OrganizationRole;

import java.util.List;

@SuppressWarnings("unused")
public interface ProfileTaskListener extends PlatformTaskListener {

    void showOrganizations(List<Organization> organizations);

    void showOrganizationProjects(List<OrganizationProject> organizationProjects);

    void showOrganizationRoles(List<OrganizationRole> organizationRoles);

    void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName);
}
