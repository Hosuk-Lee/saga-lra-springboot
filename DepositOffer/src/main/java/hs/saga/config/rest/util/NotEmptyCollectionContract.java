package hs.saga.config.rest.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;


public class NotEmptyCollectionContract extends AbstractContract<Collection<?>> {
    public NotEmptyCollectionContract(String parameterName, Collection<?> object) {
        super(parameterName, object);
    }

    public boolean isValid() {
        return CollectionUtils.isNotEmpty((Collection)this.getObject());
    }

    public String getMessage() {
        return "collection " + this.getParameterName() + " cannot be empty or null";
    }
}

