package cn.wildfirechat.admin.mapper;


import cn.wildfirechat.common.model.po.MainPageInfoPO;

public interface MainPageInfoMapper {

    int insert(MainPageInfoPO mainPageInfoPO);

    int update(MainPageInfoPO mainPageInfoPO);

    MainPageInfoPO find(String date);
}
