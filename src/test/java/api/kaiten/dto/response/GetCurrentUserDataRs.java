package api.kaiten.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCurrentUserDataRs {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("full_name")
    @Expose
    public String fullName;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("username")
    @Expose
    public String username;
}
