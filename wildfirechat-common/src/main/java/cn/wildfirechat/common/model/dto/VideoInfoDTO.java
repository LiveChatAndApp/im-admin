package cn.wildfirechat.common.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoInfoDTO {
    private Long duration;

    private String thumb;

    private Long fileSize;
}
