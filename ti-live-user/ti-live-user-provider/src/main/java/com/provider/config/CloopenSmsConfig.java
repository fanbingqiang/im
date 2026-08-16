package com.provider.config;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cloopen.sms")
public class CloopenSmsConfig {

    private String serverIp;
    private String serverPort;
    private String accountSid;
    private String accountToken;
    private String appId;
    private String templateId;

    @Bean
    public CCPRestSmsSDK ccPRestSmsSDK() {
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSid, accountToken);
        sdk.setAppId(appId);
        return sdk;
    }
}
