package kakaobank_api.kakaobank_api.dao;

import org.apache.ibatis.annotations.Mapper;
import kakaobank_api.kakaobank_api.dto.ReceiverDataInfo;
import kakaobank_api.kakaobank_api.dto.TransferInfo;

@Mapper
public interface TransferInfoDao {
    public void insertTransferInfo(TransferInfo tInfo) throws Exception;

    // 이체 보내는 부분 -> 잔고 차감
    public void sendTransferExecute(TransferInfo tInfo) throws Exception;

    // 거래 번호 + 시분초로 유니크한 PK로 해당 거래의 내용 찾기
    public TransferInfo selectTransferTransaction(ReceiverDataInfo rInfo) throws Exception;

    // 거래 내용 찾으면 잔고 플러스하기
    public void receiveMoney(TransferInfo tInfo) throws Exception;

    public void deleteTempTransaction(TransferInfo tInfo) throws Exception;

    public void updateReceiverData(ReceiverDataInfo rDataInfo) throws Exception;

    public void updateStateTransferInfo(TransferInfo tInfo) throws Exception;

    public void insertTransferWait(TransferInfo tInfo) throws Exception;

    public void selectTransferState(TransferInfo tInfo) throws Exception;
}
