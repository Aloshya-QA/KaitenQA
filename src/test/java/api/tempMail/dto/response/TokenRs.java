package api.tempMail.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenRs {

    @SerializedName("token")
    @Expose
    public String token;
}
