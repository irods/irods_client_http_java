package org.example.Mapper;

public class IrodsResponse {
    private int status_code;
    private String status_message;

    public int getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    @Override
    public String toString() {
        return " status code: " + status_code +
                "\n status message: " + status_message;
    }
}