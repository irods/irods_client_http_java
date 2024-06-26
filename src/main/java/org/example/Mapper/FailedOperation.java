package org.example.Mapper;

import org.example.Collections.PermissionJson;

public class FailedOperation {

    private PermissionJson operation;
    private int operation_index;
    private String status_message;

    @Override
    public String toString() {
        return "FailedOperation{" +
                "operation=" + operation +
                ", operation_index=" + operation_index +
                ", status_message='" + status_message + '\'' +
                '}';
    }



}
