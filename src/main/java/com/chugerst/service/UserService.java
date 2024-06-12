package com.chugerst.service;

import com.chugerst.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chugerst.util.Result;

/**
* @author MIKU
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-04-09 21:41:38
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);
}
