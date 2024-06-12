package com.chugerst.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chugerst.pojo.Type;
import com.chugerst.service.TypeService;
import com.chugerst.mapper.TypeMapper;
import com.chugerst.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
* @author MIKU
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-04-09 21:41:38
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{
    @Autowired
    private TypeMapper typeMapper;

    /**
     * 查询首页分类
     * 查询所有返回
     * @return {@link Result}
     */
    @Override
    public Result findAllTypes() {
        List<Type> types = typeMapper.selectList(null);
        return Result.ok(types);
    }
}




