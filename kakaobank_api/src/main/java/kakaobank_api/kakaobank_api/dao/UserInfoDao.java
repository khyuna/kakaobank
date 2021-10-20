package kakaobank_api.kakaobank_api.dao;

import org.apache.ibatis.annotations.Mapper;
import kakaobank_api.kakaobank_api.dto.UserInfo;

@Mapper
public interface UserInfoDao {
    public UserInfo selectUser(String param) throws Exception;

}