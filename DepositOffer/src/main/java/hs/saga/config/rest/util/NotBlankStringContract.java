package hs.saga.config.rest.util;

import org.apache.commons.lang3.StringUtils;

public class NotBlankStringContract extends AbstractContract<String> {
    public NotBlankStringContract(String parameterName, String object) {
        super(parameterName, object);
    }

    public boolean isValid() {
        return StringUtils.isNotBlank((CharSequence)this.getObject());
    }

    public String getMessage() {
        return "string cannot be empty, blank or null";
    }
}