package com.energiai.backend.service;

import com.energiai.backend.config.OciProperties;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Encapsula la autenticación y descarga de objetos (por ejemplo, el modelo .onnx)
 * desde un bucket de OCI Object Storage usando autenticación por API Key (user principal).
 */
@Service
public class OciObjectStorageService {

    private static final Logger logger = LoggerFactory.getLogger(OciObjectStorageService.class);

    private final OciProperties ociProperties;
    private ObjectStorageClient objectStorageClient;

    public OciObjectStorageService(OciProperties ociProperties) {
        this.ociProperties = ociProperties;
    }

    @PostConstruct
    public void init() {
        if (!ociProperties.isEnabled()) {
            logger.info("Integración con OCI Object Storage deshabilitada (oci.enabled=false).");
            return;
        }

        try {
            validateConfig();

            Path keyPath = Path.of(ociProperties.getPrivateKeyPath());
            if (!Files.exists(keyPath)) {
                throw new IllegalStateException(
                        "No se encontró la clave privada OCI en: " + keyPath.toAbsolutePath());
            }

            AuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                    .tenantId(ociProperties.getTenancyId())
                    .userId(ociProperties.getUserId())
                    .fingerprint(ociProperties.getFingerprint())
                    .privateKeySupplier(new SimplePrivateKeySupplier(keyPath.toAbsolutePath().toString()))
                    .passPhrase(ociProperties.getPassphrase())
                    .region(Region.fromRegionCodeOrId(ociProperties.getRegion()))
                    .build();

            this.objectStorageClient = ObjectStorageClient.builder()
                    .region(Region.fromRegionCodeOrId(ociProperties.getRegion()))
                    .build(provider);

            logger.info("Cliente de OCI Object Storage inicializado correctamente (bucket: {}).",
                    ociProperties.getBucketName());
        } catch (Exception e) {
            logger.error("No se pudo inicializar el cliente de OCI Object Storage: {}", e.getMessage(), e);
            this.objectStorageClient = null;
        }
    }

    private void validateConfig() {
        require(ociProperties.getTenancyId(), "oci.tenancy-id");
        require(ociProperties.getUserId(), "oci.user-id");
        require(ociProperties.getFingerprint(), "oci.fingerprint");
        require(ociProperties.getRegion(), "oci.region");
        require(ociProperties.getPrivateKeyPath(), "oci.private-key-path");
        require(ociProperties.getNamespace(), "oci.namespace");
        require(ociProperties.getBucketName(), "oci.bucket-name");
    }

    private void require(String value, String propertyName) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta configurar la propiedad requerida: " + propertyName);
        }
    }

    public boolean isAvailable() {
        return objectStorageClient != null;
    }

    /**
     * Descarga un objeto del bucket configurado y devuelve sus bytes.
     * Usar preferentemente para archivos pequeños/medianos (ej. el modelo ONNX).
     */
    public byte[] downloadObject(String objectName) {
        if (!isAvailable()) {
            throw new IllegalStateException("El cliente de OCI Object Storage no está disponible.");
        }

        GetObjectRequest request = GetObjectRequest.builder()
                .namespaceName(ociProperties.getNamespace())
                .bucketName(ociProperties.getBucketName())
                .objectName(objectName)
                .build();

        GetObjectResponse response = objectStorageClient.getObject(request);
        try (InputStream inputStream = response.getInputStream()) {
            byte[] data = inputStream.readAllBytes();
            logger.info("Objeto '{}' descargado desde el bucket '{}' ({} bytes).",
                    objectName, ociProperties.getBucketName(), data.length);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al descargar el objeto '" + objectName + "' desde OCI Object Storage: " + e.getMessage(), e);
        }
    }

    /** Descarga el objeto del modelo configurado en oci.model-object-name. */
    public byte[] downloadModel() {
        return downloadObject(ociProperties.getModelObjectName());
    }

    @PreDestroy
    public void close() {
        if (objectStorageClient != null) {
            objectStorageClient.close();
            logger.info("Cliente de OCI Object Storage cerrado correctamente.");
        }
    }
}
