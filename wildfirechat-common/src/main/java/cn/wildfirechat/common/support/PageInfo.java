package cn.wildfirechat.common.support;

import cn.wildfirechat.common.model.vo.PageVO;
import java.util.List;


public class PageInfo<T>  extends com.github.pagehelper.PageInfo<T>  {

    public PageInfo() {
        super();
    }

    public PageInfo(List<T> list) {
        super(list);
    }

    public PageInfo(List<T> list, int navigatePages) {
        super(list, navigatePages);
    }

    public PageVO<T> convertToPageVO(){
        return new PageVO<T>(getList(), getTotal(), getNavigateLastPage());
    }
}
