package api.tempMail.dto.request;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRq {

    @Expose
    public String address;
    @Expose
    public String password;
}
