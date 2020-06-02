package ru.hse.cs.java2020.task03.tracker.models;


import java.util.Objects;

public class Myself {
    private String self;
    private Integer uid;
    private String login;
    private String firstName;
    private String lastName;
    private String display;
    private String email;
    private Boolean external;
    private Boolean hasLicense;
    private Boolean dismissed;
    private Boolean useNewFilters;
    private Boolean disableNotifications;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public Boolean getHasLicense() {
        return hasLicense;
    }

    public void setHasLicense(Boolean hasLicense) {
        this.hasLicense = hasLicense;
    }

    public Boolean getDismissed() {
        return dismissed;
    }

    public void setDismissed(Boolean dismissed) {
        this.dismissed = dismissed;
    }

    public Boolean getUseNewFilters() {
        return useNewFilters;
    }

    public void setUseNewFilters(Boolean useNewFilters) {
        this.useNewFilters = useNewFilters;
    }

    public Boolean getDisableNotifications() {
        return disableNotifications;
    }

    public void setDisableNotifications(Boolean disableNotifications) {
        this.disableNotifications = disableNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Myself myself = (Myself) o;
        return Objects.equals(self, myself.self)
                && Objects.equals(uid, myself.uid)
                && Objects.equals(login, myself.login)
                && Objects.equals(firstName, myself.firstName)
                && Objects.equals(lastName, myself.lastName)
                && Objects.equals(display, myself.display)
                && Objects.equals(email, myself.email)
                && Objects.equals(external, myself.external)
                && Objects.equals(hasLicense, myself.hasLicense)
                && Objects.equals(dismissed, myself.dismissed)
                && Objects.equals(useNewFilters, myself.useNewFilters)
                && Objects.equals(disableNotifications, myself.disableNotifications);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(self, uid, login, firstName, lastName, display, email, external, hasLicense, dismissed, useNewFilters,
                        disableNotifications);
    }
}
