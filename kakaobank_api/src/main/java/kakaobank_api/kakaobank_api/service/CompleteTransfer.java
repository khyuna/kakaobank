package kakaobank_api.kakaobank_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kakaobank_api.kakaobank_api.dao.TransferInfoDao;
import kakaobank_api.kakaobank_api.dto.ReceiverDataInfo;
import kakaobank_api.kakaobank_api.dto.Response;
import kakaobank_api.kakaobank_api.dto.TransferInfo;

@Service
public class CompleteTransfer implements CompleteTransferInterface {

    // 돈 받기 프로세스
    private static final int SEND_TRANSFER = 9; // 돈받기 완료
    private static final int AVAILABLE_RECEIVE = 0;
    @Autowired
    private static TransferInfo tInfo;
    @Autowired
    private static ReceiverDataInfo rInfo;
    @Autowired
    private TransferInfoDao transferInfoDao;

    private void searchTransferInfo() throws Exception {
        // 이체테이블에서 이체 이력 찾아서 반
        // 금액 / 입금계좌 / 출금계좌
        // 고객이 취소했거나 이미 받은 건에 대한 처리 로직 필요
        // 이체 대기상태인 경우에만 이체 실행해야함.
        // 거래번호로 거래 찾기(거래번호 + 거래일자 + 시분초 )
        tInfo = transferInfoDao.selectTransferTransaction(rInfo);
        if (tInfo.getTransfer_step() == 2) {
            // 이체 대기상태의 경우
            // TODO 이체 상태에 대한 정의 필요
            transferInfoDao.receiveMoney(tInfo);

        } else {
            // 이체 상태에 따른 메시지
        }
    }

    public void receiveMoneyOtherBank(ReceiverDataInfo rDataInf) {
        // 타행이체 선택한 경우
        // 세션에 고객번호가 없는 경우
        // 타기관 이체 API 통해서 이체 완료해야함
        updateTransferInfo();
    }

    @Override
    public void receiveMoneyKakaoBank(ReceiverDataInfo rDataInfo) {

        try {
            if (getCheckAccountState(rDataInfo) == AVAILABLE_RECEIVE) {
                transferInfoDao.receiveMoney(tInfo);
            } else {
                // 다른계좌로 선택할수있도록 에러처리
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteTempTransaction() {

    }

    private void updateTransferInfo() {
        // 입금인 내용 입력

    }

    private int getCheckAccountState(ReceiverDataInfo tInfo) throws Exception {
        // TODO 이체 가능한 상태인지 알아보는 로직 추가
        // 이체금액, 출금계좌에 따라서 넘어오는 로직 필요
        // 해당 금액으로 계좌자체가 출금가능상태인지, 이체가능한상태인지 체크
        return 1;
    }

    @Override
    public Response checkUserState(String cusno) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
