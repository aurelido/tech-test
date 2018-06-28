package com.aabanegas.techtest.rest.util;

import org.springframework.http.HttpHeaders;

import lombok.extern.apachecommons.CommonsLog;

/**
 * Utility class for HTTP headers creation.
 */
@CommonsLog
public final class HeaderUtil {

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-paymentsApp-alert", message);
        headers.add("X-paymentsApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("A new " + entityName + " is created with identifier " + param, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is updated with identifier " + param, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is deleted with identifier " + param, param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error(String.format("Entity processing failed, %s", defaultMessage));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-paymentsApp-error", defaultMessage);
        headers.add("X-paymentsApp-params", entityName);
        return headers;
    }
}
