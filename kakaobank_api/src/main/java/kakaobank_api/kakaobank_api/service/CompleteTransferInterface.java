package kakaobank_api.kakaobank_api.service;

import kakaobank_api.kakaobank_api.dto.ReceiverDataInfo;
import kakaobank_api.kakaobank_api.dto.Response;

public interface CompleteTransferInterface {

    public void receiveMoneyKakaoBank(ReceiverDataInfo rDataInfo) throws Exception;

    public Response checkUserState(String cusno) throws Exception;
}
