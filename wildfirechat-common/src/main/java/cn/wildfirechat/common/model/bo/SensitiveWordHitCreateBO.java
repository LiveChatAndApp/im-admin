package cn.wildfirechat.common.model.bo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class SensitiveWordHitCreateBO {

    /** 寄送者 */
    private String senderUid;
    /** 接收者 */
    private String receiverUid;
    /** 聊天室类型 */
    private Integer type;
    /** 内容 */
    private String content;
    /** 敏感词 */
    private List<String> sensitive;

    public boolean isGroup() {
        return type != null && type == 1;
    }

    public boolean isMember() {
        return type != null && type == 0;
    }

    public boolean isChatroom() {
        return type != null && type == 2;
    }
}
