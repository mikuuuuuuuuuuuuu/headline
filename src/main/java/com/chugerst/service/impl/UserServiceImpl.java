package com.chugerst.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chugerst.pojo.User;
import com.chugerst.service.UserService;
import com.chugerst.mapper.UserMapper;
import com.chugerst.util.JwtHelper;
import com.chugerst.util.MD5Util;
import com.chugerst.util.Result;
import com.chugerst.util.ResultCodeEnum;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author MIKU
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-04-09 21:41:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtHelper jwtHelper;
    /**
     * 登录业务实现
     * @param user
     * @return {@link Result}
     */
    @Override
    public Result login(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(lambdaQueryWrapper);
        //没查到用户，返回账号错误
        if(loginUser==null){
            return Result.build(null,ResultCodeEnum.USERNAME_ERROR);
        }
        //接着对比密码，密码非空且密码对应
        if(!StringUtils.isEmptyOrWhitespaceOnly(user.getUserPwd())&& MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())){
        //登录成功，根据id生成token，
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));
            Map data = new HashMap();
            data.put("token",token);
            //token封装到result返回
            return Result.ok(data);
        }

            //密码错误返回
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    /**
     * token验证业务实现
     * 1.token是否在有效期
     * 2 根据token解析userId
     * 3 根据用户id查询用户数据
     * 4.去掉密码，封装resuLt结果返回即可
     * @param token
     * @return {@link Result}
     */
    @Override
    public Result getUserInfo(String token) {
        boolean expiration= jwtHelper.isExpiration(token);
        //1是到期，0是未到期，到期就返回504，然后data不传值
        if(expiration){
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        int userId=jwtHelper.getUserId(token).intValue();
        User user = userMapper.selectById(userId);
        user.setUserPwd("");
        Map data = new HashMap();
        data.put("loginUser",user);
        return Result.ok(data);
    }

    /**
     * 用户是否占用（可能是给ajax用的）
     * 查询是否存在用户注册过
     * 实现步骤:
     *   1. 获取账号数据
     *   2. 根据账号进行数据库查询
     *   3. 结果封装
     * @param username
     * @return {@link Result}
     */
    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,username);
        //返回查到的行数
        Long count = userMapper.selectCount(lambdaQueryWrapper);
        //没查到用户，返回ok，可以注册
        if(count==0){
            return Result.ok(null);
        }
        return Result.build(null,ResultCodeEnum.USERNAME_USED);
    }


    /**
     *
     * 注册功能实现
     * 1 注册用户是否占用（注册还要再验证一次）
     * 2 （账号密码格式前端处理）密码加密传给数据库保存
     * 3 封装结果，
     * @param user
     * @return {@link Result}
     */
    @Override
    public Result regist(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        //返回查到的行数
        Long count = userMapper.selectCount(lambdaQueryWrapper);
        //查到用户，返回失败，注册用户占用
        if(count>0){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        userMapper.insert(user);
        return  Result.ok(null);

    }
}




