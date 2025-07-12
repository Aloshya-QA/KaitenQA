package factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DataFactory {

    private final String
            email,
            pin,
            password,
            username;
}
