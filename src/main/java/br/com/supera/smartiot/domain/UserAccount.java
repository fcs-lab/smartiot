package br.com.supera.smartiot.domain;

import br.com.supera.smartiot.domain.enumeration.CNHSituation;
import br.com.supera.smartiot.domain.enumeration.RegisterSituation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @NotNull
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @NotNull
    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "rpush_feedback_id")
    private Integer rpushFeedbackId;

    @Column(name = "exec_commands")
    private Boolean execCommands;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "employer_name")
    private String employerName;

    @Column(name = "push_configuration")
    private Integer pushConfiguration;

    @Column(name = "traveled_distance")
    private Float traveledDistance;

    @Column(name = "language")
    private String language;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @Column(name = "blocked_by_id")
    private Long blockedById;

    @Column(name = "blocked_at")
    private Instant blockedAt;

    @Column(name = "deleted_reason")
    private String deletedReason;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by_id")
    private Long deletedById;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "registration_code")
    private String registrationCode;

    @NotNull
    @Size(min = 8)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_hint")
    private String passwordHint;

    @Column(name = "feature_flags")
    private String featureFlags;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "public_place")
    private String publicPlace;

    @Column(name = "address_number")
    private String addressNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "address_complement")
    private String addressComplement;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "state_name")
    private String stateName;

    @Lob
    @Column(name = "cnh_image")
    private String cnhImage;

    @Lob
    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "cnh_expiration_date")
    private LocalDate cnhExpirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "cnh_status")
    private CNHSituation cnhStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status")
    private RegisterSituation registrationStatus;

    @Column(name = "analyzed_by")
    private String analyzedBy;

    @Column(name = "analyzed_at")
    private Instant analyzedAt;

    @Lob
    @Column(name = "signature_image")
    private String signatureImage;

    @Lob
    @Column(name = "residence_proof_image")
    private String residenceProofImage;

    @JsonIgnoreProperties(value = { "user", "userAccount", "contracts" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public UserAccount accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public UserAccount emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDate getAdmissionDate() {
        return this.admissionDate;
    }

    public UserAccount admissionDate(LocalDate admissionDate) {
        this.setAdmissionDate(admissionDate);
        return this;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserAccount createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserAccount updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public UserAccount isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public UserAccount mobilePhone(String mobilePhone) {
        this.setMobilePhone(mobilePhone);
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getRpushFeedbackId() {
        return this.rpushFeedbackId;
    }

    public UserAccount rpushFeedbackId(Integer rpushFeedbackId) {
        this.setRpushFeedbackId(rpushFeedbackId);
        return this;
    }

    public void setRpushFeedbackId(Integer rpushFeedbackId) {
        this.rpushFeedbackId = rpushFeedbackId;
    }

    public Boolean getExecCommands() {
        return this.execCommands;
    }

    public UserAccount execCommands(Boolean execCommands) {
        this.setExecCommands(execCommands);
        return this;
    }

    public void setExecCommands(Boolean execCommands) {
        this.execCommands = execCommands;
    }

    public Boolean getIsBlocked() {
        return this.isBlocked;
    }

    public UserAccount isBlocked(Boolean isBlocked) {
        this.setIsBlocked(isBlocked);
        return this;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getEmployerName() {
        return this.employerName;
    }

    public UserAccount employerName(String employerName) {
        this.setEmployerName(employerName);
        return this;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public Integer getPushConfiguration() {
        return this.pushConfiguration;
    }

    public UserAccount pushConfiguration(Integer pushConfiguration) {
        this.setPushConfiguration(pushConfiguration);
        return this;
    }

    public void setPushConfiguration(Integer pushConfiguration) {
        this.pushConfiguration = pushConfiguration;
    }

    public Float getTraveledDistance() {
        return this.traveledDistance;
    }

    public UserAccount traveledDistance(Float traveledDistance) {
        this.setTraveledDistance(traveledDistance);
        return this;
    }

    public void setTraveledDistance(Float traveledDistance) {
        this.traveledDistance = traveledDistance;
    }

    public String getLanguage() {
        return this.language;
    }

    public UserAccount language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBlockedReason() {
        return this.blockedReason;
    }

    public UserAccount blockedReason(String blockedReason) {
        this.setBlockedReason(blockedReason);
        return this;
    }

    public void setBlockedReason(String blockedReason) {
        this.blockedReason = blockedReason;
    }

    public Long getBlockedById() {
        return this.blockedById;
    }

    public UserAccount blockedById(Long blockedById) {
        this.setBlockedById(blockedById);
        return this;
    }

    public void setBlockedById(Long blockedById) {
        this.blockedById = blockedById;
    }

    public Instant getBlockedAt() {
        return this.blockedAt;
    }

    public UserAccount blockedAt(Instant blockedAt) {
        this.setBlockedAt(blockedAt);
        return this;
    }

    public void setBlockedAt(Instant blockedAt) {
        this.blockedAt = blockedAt;
    }

    public String getDeletedReason() {
        return this.deletedReason;
    }

    public UserAccount deletedReason(String deletedReason) {
        this.setDeletedReason(deletedReason);
        return this;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public UserAccount deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getDeletedById() {
        return this.deletedById;
    }

    public UserAccount deletedById(Long deletedById) {
        this.setDeletedById(deletedById);
        return this;
    }

    public void setDeletedById(Long deletedById) {
        this.deletedById = deletedById;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserAccount lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getRegistrationCode() {
        return this.registrationCode;
    }

    public UserAccount registrationCode(String registrationCode) {
        this.setRegistrationCode(registrationCode);
        return this;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getPassword() {
        return this.password;
    }

    public UserAccount password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return this.passwordHint;
    }

    public UserAccount passwordHint(String passwordHint) {
        this.setPasswordHint(passwordHint);
        return this;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getFeatureFlags() {
        return this.featureFlags;
    }

    public UserAccount featureFlags(String featureFlags) {
        this.setFeatureFlags(featureFlags);
        return this;
    }

    public void setFeatureFlags(String featureFlags) {
        this.featureFlags = featureFlags;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public UserAccount zipCode(String zipCode) {
        this.setZipCode(zipCode);
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPublicPlace() {
        return this.publicPlace;
    }

    public UserAccount publicPlace(String publicPlace) {
        this.setPublicPlace(publicPlace);
        return this;
    }

    public void setPublicPlace(String publicPlace) {
        this.publicPlace = publicPlace;
    }

    public String getAddressNumber() {
        return this.addressNumber;
    }

    public UserAccount addressNumber(String addressNumber) {
        this.setAddressNumber(addressNumber);
        return this;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public UserAccount streetName(String streetName) {
        this.setStreetName(streetName);
        return this;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAddressComplement() {
        return this.addressComplement;
    }

    public UserAccount addressComplement(String addressComplement) {
        this.setAddressComplement(addressComplement);
        return this;
    }

    public void setAddressComplement(String addressComplement) {
        this.addressComplement = addressComplement;
    }

    public String getCityName() {
        return this.cityName;
    }

    public UserAccount cityName(String cityName) {
        this.setCityName(cityName);
        return this;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return this.stateName;
    }

    public UserAccount stateName(String stateName) {
        this.setStateName(stateName);
        return this;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCnhImage() {
        return this.cnhImage;
    }

    public UserAccount cnhImage(String cnhImage) {
        this.setCnhImage(cnhImage);
        return this;
    }

    public void setCnhImage(String cnhImage) {
        this.cnhImage = cnhImage;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public UserAccount profileImage(String profileImage) {
        this.setProfileImage(profileImage);
        return this;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDate getCnhExpirationDate() {
        return this.cnhExpirationDate;
    }

    public UserAccount cnhExpirationDate(LocalDate cnhExpirationDate) {
        this.setCnhExpirationDate(cnhExpirationDate);
        return this;
    }

    public void setCnhExpirationDate(LocalDate cnhExpirationDate) {
        this.cnhExpirationDate = cnhExpirationDate;
    }

    public CNHSituation getCnhStatus() {
        return this.cnhStatus;
    }

    public UserAccount cnhStatus(CNHSituation cnhStatus) {
        this.setCnhStatus(cnhStatus);
        return this;
    }

    public void setCnhStatus(CNHSituation cnhStatus) {
        this.cnhStatus = cnhStatus;
    }

    public RegisterSituation getRegistrationStatus() {
        return this.registrationStatus;
    }

    public UserAccount registrationStatus(RegisterSituation registrationStatus) {
        this.setRegistrationStatus(registrationStatus);
        return this;
    }

    public void setRegistrationStatus(RegisterSituation registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getAnalyzedBy() {
        return this.analyzedBy;
    }

    public UserAccount analyzedBy(String analyzedBy) {
        this.setAnalyzedBy(analyzedBy);
        return this;
    }

    public void setAnalyzedBy(String analyzedBy) {
        this.analyzedBy = analyzedBy;
    }

    public Instant getAnalyzedAt() {
        return this.analyzedAt;
    }

    public UserAccount analyzedAt(Instant analyzedAt) {
        this.setAnalyzedAt(analyzedAt);
        return this;
    }

    public void setAnalyzedAt(Instant analyzedAt) {
        this.analyzedAt = analyzedAt;
    }

    public String getSignatureImage() {
        return this.signatureImage;
    }

    public UserAccount signatureImage(String signatureImage) {
        this.setSignatureImage(signatureImage);
        return this;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }

    public String getResidenceProofImage() {
        return this.residenceProofImage;
    }

    public UserAccount residenceProofImage(String residenceProofImage) {
        this.setResidenceProofImage(residenceProofImage);
        return this;
    }

    public void setResidenceProofImage(String residenceProofImage) {
        this.residenceProofImage = residenceProofImage;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public UserAccount applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
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
            "}";
    }
}
