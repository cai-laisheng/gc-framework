package com.allen.sys.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Author xuguocai
 * @Date 20:18 2022/12/21
 **/
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
