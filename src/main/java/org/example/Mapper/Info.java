package org.example.Mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{ \"error\": \"Unable to serialize to JSON\" }";
        }
    }
}
