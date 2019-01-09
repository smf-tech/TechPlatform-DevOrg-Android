package com.platform.models.home;

@SuppressWarnings("unused")
public class HomeModel {

    private String moduleId;
    private String moduleName;
    private Integer moduleIcon;
    private Class<?> destination;
    private Boolean isAccessible;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getModuleIcon() {
        return moduleIcon;
    }

    public void setModuleIcon(Integer moduleIcon) {
        this.moduleIcon = moduleIcon;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public Boolean getAccessible() {
        return isAccessible;
    }

    public void setAccessible(Boolean accessible) {
        isAccessible = accessible;
    }
}
