package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePasswordRq {

    @Expose
    @SerializedName("old_password")
    public String oldPassword;

    @Expose
    @SerializedName("password")
    public String password;
}
