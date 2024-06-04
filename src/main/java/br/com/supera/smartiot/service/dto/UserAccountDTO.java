package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.CNHSituation;
import br.com.supera.smartiot.domain.enumeration.RegisterSituation;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.UserAccount} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountName;

    @NotNull
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    private String emailAddress;

    @NotNull
    private LocalDate admissionDate;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private Boolean isActive;

    private String mobilePhone;

    private Integer rpushFeedbackId;

    private Boolean execCommands;

    private Boolean isBlocked;

    private String employerName;

    private Integer pushConfiguration;

    private Float traveledDistance;

    private String language;

    private String blockedReason;

    private Long blockedById;

    private Instant blockedAt;

    private String deletedReason;

    private Instant deletedAt;

    private Long deletedById;

    private String lastModifiedBy;

    private String registrationCode;

    @NotNull
    @Size(min = 8)
    private String password;

    private String passwordHint;

    private String featureFlags;

    private String zipCode;

    private String publicPlace;

    private String addressNumber;

    private String streetName;

    private String addressComplement;

    private String cityName;

    private String stateName;

    @Lob
    private String cnhImage;

    @Lob
    private String profileImage;

    private LocalDate cnhExpirationDate;

    private CNHSituation cnhStatus;

    private RegisterSituation registrationStatus;

    private String analyzedBy;

    private Instant analyzedAt;

    @Lob
    private String signatureImage;

    @Lob
    private String residenceProofImage;

    private ApplicationUserDTO applicationUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getRpushFeedbackId() {
        return rpushFeedbackId;
    }

    public void setRpushFeedbackId(Integer rpushFeedbackId) {
        this.rpushFeedbackId = rpushFeedbackId;
    }

    public Boolean getExecCommands() {
        return execCommands;
    }

    public void setExecCommands(Boolean execCommands) {
        this.execCommands = execCommands;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public Integer getPushConfiguration() {
        return pushConfiguration;
    }

    public void setPushConfiguration(Integer pushConfiguration) {
        this.pushConfiguration = pushConfiguration;
    }

    public Float getTraveledDistance() {
        return traveledDistance;
    }

    public void setTraveledDistance(Float traveledDistance) {
        this.traveledDistance = traveledDistance;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBlockedReason() {
        return blockedReason;
    }

    public void setBlockedReason(String blockedReason) {
        this.blockedReason = blockedReason;
    }

    public Long getBlockedById() {
        return blockedById;
    }

    public void setBlockedById(Long blockedById) {
        this.blockedById = blockedById;
    }

    public Instant getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Instant blockedAt) {
        this.blockedAt = blockedAt;
    }

    public String getDeletedReason() {
        return deletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getDeletedById() {
        return deletedById;
    }

    public void setDeletedById(Long deletedById) {
        this.deletedById = deletedById;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getFeatureFlags() {
        return featureFlags;
    }

    public void setFeatureFlags(String featureFlags) {
        this.featureFlags = featureFlags;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPublicPlace() {
        return publicPlace;
    }

    public void setPublicPlace(String publicPlace) {
        this.publicPlace = publicPlace;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAddressComplement() {
        return addressComplement;
    }

    public void setAddressComplement(String addressComplement) {
        this.addressComplement = addressComplement;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCnhImage() {
        return cnhImage;
    }

    public void setCnhImage(String cnhImage) {
        this.cnhImage = cnhImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDate getCnhExpirationDate() {
        return cnhExpirationDate;
    }

    public void setCnhExpirationDate(LocalDate cnhExpirationDate) {
        this.cnhExpirationDate = cnhExpirationDate;
    }

    public CNHSituation getCnhStatus() {
        return cnhStatus;
    }

    public void setCnhStatus(CNHSituation cnhStatus) {
        this.cnhStatus = cnhStatus;
    }

    public RegisterSituation getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegisterSituation registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getAnalyzedBy() {
        return analyzedBy;
    }

    public void setAnalyzedBy(String analyzedBy) {
        this.analyzedBy = analyzedBy;
    }

    public Instant getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(Instant analyzedAt) {
        this.analyzedAt = analyzedAt;
    }

    public String getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }

    public String getResidenceProofImage() {
        return residenceProofImage;
    }

    public void setResidenceProofImage(String residenceProofImage) {
        this.residenceProofImage = residenceProofImage;
    }

    public ApplicationUserDTO getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUserDTO applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccountDTO)) {
            return false;
        }

        UserAccountDTO userAccountDTO = (UserAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccountDTO{" +
            "id=" + getId() +
            ", accountName='" + getAccountName() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", rpushFeedbackId=" + getRpushFeedbackId() +
            ", execCommands='" + getExecCommands() + "'" +
            ", isBlocked='" + getIsBlocked() + "'" +
            ", employerName='" + getEmployerName() + "'" +
            ", pushConfiguration=" + getPushConfiguration() +
            ", traveledDistance=" + getTraveledDistance() +
            ", language='" + getLanguage() + "'" +
            ", blockedReason='" + getBlockedReason() + "'" +
            ", blockedById=" + getBlockedById() +
            ", blockedAt='" + getBlockedAt() + "'" +
            ", deletedReason='" + getDeletedReason() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", deletedById=" + getDeletedById() +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", registrationCode='" + getRegistrationCode() + "'" +
            ", password='" + getPassword() + "'" +
            ", passwordHint='" + getPasswordHint() + "'" +
            ", featureFlags='" + getFeatureFlags() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", publicPlace='" + getPublicPlace() + "'" +
            ", addressNumber='" + getAddressNumber() + "'" +
            ", streetName='" + getStreetName() + "'" +
            ", addressComplement='" + getAddressComplement() + "'" +
            ", cityName='" + getCityName() + "'" +
            ", stateName='" + getStateName() + "'" +
            ", cnhImage='" + getCnhImage() + "'" +
            ", profileImage='" + getProfileImage() + "'" +
            ", cnhExpirationDate='" + getCnhExpirationDate() + "'" +
            ", cnhStatus='" + getCnhStatus() + "'" +
            ", registrationStatus='" + getRegistrationStatus() + "'" +
            ", analyzedBy='" + getAnalyzedBy() + "'" +
            ", analyzedAt='" + getAnalyzedAt() + "'" +
            ", signatureImage='" + getSignatureImage() + "'" +
            ", residenceProofImage='" + getResidenceProofImage() + "'" +
            ", applicationUser=" + getApplicationUser() +
            "}";
    }
}
