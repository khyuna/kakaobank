package kakaobank_api.kakaobank_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    private String cusno; // 출금인 고객번호
    private int user_state;// 고객상태

}
