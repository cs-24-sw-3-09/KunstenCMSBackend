package com.github.cs_24_sw_3_09.CMS.utils;

// Encapsulation of validation result
public class SetTSContentValidationResult {
    private final boolean valid;
    private final String errorMessage;
    private final Long displayContentId;
    private final String displayContentType;

    private SetTSContentValidationResult(boolean valid, String errorMessage, Long displayContentId, String displayContentType) {
        this.valid = valid;
        this.errorMessage = errorMessage;
        this.displayContentId = displayContentId;
        this.displayContentType = displayContentType;
    }

    public static SetTSContentValidationResult valid(Long displayContentId, String displayContentType) {
        return new SetTSContentValidationResult(true, null, displayContentId, displayContentType);
    }

    public static SetTSContentValidationResult invalid(String errorMessage) {
        return new SetTSContentValidationResult(false, errorMessage, null, null);
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getDisplayContentId() {
        return displayContentId;
    }

    public String getDisplayContentType() {
        return displayContentType;
    }
}
