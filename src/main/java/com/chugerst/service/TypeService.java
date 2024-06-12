package com.chugerst.service;

import com.chugerst.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chugerst.util.Result;

/**
* @author MIKU
* @description 针对表【news_type】的数据库操作Service
* @createDate 2024-04-09 21:41:38
*/
public interface TypeService extends IService<Type> {

    Result findAllTypes();

}
