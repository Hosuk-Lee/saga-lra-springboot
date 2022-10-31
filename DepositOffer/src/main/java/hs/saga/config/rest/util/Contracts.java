package hs.saga.config.rest.util;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;

public final class Contracts {
    private Contracts() {
    }

    public static void notNull(Object parameter, String name) throws ContractException {
        (new NotNullContract(name, parameter)).check();
    }

    public static void notBlank(String parameter, String name) throws ContractException {
        (new NotBlankStringContract(name, parameter)).check();
    }

    public static void notEmpty(Collection<?> parameter, String name) throws ContractException {
        (new NotEmptyCollectionContract(name, parameter)).check();
    }

    public static void conditional(boolean condition, Runnable contract) {
        if (condition) {
            contract.run();
        }

    }

    public static void conditional(boolean condition, Contract<?> contract) {
        if (condition) {
            custom(contract);
        }

    }

    public static void custom(Contract<?> vc) throws ContractException {
        vc.check();
    }
}