package org.example.Mapper.DataObjects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Mapper.Mapped;

public class DataObjectCalculateSum extends Mapped {
    @JsonProperty("checksum")
    private String checksum;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
