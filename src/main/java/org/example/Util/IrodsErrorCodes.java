package org.example.Util;

import org.example.IrodsException;
import org.example.Mapper.Mapped;

import java.util.HashMap;
import java.util.Map;

public class IrodsErrorCodes {
    public static final Map<Integer, String> STATUS_CODES;

    static {
        STATUS_CODES = new HashMap<>();
        STATUS_CODES.put(-170000, "NOT_A_COLLECTION");
        STATUS_CODES.put(-814000, "CAT_UNKNOWN_COLLECTION");
        STATUS_CODES.put(-130000, "SYS_INVALID_INPUT_PARAM");
        STATUS_CODES.put(-154000, "SYS_INTERNAL_ERR");
    }

    public static void statusCodeMessage(Mapped.IrodsResponse irodsResponse, String failMessage) throws IrodsException {
        int statusCode = irodsResponse.getStatus_code();
        String statusMessage = irodsResponse.getStatus_message();

        if (STATUS_CODES.containsKey(statusCode) && statusMessage == null) {
            throw new IrodsException(failMessage + ": " + STATUS_CODES.get(statusCode));
        } else if (statusCode != 0) { // but the message is not null
            throw new IrodsException(failMessage + ": " + statusMessage);
        }
    }

    public static Map<Integer, String> getMap() {
        return STATUS_CODES;
    }


}
