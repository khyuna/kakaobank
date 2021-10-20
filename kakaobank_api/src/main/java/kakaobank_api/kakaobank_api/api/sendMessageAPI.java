package kakaobank_api.kakaobank_api.api;

import org.springframework.stereotype.Service;

@Service
public class sendMessageAPI {
    // 이미 있다고 가정한 메시지 API 소스
    private static final Boolean resultCode = true;

    // 항상 true로 반환하도록 설정
    public Boolean returnResultCode() {
        return resultCode;
    }
}
