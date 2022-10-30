package hs.saga.config.rest.util;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public final class TextFormatter {
    private TextFormatter() {
    }

    public static String format(String messagePattern, Object... arg) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(messagePattern, arg);
        String result = formattingTuple.getMessage();
        return result;
    }
}
