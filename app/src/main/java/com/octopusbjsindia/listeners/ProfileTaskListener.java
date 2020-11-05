package com.octopusbjsindia.listeners;

import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.profile.Organization;
import com.octopusbjsindia.models.profile.OrganizationProject;
import com.octopusbjsindia.models.profile.OrganizationRole;

import java.util.List;

@SuppressWarnings("unused")
public interface ProfileTaskListener extends PlatformTaskListener {

    void showOrganizations(List<Organization> organizations);

    void showOrganizationProjects(List<OrganizationProject> organizationProjects);

    void showOrganizationRoles(List<OrganizationRole> organizationRoles);

    void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName);
}
