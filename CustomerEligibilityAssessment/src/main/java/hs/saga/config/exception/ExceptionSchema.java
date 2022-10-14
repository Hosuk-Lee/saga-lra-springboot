package hs.saga.config.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionSchema {
    int status;
    String message;
    String path;
}
