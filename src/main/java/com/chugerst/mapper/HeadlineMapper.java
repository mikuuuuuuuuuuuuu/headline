package com.chugerst.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chugerst.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chugerst.pojo.vo.PortalVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author MIKU
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-04-09 21:41:38
* @Entity com.chugerst.pojo.Headline
*/


public interface HeadlineMapper extends BaseMapper<Headline> {
    /**
     * 查询条件keywords和type可查可不查，如果有内容就加入到where查询条件中
     * @param iPage
     * @param portalVo
     * @return {@link IPage}<{@link Map}>
     */
    IPage<Map> selectMyPage(IPage iPage,@Param("portalVo") PortalVo portalVo);

    Map queryDetailMap(Integer hid);
}




