package com.chugerst.service;

import com.chugerst.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chugerst.pojo.vo.PortalVo;
import com.chugerst.util.Result;

/**
* @author MIKU
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-04-09 21:41:38
*/
public interface HeadlineService extends IService<Headline> {


    /**
     * 首页数据查询
     * @param portalVo
     * @return {@link Result}
     */
    Result findNewsPage(PortalVo portalVo);

    Result showHeadlineDetail(Integer hid);

    Result publish(Headline headline,String token);

    Result updateData(Headline headline);
}
