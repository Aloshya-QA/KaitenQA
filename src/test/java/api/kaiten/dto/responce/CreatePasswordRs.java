package api.kaiten.dto.responce;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePasswordRs {

    @SerializedName("has_password")
    @Expose
    public Boolean hasPassword;
}
