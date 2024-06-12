package com.chugerst.controller;

import com.chugerst.pojo.vo.PortalVo;
import com.chugerst.service.HeadlineService;
import com.chugerst.service.TypeService;
import com.chugerst.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.PropertyResourceBundle;

/**
 *
 *
 * @author MIKU
 * @date 2024/04/10
 */
@RestController
@RequestMapping("portal")
@CrossOrigin
public class PortalController {
    @Autowired
   private TypeService typeService;
    @Autowired
    private HeadlineService headlineService;

    /**
     * 进入新闻首页,查询所有分类并动态展示新闻类别栏位
     1. url地址：portal/findAllTypes
     请求方式：get
     请求参数：无
     响应数据：
     成功
     {
        "code":"200",
        "message":"OK"
        "data":{[{
        "tid":"1",
        "tname":"新闻"
     },{
        "tid":"2",
        "tname":"体育"
     },{
        "tid":"3",
        "tname":"娱乐"
     },{
        "tid":"4",
        "tname":"科技"
     },{
        "tid":"5",
        "tname":"其他"
     }]}
     }

     * @return {@link Result}
     */
    @GetMapping("findAllTypes")
    public Result findAllTypes(){
        Result result=typeService.findAllTypes();
        return result;
    }

    /**
     * 首页分页查询，前端类型按钮新闻类型，会按照新闻类型查，
     * url地址：portal/findNewsPage
     *
     * 请求方式：post
     *
     * 请求参数:
     * {
     *     "keyWords":"莫斯科", // 搜索标题关键字
     *     "type":0,           // 新闻类型
     *     "pageNum":1,        // 页码数
     *     "pageSize":10     // 页大小
     * }
     * @return
     */
    @PostMapping("findNewsPage")
    public Result findNewsPage(@RequestBody PortalVo portalVo){
        Result result = headlineService.findNewsPage(portalVo);
        return result;
    }


    /**
     * 成功
     * {
     *     "code":"200",
     *     "message":"success",
     *     "data":{
     *         "headline":{
     *             "hid":"1",                     // 新闻id
     *             "title":"马斯克宣布 ... ...",   // 新闻标题
     *             "article":"... ..."            // 新闻正文
     *             "type":"1",                    // 新闻所属类别编号
     *             "typeName":"科技",             // 新闻所属类别
     *             "pageViews":"40",              // 新闻浏览量
     *             "pastHours":"3" ,              // 发布时间已过小时数
     *             "publisher":"1" ,              // 发布用户ID
     *             "author":"张三"                 // 新闻作者
     *         }
     *     }
     * }
     *用户点击"查看全文"时,向服务端发送新闻id
     *后端根据新闻id查询完整新闻文章信息并返回
     *后端要同时让新闻的浏览量+1
     * @param hid
     * @return {@link Result}
     */
    @PostMapping("showHeadlineDetail")
    public Result showHeadlineDetail(Integer hid){
        Result result= headlineService.showHeadlineDetail(hid);
        return result;
    }
}
