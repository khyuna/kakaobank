package kakaobank_api.kakaobank_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 계좌 정보
public class TransferInfo {
    private String cusno; // 출금인 고객번호
    private String withdrawal_account;// 출금계좌
    private int transfer_step; // 이체 단계
    private String receiver_num; // 입금받는 사람
    private int withdrawal_amount; // 출금금액
    private String transfer_date;
    private String transfer_time;
    private String lst_transfer_date;
    private String lst_transfer_time;
    private int transaction_num;
}
