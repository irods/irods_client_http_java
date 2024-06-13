package org.example;

public class Status {
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

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public boolean isGenquery2_enabled() {
        return genquery2_enabled;
    }

    public void setGenquery2_enabled(boolean genquery2_enabled) {
        this.genquery2_enabled = genquery2_enabled;
    }

    public String getIrods_zone() {
        return irods_zone;
    }

    public void setIrods_zone(String irods_zone) {
        this.irods_zone = irods_zone;
    }

    public int getMax_number_of_parallel_write_streams() {
        return max_number_of_parallel_write_streams;
    }

    public void setMax_number_of_parallel_write_streams(int max_number_of_parallel_write_streams) {
        this.max_number_of_parallel_write_streams = max_number_of_parallel_write_streams;
    }

    public int getMax_number_of_rows_per_catalog_query() {
        return max_number_of_rows_per_catalog_query;
    }

    public void setMax_number_of_rows_per_catalog_query(int max_number_of_rows_per_catalog_query) {
        this.max_number_of_rows_per_catalog_query = max_number_of_rows_per_catalog_query;
    }

    public int getMax_size_of_request_body_in_bytes() {
        return max_size_of_request_body_in_bytes;
    }

    public void setMax_size_of_request_body_in_bytes(int max_size_of_request_body_in_bytes) {
        this.max_size_of_request_body_in_bytes = max_size_of_request_body_in_bytes;
    }

    public boolean isOpenid_connect_enabled() {
        return openid_connect_enabled;
    }

    public void setOpenid_connect_enabled(boolean openid_connect_enabled) {
        this.openid_connect_enabled = openid_connect_enabled;
    }
}
