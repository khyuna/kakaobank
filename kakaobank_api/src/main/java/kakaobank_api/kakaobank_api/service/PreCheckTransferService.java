package kakaobank_api.kakaobank_api.service;

import java.util.List;

import javax.transaction.Transactional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kakaobank_api.kakaobank_api.dao.AccountInfoDao;
import kakaobank_api.kakaobank_api.dao.TransferInfoDao;
import kakaobank_api.kakaobank_api.dao.UserInfoDao;
import kakaobank_api.kakaobank_api.dto.DepositAccount;
import kakaobank_api.kakaobank_api.dto.TransferInfo;
import kakaobank_api.kakaobank_api.dto.UserInfo;
import kakaobank_api.kakaobank_api.dto.*;

@Service
public class PreCheckTransferService implements PreCheckTransferServiceInterface {
    // 입금여부판단 서비스
    // 입금가능여부 판단하는 서비스가 최초 STEP이므로 1
    private static final int PRE_CHECK_TRANSFER_AMOUNT = 1;
    private static final String RESULT_OK_CODE = "0000";
    private static String RESULT_FAIL_CODE = "1111";
    private static final int STATE_OK = 0;
    private static final int STATE_NOT_OK = 0;
    private static final int NOT_ENOUGH_BALANCE_STATE = 1;
    private static final int UNUSUAL_STATE = 2;

    private TransferInfo tInfo;
    private static Response response;
    private static UserInfo userInfo;
    private static DepositAccount depositAccount;
    private String cusno;

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private TransferInfoDao transferInfoDao;
    @Autowired
    private AccountInfoDao accountInfoDao;

    public Response checkUserState(String cusno) throws Exception {
        this.cusno = cusno;
        userInfo = getUserInfo();
        response = new Response();

        if (userInfo.getUser_state() == 0) {
            // 정상상태
            // userTransferInfo.setTransferStep(PRE_CHECK_USER);
            response.setReturnCode(RESULT_OK_CODE);
            response.setUserState(STATE_OK);
            response.setReturnMessage("");
        } else {
            // 고객상태에 따른 정의 없으므로 우선 else로 처리
            response.setReturnCode(RESULT_FAIL_CODE);
            response.setUserState(userInfo.getUser_state());
            response.setReturnMessage("고객원장 확인요망");
        }

        return response;
    }

    public Response checkDepositAccount(HttpServletRequest request, String cusno, TransferInfo tInfo) {
        this.cusno = cusno;
        this.tInfo = tInfo;
        response = new Response();
        HttpSession session = request.getSession();
        if (setTransferInfoAmount()) {
            // 계좌 상태가 출금가능상태인경우
            // response에 대한 처리 필요
            response.setReturnCode(RESULT_OK_CODE);
            response.setTransferState(STATE_OK);
            response.setReturnMessage("");
            String transferUniqueKey = tInfo.getTransfer_date().concat(tInfo.getTransfer_time())
                    .concat(String.valueOf(tInfo.getTransaction_num()));
            session.setAttribute("transferUniqueKey", transferUniqueKey);
        } else {
            response.setReturnCode("");
            response.setTransferState(STATE_NOT_OK);
            response.setReturnMessage("이체 불가");
        }

        return response;
    }

    private Boolean setTransferInfoAmount() {
        try {

            depositAccount = getCheckAccountState(this.tInfo);
            if (depositAccount.getAccount_state() == STATE_OK) {
                // 정상상태
                // 이체 대기시작상태 (수취인 조회상태 )
                tInfo.setTransfer_step(PRE_CHECK_TRANSFER_AMOUNT);
                insertTransferInfo();
                return true;
            } else {
                if (depositAccount.getAccount_state() == UNUSUAL_STATE) {
                    //
                } else {

                }
                return false;
            }
        } catch (Exception e) {
            // TODO 고객안내문구 넣기
            e.printStackTrace();
            return false;
        }
    }

    private UserInfo getUserInfo() throws Exception {
        // 세션에 있는 고객번호로 해당 고객 상태 반환
        return userInfoDao.selectUser(this.cusno);
    }

    private DepositAccount getCheckAccountState(TransferInfo tInfo) throws Exception {
        // TODO 이체 가능한 상태인지 알아보는 로직 추가
        // 이체금액, 출금계좌에 따라서 넘어오는 로직 필요
        // 해당 금액으로 계좌자체가 출금가능상태인지, 이체가능한상태인지 체크
        return accountInfoDao.selectAccountState(tInfo.getWithdrawal_account());
    }

    private void insertTransferInfo() {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");

            Date time = new Date();

            String sys_date = format1.format(time);
            String sys_time = format2.format(time);
            Random rnd = new Random();
            tInfo.setTransaction_num(rnd.nextInt(Integer.MAX_VALUE) + 1);
            tInfo.setTransfer_date(sys_date);
            tInfo.setTransfer_time(sys_time);
            tInfo.setCusno(this.cusno);
            transferInfoDao.insertTransferInfo(tInfo);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
