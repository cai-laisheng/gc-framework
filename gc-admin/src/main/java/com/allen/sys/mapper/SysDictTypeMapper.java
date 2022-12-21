package com.allen.sys.mapper;

import com.allen.sys.model.po.SysDictType;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    List<SysDictType> findList(@Param("typeCode")String typeCode,@Param("typeName")String typeName);

}