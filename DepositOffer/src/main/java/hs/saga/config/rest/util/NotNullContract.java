package hs.saga.config.rest.util;
public class NotNullContract extends AbstractContract<Object> {
    public NotNullContract(String parameterName, Object object) {
        super(parameterName, object);
    }

    public boolean isValid() {
        return this.getObject() != null;
    }

    public String getMessage() {
        return "expected object not null, but was null";
    }
}
