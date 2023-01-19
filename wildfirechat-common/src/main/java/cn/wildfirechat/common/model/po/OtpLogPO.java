package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpLogPO {
    private Long id;
    private Long userId;
    private String secretKey;
    private String totpCode;
    private Integer status;
    private String action;
    private Date createTime;
    private String creator;
}
