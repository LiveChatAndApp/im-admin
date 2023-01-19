package cn.wildfirechat.common.model.dto;

import cn.wildfirechat.common.model.enums.AppOperateLogEnum;
import cn.wildfirechat.common.model.vo.MemberOperateLogVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberOperateLogCsvDTO {

    @JsonProperty("操作权限")
    private String operator;

    @JsonProperty("操作行为")
    private String operatorManner;

    @JsonProperty("地理位置")
    private String ipAndLocation;

    @JsonProperty("备注")
    private String memo;

    @JsonProperty("操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public static MemberOperateLogCsvDTO from(MemberOperateLogVO logVO) {
        if (logVO == null) {
            return new MemberOperateLogCsvDTO();
        }

        Map<String, String> beforeMap = logVO.getMemo().getBefore().stream()
                .collect(Collectors.toMap(LogPairDto::getKey, LogPairDto::getVal));

        StringBuilder operatorSb = new StringBuilder()
                .append("昵称：").append(logVO.getNickName()).append("\n")
                .append("账号：").append(logVO.getMemberName()).append("\n")
                .append("手机号：").append(logVO.getPhone()).append("\n");

        StringBuilder operatorMannerSb = new StringBuilder()
                .append(AppOperateLogEnum.parseByKey(logVO.getAuthId()).getName()).append("\n");

        StringBuilder memoSb = new StringBuilder()
                .append("用户账号：").append(logVO.getMemberName()).append("\n")
                .append("操作设备：").append(beforeMap.get("操作设备")).append("\n");

        StringBuilder ipAndLocationSb = new StringBuilder()
                .append(logVO.getCreatorIp()).append("\n")
                .append(logVO.getCreatorLocation()).append("\n");


        return MemberOperateLogCsvDTO.builder()
                .operator(operatorSb.toString())
                .operatorManner(operatorMannerSb.toString())
                .memo(memoSb.toString())
                .ipAndLocation(ipAndLocationSb.toString())
                .createTime(logVO.getCreateTime())
                .build();
    }
}
