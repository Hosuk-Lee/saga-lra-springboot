package hs.saga.config.rest.util;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.Arrays;
import java.util.Base64;

public final class ByteUtil {
    public static final Charset[] TRY_ENC_UTF8_ISO88591_UTF16LE_UTF16BE;
    public static final Charset[] TRY_ENC_UTF16LE_UTF16BE_UTF8_ISO88591;
    private static final String BASE64_PREFIX = "base64:";
    private static final byte FIRST_NON_BINARY = -119;
    private static final Charset DEFAULT_CHARSET;
    private static Logger log;

    private ByteUtil() {
    }

    public static String transform(byte[] bytes) {
        return transform(bytes, TRY_ENC_UTF8_ISO88591_UTF16LE_UTF16BE);
    }

    public static String transform(byte[] bytes, Charset... charsets) {
        Contracts.notNull(charsets, "charsets");
        String result;
        if (ArrayUtils.isEmpty(bytes)) {
            result = "";
        } else {
            try {
                result = decode(bytes, charsets);
            } catch (DecodingFailureException var4) {
                log.trace("Decoding failed. Assume it's binary data. Fallback to Base64 encoding [charsets={}]", charsets, var4);
                result = "base64:" + encodeToString(bytes);
            }
        }

        return result;
    }

    private static String encodeToString(byte[] src) {
        return src.length == 0 ? "" : new String(Base64.getEncoder().encode(src), DEFAULT_CHARSET);
    }

    public static byte[] limit(byte[] bytes, int limit) {
        byte[] result;
        if (!ArrayUtils.isEmpty(bytes) && limit >= 0 && ArrayUtils.getLength(bytes) > limit) {
            result = new byte[limit + 3];
            System.arraycopy(bytes, 0, result, 0, limit);
            result[limit] = 46;
            result[limit + 1] = 46;
            result[limit + 2] = 46;
        } else {
            result = bytes;
        }

        return result;
    }

    public static boolean isBinary(byte[] bytes) {
        return ArrayUtils.isEmpty(bytes) ? false : isBinary(bytes, bytes.length);
    }

    public static boolean isBinary(byte[] bytes, int toIndex) {
        return isBinary(bytes, 0, toIndex);
    }

    public static boolean isBinary(byte[] bytes, int fromIndex, int toIndex) {
        Contracts.notNull(bytes, "bytes");

        for(int i = fromIndex; i < toIndex; ++i) {
            byte b = bytes[i];
            if (b < -119) {
                return true;
            }
        }

        return false;
    }

    public static String decode(byte[] bytes, Charset... charsets) {
        Contracts.notNull(bytes, "bytes");
        Contracts.notNull(charsets, "charsets");
        Charset[] var2 = charsets;
        int var3 = charsets.length;
        int var4 = 0;

        while(var4 < var3) {
            Charset charset = var2[var4];

            try {
                return decodeInternal(bytes, charset);
            } catch (CharacterCodingException var7) {
                log.trace("Decoding failed [charset={}, charsets={}]", new Object[]{charset, charsets, var7});
                ++var4;
            }
        }

        throw new DecodingFailureException(TextFormatter.format("Decoding failed [charsets={}]", new Object[]{Arrays.toString(charsets)}));
    }

    private static String decodeInternal(byte[] chars, Charset charset) throws CharacterCodingException {
        CharsetDecoder decoder = charset.newDecoder().onMalformedInput(CodingErrorAction.REPORT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(chars);
        CharBuffer decoded = decoder.decode(byteBuffer);
        return decoded.toString();
    }

    static {
        TRY_ENC_UTF8_ISO88591_UTF16LE_UTF16BE = new Charset[]{StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_16LE, StandardCharsets.UTF_16BE};
        TRY_ENC_UTF16LE_UTF16BE_UTF8_ISO88591 = new Charset[]{StandardCharsets.UTF_16LE, StandardCharsets.UTF_16BE, StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1};
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
        log = LoggerFactory.getLogger(ByteUtil.class);
    }
}