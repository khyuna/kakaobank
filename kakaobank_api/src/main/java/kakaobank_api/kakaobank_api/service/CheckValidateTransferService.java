package kakaobank_api.kakaobank_api.service;

import org.springframework.stereotype.Service;

@Service
public class CheckValidateTransferService implements CheckValidateTransferInterface {
    // 24시간 체크 상주 데몬 ..
    // 24시간이 지난 이후에는 메시지 API 호출해서 메시지를 보내야함
    // 메시지 보내기전 update 치고 메시지 보냄
    // TODO 취소되는 경우에는 다시 송금인에게 이체해줘야함 !!!!!
    public void checkTimeforTransfer() {
        // 24시간이 아직 지나지않았는지 확인
    }
}
