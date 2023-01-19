package cn.wildfirechat.common.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 会员信息发送业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class MemberMessageSendBO {
    private Long mid;
    private String from;
    private String to;
    private Integer type;
    private String target;
    private Integer line;
    private Message message;
    private Long serverTimestamp;
    private Integer contentType;

    public boolean isChatRoom() {
        return type != null && type == 2;
    }

    public boolean isGroup() {
        return type != null && type == 1;
    }

    public boolean isMember() {
        return type != null && type == 0;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties
    public static class Message {
        private String searchableContent;
        private String content;
        private String extra;
        private String data;
        private Integer mediaType;
        private String remoteMediaUrl;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties
        public static class Extra {
            private String fileUrl;
            private Integer senderRole;// 创建者角色 1: 系统管理者, 2: 会员
            private Integer groupType;// 群组类型 1:一般,2:广播
        }
    }
}
