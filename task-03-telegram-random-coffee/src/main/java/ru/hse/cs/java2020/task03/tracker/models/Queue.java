package ru.hse.cs.java2020.task03.tracker.models;


public class Queue {
    private String self;
    private Integer id;
    private String key;
    private Integer version;
    private String name;
    private User lead;
    private Boolean assignAuto;
    private Type defaultType;
    private Priority defaultPriority;
    private Boolean denyVoting;
    private Boolean denyConductorAutolink;
    private Boolean denyTrackerAutolink;
    private Boolean useComponentPermissionsIntersection;
    private Boolean useLastSignature;
    private Boolean allowExternalMailing;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getLead() {
        return lead;
    }

    public void setLead(User lead) {
        this.lead = lead;
    }

    public Boolean getAssignAuto() {
        return assignAuto;
    }

    public void setAssignAuto(Boolean assignAuto) {
        this.assignAuto = assignAuto;
    }

    public Type getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(Type defaultType) {
        this.defaultType = defaultType;
    }

    public Priority getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(Priority defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public Boolean getDenyVoting() {
        return denyVoting;
    }

    public void setDenyVoting(Boolean denyVoting) {
        this.denyVoting = denyVoting;
    }

    public Boolean getDenyConductorAutolink() {
        return denyConductorAutolink;
    }

    public void setDenyConductorAutolink(Boolean denyConductorAutolink) {
        this.denyConductorAutolink = denyConductorAutolink;
    }

    public Boolean getDenyTrackerAutolink() {
        return denyTrackerAutolink;
    }

    public void setDenyTrackerAutolink(Boolean denyTrackerAutolink) {
        this.denyTrackerAutolink = denyTrackerAutolink;
    }

    public Boolean getUseComponentPermissionsIntersection() {
        return useComponentPermissionsIntersection;
    }

    public void setUseComponentPermissionsIntersection(Boolean useComponentPermissionsIntersection) {
        this.useComponentPermissionsIntersection = useComponentPermissionsIntersection;
    }

    public Boolean getUseLastSignature() {
        return useLastSignature;
    }

    public void setUseLastSignature(Boolean useLastSignature) {
        this.useLastSignature = useLastSignature;
    }

    public Boolean getAllowExternalMailing() {
        return allowExternalMailing;
    }

    public void setAllowExternalMailing(Boolean allowExternalMailing) {
        this.allowExternalMailing = allowExternalMailing;
    }
}
