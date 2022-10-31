package hs.saga.config.rest.util;

public interface Contract<T> {
    boolean isValid();

    String getMessage();

    T getObject();

    String getParameterName();

    default void check() throws ContractException {
        if (!this.isValid()) {
            throw new ContractException(this.getParameterName(), this.getMessage(), this.getObject());
        }
    }
}
