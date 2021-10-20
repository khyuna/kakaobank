package kakaobank_api.kakaobank_api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kakaobank_api.kakaobank_api.dto.UserInfo;
import kakaobank_api.kakaobank_api.dto.ReceiverDataInfo;
import kakaobank_api.kakaobank_api.dto.TransactionData;
import kakaobank_api.kakaobank_api.dto.TransferInfo;
import kakaobank_api.kakaobank_api.dto.Response;
import kakaobank_api.kakaobank_api.service.CheckValidateTransferService;
import kakaobank_api.kakaobank_api.service.CompleteTransfer;
import kakaobank_api.kakaobank_api.service.PreCheckTransferService;
import kakaobank_api.kakaobank_api.service.SendTransfer;

@RestController
@RequestMapping("kakaobank")
public class CenterController {
    @Autowired
    private PreCheckTransferService preCheckTransferService;
    @Autowired
    private SendTransfer sendTransfer;
    @Autowired
    private CompleteTransfer completeTransfer;

    @Autowired
    private CheckValidateTransferService checkValidateTransfer;

    // 송긍 고객상태 확인 API
    @RequestMapping(value = "/preCheckUserInfo", method = RequestMethod.POST)
    @PostMapping
    public String preCheckUserInfo(HttpServletRequest request, String inputCusno) throws Exception {
        HttpSession session = request.getSession();
        String cusno = (String) session.getAttribute("cusno");
        Response response;
        JSONObject obj = new JSONObject();
        if (cusno != null) {
            // 세션에 고객번호가 있는경우에는 세션 고객번호 사용
            response = preCheckTransferService.checkUserState(cusno);
        } else {
            if (inputCusno != null) {
                response = preCheckTransferService.checkUserState(inputCusno);
            } else {
                // exception 발생하기
                return null;
            }
        }
        obj.put("returnCode", response.getReturnCode());
        obj.put("returnMessage", response.getReturnMessage());
        obj.put("userState", response.getUserState());
        return obj.toString();
    }

    // 송금 계좌 상태 확인 API
    @RequestMapping(value = "/preCheckAccount", method = RequestMethod.POST)

    @PostMapping
    public String preCheckDepositAccount(HttpServletRequest request, TransferInfo tInfo) throws Exception {
        HttpSession session = request.getSession();
        String cusno = (String) session.getAttribute("cusno");
        Response response;
        JSONObject obj = new JSONObject();
        response = preCheckTransferService.checkDepositAccount(request, cusno, tInfo);
        obj.put("returnCode", response.getReturnCode());
        obj.put("returnMessage", response.getReturnMessage());
        obj.put("wtihdrawalState", response.getWithdrawalState());
        return obj.toString();
    }

    // 이체 보내기 API
    @RequestMapping(value = "/sendTransfer", method = RequestMethod.POST)

    @PostMapping
    public Response sendTransferExecute(HttpServletRequest request, TransferInfo tInfo) throws Exception {
        HttpSession session = request.getSession();
        tInfo.setCusno((String) session.getAttribute("cusno"));
        return sendTransfer.sendTransferMessage(request, tInfo);
    }

    // 이체 받기 API
    @RequestMapping(value = "/receiveMoney", method = RequestMethod.POST)

    @PostMapping
    public void receiveMoneyExecute(HttpServletRequest request, ReceiverDataInfo rDataInfo) {
        HttpSession session = request.getSession();
        if (session.getAttribute("cusno") == null) {
            completeTransfer.receiveMoneyKakaoBank(rDataInfo);
        } else {
            completeTransfer.receiveMoneyOtherBank(rDataInfo);
        }

    }

    // 24시간 거래 유효성 체크 API
    @RequestMapping(value = "/validateTransfer", method = RequestMethod.POST)

    @PostMapping
    public void validateTransfer(@RequestParam TransactionData.Request request) {
        checkValidateTransfer.checkTimeforTransfer();
    }

    // 고객번호를 세션에 넣기 위한 로직
    @RequestMapping(value = "/login", method = RequestMethod.POST)

    @PostMapping
    public void setSessionUserInfo(HttpServletRequest request, UserInfo userInfo) {
        HttpSession session = request.getSession();
        // 고객번호 세션에 저장
        session.setAttribute("cusno", userInfo.getCusno());
    }

}
