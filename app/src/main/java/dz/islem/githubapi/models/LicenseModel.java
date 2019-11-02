package dz.islem.githubapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LicenseModel implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;

    public LicenseModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
