package cn.wildfirechat.common.model.dto;

import java.util.Map;

public class HazelCastOpResultDto {
    private Integer success;
    private Map<String,Object> data;

    public HazelCastOpResultDto() {
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
