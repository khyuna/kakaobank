package kakaobank_api.kakaobank_api.dao;

import org.apache.ibatis.annotations.Mapper;
import kakaobank_api.kakaobank_api.dto.DepositAccount;

@Mapper
public interface AccountInfoDao {
    public DepositAccount selectAccountState(String param) throws Exception;
}