package dz.islem.githubapi.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OwnerModel implements Serializable {
    @SerializedName("login")
    @Expose
    private String name;
    @SerializedName("avatar_url")
    @Expose
    private String avatar_url;
    @SerializedName("html_url")
    @Expose
    private String html_url;

    public OwnerModel(String name, String avatar_url, String html_url) {
        this.name = name;
        this.avatar_url = avatar_url;
        this.html_url = html_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }
}
