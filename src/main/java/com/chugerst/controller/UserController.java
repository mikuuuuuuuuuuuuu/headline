package com.chugerst.controller;


import com.chugerst.pojo.User;
import com.chugerst.service.UserService;
import com.chugerst.util.JwtHelper;
import com.chugerst.util.Result;
import com.chugerst.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin//允许前端访问
public class UserController {
    @Autowired
    private UserService userService;

    /**
     *
     * login登录，访问user/login
     * post方式，
     *
     * 请求json数据:
     * {
     *     "username":"zhangshan",
     *     "userPwd":"123456"
     * }
     * 返回:
     * 成功
     * {
     *     "code":"200",
     *     "message":"success",
     *     "data":{
     *     "token":"..."
     *     }
     * }
     * 失败
     * {
     *     "code":"501",
     *     "message":"用户名错误",
     *     "data":{}
     * }
     * @param user
     * @return {@link Result}
     */
    @PostMapping("login")
    public Result login(@RequestBody User user){
        Result result= userService.login(user);
        return result;
    }

    /**
     *
     * token验证，可以通过token访问，无需账号密码
     * get只有请求头，默认从param获取数据，要加@RequestHeader才能获取token数据
     * 请求头传入token
     * @param token
     * @return {@link Result}
     */
    @GetMapping("getUserInfo")
    public Result getUserInfo(@RequestHeader String token){
        Result result=userService.getUserInfo(token);
        return result;
    }

    /**
     /**检查账号是否可以注册
     * url地址：user/checkUserName
     * 请求方式：POST
     * 请求参数：param形式
     * username=zhangsan
     * 响应数据:
     * {
     *    "code":"200",
     *    "message":"success"
     *    "data":{}
     * }
     *

     * @param username
     * @return {@link Result}
     */
    @PostMapping("checkUserName")
    public Result checkUsername(String username){
        Result result=userService.checkUserName(username);
        return result;
    }

    /**
     /**
     * url地址：user/regist
     * 请求方式：POST
     * 请求参数：
     * {
     *     "username":"zhangsan",
     *     "userPwd":"123456",
     *     "nickName":"张三"
     * }
     * 响应数据：
     * {
     *    "code":"200",
     *    "message":"success"
     *    "data":{}
     * }
     *
     * 实现步骤:
     *   1. 将密码加密
     *   2. 将数据插入
     *   3. 判断结果,成 返回200 失败 505
     *
     * @param user
     * @return {@link Result}
     */
    @PostMapping("regist")
    public Result regist(@RequestBody User user){
        Result result=userService.regist(user);
        return result;
    }

    @Autowired
    private JwtHelper jwtHelper;

    /**
     *
     * 登录检查
     * 在controller层就可以实现
     * @param token
     * @return {@link Result}
     */
    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token){
        boolean expiration = jwtHelper.isExpiration(token);
        //1是到期，0是未到期
        if(expiration){
            return  Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        return Result.ok(null);
    }




}
