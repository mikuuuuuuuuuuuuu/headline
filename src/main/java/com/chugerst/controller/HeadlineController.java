package com.chugerst.controller;


import com.chugerst.pojo.Headline;
import com.chugerst.service.HeadlineService;
import com.chugerst.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadlineController {

    @Autowired
    private HeadlineService headlineService;



    /**接值的headline实体类是不全的
     * 请求参数:
     * {
     *     "title":"尚硅谷宣布 ... ...",   // 文章标题
     *     "article":"... ...",          // 文章内容
     *     "type":"1"                    // 文章类别
     * }
     * 响应数据：
     *
     * 未登录
     * {
     *     "code":"504",
     *     "message":"loginExpired",
     *     "data":{}
     * }
     * 成功
     * {
     *     "code":"200",
     *     "message":"success",
     *     "data":{}
     * }
     * @param headline
     * @return {@link Result}
     */
    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline,@RequestHeader String token){
        Result result = headlineService.publish(headline,token);
        return result;
    }

    /**
     * 头条信息修改回显
     * 1 前端先调用登录校验接口,校验登录是否过期
     * 2 登录校验通过后 ,则根据新闻id查询新闻的完整信息并响应给前端
     * 成功
     * {
     *     "code":"200",
     *     "message":"success",
     *     "data":{
     *         "headline":{
     *             "hid":"1",
     *             "title":"马斯克宣布",
     *             "article":"... ... ",
     *             "type":"2"
     *         }
     *     }
     * }
     *
     * 这次用iservice层mybatis-plus的方法查找
     * @param hid
     * @return {@link Result}
     */
    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid){
        Headline headline = headlineService.getById(hid);
        Map data= new HashMap();
        data.put("headline",headline);
        return  Result.ok(data);
    }

    /**
     * 头条修改接口
     * @param headline
     * @return {@link Result}
     *
     * 请求参数:
     * {
     *     "hid":"1",
     *     "title":"尚硅谷宣布 ... ...",
     *     "article":"... ...",
     *     "type":"2"
     * }
     * 响应数据：
     * 成功
     * {
     *     "code":"200",
     *     "message":"success",
     *     "data":{}
     * }
     *
     */
    @PostMapping("update")
    public Result update(@RequestBody Headline headline){
        Result result = headlineService.updateData(headline);
        return  result;
    }

    /**
     * 头条删除（逻辑删除）
     * 这次用iservice层mybatis-plus的方法removeById删除
     *
     * 请求参数
     * hid=1 param形成参数
     *
     * 成功
     * {
     *     "code":"200",
     *     "message":"success",
     *     "data":{}
     * }
     * @param hid
     * @return {@link Result}
     */
    @PostMapping("removeByHid")
    public Result removeByHid(Integer hid){
        headlineService.removeById(hid);
        return  Result.ok(null);
    }
}
