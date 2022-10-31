package hs.saga.config.rest.util;

public abstract class AbstractContract<T> implements Contract<T> {
    private final String parameterName;
    private final T object;

    protected AbstractContract(String parameterName, T object) {
        this.parameterName = parameterName;
        this.object = object;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public T getObject() {
        return this.object;
    }
}

