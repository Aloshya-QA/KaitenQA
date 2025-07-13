package api.tempMail.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Domain {

    @SerializedName("domain")
    @Expose
    public String domain;
}