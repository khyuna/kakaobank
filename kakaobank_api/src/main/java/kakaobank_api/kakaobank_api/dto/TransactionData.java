package kakaobank_api.kakaobank_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TransactionData {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        // 고객번호
        private String withdrawalAccount;// 출금계좌
        private String transferStep; // 이체 단계
        private String depositUser; // 입금받는 사람
        private String depositUserContact; // 입금받는 사람 전화번호
        private long withdrawalAmount; // 출금금액
    }

}
