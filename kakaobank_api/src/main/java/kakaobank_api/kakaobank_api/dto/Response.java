package kakaobank_api.kakaobank_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response {
    private int withdrawalState;
    private int transferState;
    private int userState;
    private String returnCode;
    private String returnMessage;
}
