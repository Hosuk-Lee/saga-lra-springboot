package hs.saga.config.rest.util;

public class ContractException extends RuntimeException {
    private static final long serialVersionUID = -5927602117773404783L;
    private final String parameterName;
    private final String contractMessage;
    private final Object instance;

    public ContractException(String parameterName, String contractMessage, Object instance) {
        super(TextFormatter.format("Invalid parameter [parameterName={}, contractMessage={}]", new Object[]{parameterName, contractMessage}));
        this.parameterName = parameterName;
        this.contractMessage = contractMessage;
        this.instance = instance;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public String getContractMessage() {
        return this.contractMessage;
    }

    public Object getInstance() {
        return this.instance;
    }
}

