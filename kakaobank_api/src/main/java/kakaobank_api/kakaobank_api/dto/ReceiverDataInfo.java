package kakaobank_api.kakaobank_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceiverDataInfo {
    private String withdrawalAccount;// 출금계좌
    private long withdrawalAmount; // 출금금액
    private String transferDate; // 출금
    private String transferTime;
    private String transactionNum;
    private String receiverAccount;
    private String receiverBankCode;
}