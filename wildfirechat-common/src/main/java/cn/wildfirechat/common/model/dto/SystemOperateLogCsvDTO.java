package cn.wildfirechat.common.model.dto;

import cn.wildfirechat.common.model.enums.AppOperateLogEnum;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.query.AdminAuthQuery;
import cn.wildfirechat.common.model.vo.MemberOperateLogVO;
import cn.wildfirechat.common.model.vo.SystemOperateLogVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemOperateLogCsvDTO {

    @JsonProperty("操作账号")
    private String operator;

    @JsonProperty("请求方法")
    private String operatorManner;

    @JsonProperty("原值")
    private String memoBefore;

    @JsonProperty("新值")
    private String memoAfter;

    @JsonProperty("地理位置")
    private String ipAndLocation;

    @JsonProperty("操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public static SystemOperateLogCsvDTO from(SystemOperateLogVO logVO, Map<Long, String> authMap) {
        if (logVO == null) {
            return new SystemOperateLogCsvDTO();
        }

        Map<String, String> beforeMap = logVO.getMemo().getBefore().stream()
                .collect(Collectors.toMap(LogPairDto::getKey, LogPairDto::getVal));

        Map<String, String> afterMap = logVO.getMemo().getAfter().stream()
                .collect(Collectors.toMap(LogPairDto::getKey, LogPairDto::getVal));


        StringBuilder operatorSb = new StringBuilder()
                .append(logVO.getUserName());
//                .append("用户ID:").append(logVO.getAdminId()).append("\n")
//                .append("操作账号：").append(logVO.getUserName()).append("\n")
//                .append("用户昵称：").append(logVO.getNickName()).append("\n");

        StringBuilder operatorMannerSb = new StringBuilder()
                .append(authMap.get(logVO.getAuthId())).append("\n");

        StringBuilder memoBeforeSb = new StringBuilder();
        for(String key : beforeMap.keySet()){
            memoBeforeSb.append(key).append(": ").append(beforeMap.get(key)).append("\n");
        }

        StringBuilder memoAfterSb = new StringBuilder();
        for(String key : afterMap.keySet()){
            memoAfterSb.append(key).append(": ").append(afterMap.get(key)).append("\n");
        }


        StringBuilder ipAndLocationSb = new StringBuilder()
                .append(logVO.getCreatorIp()).append("\n")
                .append(logVO.getCreatorLocation()).append("\n");


        return SystemOperateLogCsvDTO.builder()
                .operator(operatorSb.toString())
                .operatorManner(operatorMannerSb.toString())
                .memoBefore(memoBeforeSb.toString())
                .memoAfter(memoAfterSb.toString())
                .ipAndLocation(ipAndLocationSb.toString())
                .createTime(logVO.getCreateTime())
                .build();
    }
}
