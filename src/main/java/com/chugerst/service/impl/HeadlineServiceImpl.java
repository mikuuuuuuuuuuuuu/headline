package com.chugerst.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chugerst.pojo.Headline;
import com.chugerst.pojo.vo.PortalVo;
import com.chugerst.service.HeadlineService;
import com.chugerst.mapper.HeadlineMapper;
import com.chugerst.util.JwtHelper;
import com.chugerst.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author MIKU
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-04-09 21:41:38
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private HeadlineMapper headlineMapper;

    /**
     * 分页查询首页头条信息
     * 1 分页数据查询
     * 2 分页数据 拼接到result
     * 注意1 查询需要自定义语句 自定义mapper方法，携带分页
     * 注意2 返回结果为List<Map>
     *
     *查询条件keywords和type可查可不查，如果有内容就加入到where查询条件中
     * @param portalVo
     * @return {@link Result}
     */
    @Override
    public Result findNewsPage(PortalVo portalVo) {

        //这里传入分页数据PageNum和PageSize,前端每一个页面按钮发送一个请求，包含页码，页大小，分页查询的类型的数据
        //以此来进行查询
        // portalVo里有数据，同样作为参数传入到自定义mapper
        IPage iPage=new Page(portalVo.getPageNum(),portalVo.getPageSize());
        //实现mapper
        headlineMapper.selectMyPage(iPage,portalVo);
        //数据会存在iPage里，以List形式存每个个体的map
        List<Map> records = iPage.getRecords();
        //包装
        Map data =new HashMap();
        data.put("pageData",records);
        data.put("pageNum",iPage.getCurrent());
        data.put("pageSize",iPage.getSize());
        data.put("totalPage",iPage.getPages());
        data.put("totalSize",iPage.getTotal());

        Map pageInfo=new HashMap();
        pageInfo.put("pageInfo",data);
        // 响应JSON
        return Result.ok(pageInfo);
    }

    /**
     * 头条详情显示
     *
     * 根据id查询
     *用户点击"查看全文"时,向服务端发送新闻id
     *后端根据新闻id查询完整新闻文章信息并返回
     *后端要同时让新闻的浏览量+1
     *
     * 1 查询对应的数据【多表查询，头条表和用户表，mapper方法自定义 返回map】
     * 2 修改阅读量+1 【version乐观锁获取版本，才能修改】
     * @param hid
     * @return {@link Result}
     */
    @Override
    public Result showHeadlineDetail(Integer hid) {
        Map data = headlineMapper.queryDetailMap(hid);
        Map headlineMap=new HashMap();
        headlineMap.put("headline",data);

        //修改阅读量
        Headline headline =new Headline();
        headline.setHid((Integer) data.get("hid"));
        headline.setVersion((Integer) data.get("version"));

        //阅读量+1
        headline.setPageViews((Integer) data.get("pageViews")+1);
        headlineMapper.updateById(headline);

        //修改生效后news_headline的version会变

        return Result.ok(headlineMap);
    }
    @Autowired
    private JwtHelper jwtHelper;

    /**头条修改接口
     * 1 补全实体类headline信息，信息拿到实体类但是数据不全，
     * 拿到token可以补publisher，pageViews，createTime，updateTime数据
     * 2 进入添加头条信息之前要加拦截器登录检查
     * 所有要数据库修改的操作
     * 即访问headline/路径下的方法都要经过拦截器
     *
     * @param headline
     * @return {@link Result}
     */
    @Override
    public Result publish(Headline headline,String token) {
        int userId=jwtHelper.getUserId(token).intValue();
        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());
        headlineMapper.insert(headline);

        return Result.ok(null);
    }

    /**
     *
     * 1 客户端将新闻信息修改后,提交前先请求登录校验接口校验登录状态
     * 2 登录校验通过则提交修改后的新闻信息,后端接收并更新进入数据库
     *
     * 修改的必要
     * 1 hid查询数据最新version
     * 2 修改数据的更新时间为最新时间
     * @param headline
     * @return {@link Result}
     */
    @Override
    public Result updateData(Headline headline) {
        //先查询version版本，然后进行修改
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        //乐观锁,获取到version然后修改，防止改的时候出现线程安全问题
        headline.setVersion(version);
        headline.setUpdateTime(new Date());
        headlineMapper.updateById(headline);
        return Result.ok(null);
    }
}




