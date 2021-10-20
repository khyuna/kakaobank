package kakaobank_api.kakaobank_api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DepositAccount {
    private String cusno;
    private String account_num;
    private String account_name;
    private int account_state;
    private String balance;
    private String isMain; // 대표계좌 여부

}
