package cn.wildfirechat.common.support;

import cn.wildfirechat.common.utils.PageUtil;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页类
 */
@Data
@Slf4j
@NoArgsConstructor
public class Page {

    /**
     * 页码数
     */
    @ApiModelProperty("页码数")
    private int page = 0;

    /**
     * 每页记录数
     */
    @ApiModelProperty("每页记录数")
    private int rows = 10;


    /**
     * 是否排序
     */
    @ApiModelProperty(value = "false", required = false, example = "false")
    private boolean needSort = false;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序方式(_create_time,DESC、_update_time,DESC) 预设(_create_time,DESC)", required = false, example = "_create_time,DESC")
    private String sort;

    public com.github.pagehelper.Page<?> startPageHelper() {
        log.info("[page 分页设定] 页码数:{}, 每页笔数:{}, 是否排序:{}, 排序:{}",page, rows, needSort, sort);
        if(needSort){
            if(StringUtils.isBlank(sort)){
                sort = "_create_time,DESC";//预设
            }
            String orderBySql = PageUtil.orderBySql(sort);
            return PageHelper.startPage(page, rows, orderBySql);
        }else {
            return PageHelper.startPage(page, rows);

        }

    }

}
