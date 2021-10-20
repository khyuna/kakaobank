package kakaobank_api.kakaobank_api.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kakaobank_api.kakaobank_api.api.sendMessageAPI;
import kakaobank_api.kakaobank_api.dto.DepositAccount;
import kakaobank_api.kakaobank_api.dto.TransferInfo;
import kakaobank_api.kakaobank_api.dto.Response;
import kakaobank_api.kakaobank_api.dao.AccountInfoDao;
import kakaobank_api.kakaobank_api.dao.TransferInfoDao;

@Service
public class SendTransfer implements SendTransferInterface {
    // 동기화 사용필수
    private static final int SEND_TRANSFER = 2;
    private static final int SEND_TRANSFER_COMPLETE = 3;
    private static final int SEND_TRANSFER_ERROR = 8;

    private static final String SEND_TRANSFER_OK = "0000";
    private static final String SEND_TRANSFER_FAIL = "9999";

    private static Response response;
    private static TransferInfo tInfo;
    private static DepositAccount dAccount;
    @Autowired
    private sendMessageAPI sendMessageAPI;
    @Autowired
    private TransferInfoDao transferInfoDao;
    @Autowired
    private AccountInfoDao accountInfoDao;

    @Override
    public Response sendTransferMessage(HttpServletRequest request, TransferInfo transferInfo) throws Exception {
        HttpSession session = request.getSession();
        String transferUniqueKey = (String) session.getAttribute("transferUniqueKey");
        String transfer_date = transferUniqueKey.substring(0, 7);
        String transfer_time = transferUniqueKey.substring(8, 14);
        int transaction_num = Integer.parseInt(transferUniqueKey.substring(15));
        tInfo = transferInfo;
        tInfo.setTransfer_date(transfer_date);
        tInfo.setTransfer_time(transfer_time);
        tInfo.setTransaction_num(transaction_num);

        if (sendMessageAPI.returnResultCode()) {
            // 메시지 보내기 성공시 -> 메시지 보낸 상태로 원장 업데이트필요
            // 캐시 DB에도 insert 필요
            setTransferInfoDTO();
            if (userDBUpdateforTransfer()) {
                tInfo.setTransfer_step(SEND_TRANSFER_COMPLETE);
                response.setReturnCode(SEND_TRANSFER_OK);
            } else {
                response.setReturnCode(SEND_TRANSFER_FAIL);
            }

        } else {
            // 이체 오류상태
            tInfo.setTransfer_step(SEND_TRANSFER_ERROR);
            userDBUpdateforTransfer();
            response.setReturnCode(SEND_TRANSFER_FAIL);
            response.setReturnMessage("메시지 전송에 실패했습니다.");
        }
        return response;
    }

    @Transactional
    private Boolean userDBUpdateforTransfer() throws Exception {
        // 돈보내기
        // 잔액 차감
        String dAccountNum = tInfo.getWithdrawal_account();
        if (dAccountNum != null) {
            dAccount = getDepositAccount(tInfo.getWithdrawal_account());
        } else {
            tInfo.setTransfer_step(SEND_TRANSFER_ERROR);
            response.setReturnCode(SEND_TRANSFER_FAIL);
            transferInfoDao.updateStateTransferInfo(tInfo);
            response.setReturnMessage("이체 처리 중 오류가 발생했습니다. 계좌를 확인해주세요.");
            return false;
        }

        int nowBalance = Integer.parseInt(dAccount.getBalance());
        int afterTransferBalance = nowBalance - tInfo.getWithdrawal_amount();
        if (afterTransferBalance <= 0) {
            // 에러 처리
            tInfo.setTransfer_step(SEND_TRANSFER_ERROR);
            userDBUpdateforTransfer();
            response.setReturnCode(SEND_TRANSFER_FAIL);
            transferInfoDao.updateStateTransferInfo(tInfo);
            response.setReturnMessage("이체 처리 중 오류가 발생했습니다. 잔액을 확인해주세요.");
            return false;
        } else {
            // update필요
            transferInfoDao.updateStateTransferInfo(tInfo);
        }
        // 캐시DB에도 insert 추가 필요
        if (insertTransferWait()) {
            return true;
        }
        response.setReturnCode(SEND_TRANSFER_FAIL);
        transferInfoDao.updateStateTransferInfo(tInfo);
        response.setReturnCode(SEND_TRANSFER_FAIL);
        response.setReturnMessage("이체 처리 중 오류가 발생했습니다.");
        return false;
    }

    private DepositAccount getDepositAccount(String accountNum) throws Exception {
        return accountInfoDao.selectAccountState(accountNum);
    }

    private Boolean insertTransferWait() throws Exception {
        // transfer_wait 에 추가해줌(24시간 시간관리필요)
        transferInfoDao.insertTransferWait(tInfo);
        return true;
    }

    private void setTransferInfoDTO() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");

        Date time = new Date();

        String sys_date = format1.format(time);
        String sys_time = format2.format(time);

        tInfo.setLst_transfer_date(sys_date); // 현재 날짜
        tInfo.setLst_transfer_time(sys_time); // 현재 시간
        tInfo.setTransfer_step(SEND_TRANSFER);
    }
}
