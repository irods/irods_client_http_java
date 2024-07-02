package org.example.Mapper.DataObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Mapper.Mapped;

import java.util.List;

public class DataObjectVertifyChecksum extends Mapped {
    @JsonProperty("results")
    private List<Results> results;
    @JsonProperty("r_error_info")
    private List<ErrorInfo> r_error_info;

    public static class Results {
        private int error_code;
        private String message;
        private String severity;
    }

    public static class ErrorInfo {
        @JsonProperty("status")
        private int status;
        @JsonProperty("message")
        private String message;
    }
}
