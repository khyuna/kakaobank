package kakaobank_api.kakaobank_api.service;

import javax.servlet.http.HttpServletRequest;

import kakaobank_api.kakaobank_api.dto.DepositAccount;
import kakaobank_api.kakaobank_api.dto.Response;
import kakaobank_api.kakaobank_api.dto.TransferInfo;

public interface SendTransferInterface {
    public Response sendTransferMessage(HttpServletRequest request, TransferInfo transferInfo) throws Exception;

}
