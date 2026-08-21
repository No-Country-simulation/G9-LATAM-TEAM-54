package com.energiai.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propiedades de conexión a Oracle Cloud Infrastructure (OCI).
 * Todos los valores se inyectan mediante variables de entorno (ver .env.example).
 * NUNCA hardcodear credenciales aquí.
 */
@Configuration
@ConfigurationProperties(prefix = "oci")
public class OciProperties {

    /** Activa o desactiva la integración con OCI Object Storage. */
    private boolean enabled;

    /** OCID del tenancy de OCI. */
    private String tenancyId;

    /** OCID del usuario/API key de OCI. */
    private String userId;

    /** Fingerprint de la clave API (formato aa:bb:cc:...). */
    private String fingerprint;

    /** Región de OCI, ej: sa-saopaulo-1, us-ashburn-1. */
    private String region;

    /** Ruta al archivo .pem de la clave privada dentro del contenedor/host. */
    private String privateKeyPath;

    /** Passphrase de la clave privada, si está encriptada (opcional). */
    private String passphrase;

    /** Namespace del Object Storage del tenancy. */
    private String namespace;

    /** Nombre del bucket donde está el modelo. */
    private String bucketName;

    /** Nombre del objeto (archivo) del modelo dentro del bucket. */
    private String modelObjectName;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTenancyId() {
        return tenancyId;
    }

    public void setTenancyId(String tenancyId) {
        this.tenancyId = tenancyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getModelObjectName() {
        return modelObjectName;
    }

    public void setModelObjectName(String modelObjectName) {
        this.modelObjectName = modelObjectName;
    }
}
