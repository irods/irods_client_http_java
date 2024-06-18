package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info {
    //@JsonProperty("api_version")
    private String api_version;
    private String build;
    private boolean genquery2_enabled;
    private String irods_zone;
    private int max_number_of_parallel_write_streams;
    private int max_number_of_rows_per_catalog_query;
    private int max_size_of_request_body_in_bytes;
    private boolean openid_connect_enabled;

    public String getApi_version() {
        return api_version;
    }

    public String getBuild() {
        return build;
    }

    public boolean isGenquery2_enabled() {
        return genquery2_enabled;
    }

    public String getIrods_zone() {
        return irods_zone;
    }

    public int getMax_number_of_parallel_write_streams() {
        return max_number_of_parallel_write_streams;
    }

    public int getMax_number_of_rows_per_catalog_query() {
        return max_number_of_rows_per_catalog_query;
    }

    public int getMax_size_of_request_body_in_bytes() {
        return max_size_of_request_body_in_bytes;
    }

    public boolean isOpenid_connect_enabled() {
        return openid_connect_enabled;
    }

    @Override
    public String toString() {
        return  "{\n" +
                "api_version='" + api_version + '\'' + ",\n" +
                "build='" + build + '\'' + ",\n" +
                "genquery2_enabled=" + genquery2_enabled + ",\n" +
                "irods_zone='" + irods_zone + '\'' + ",\n" +
                "max_number_of_parallel_write_streams=" + max_number_of_parallel_write_streams + ",\n" +
                "max_number_of_rows_per_catalog_query=" + max_number_of_rows_per_catalog_query + ",\n" +
                "max_size_of_request_body_in_bytes=" + max_size_of_request_body_in_bytes + ",\n" +
                "openid_connect_enabled=" + openid_connect_enabled + ",\n" +
                '}';
    }
}
