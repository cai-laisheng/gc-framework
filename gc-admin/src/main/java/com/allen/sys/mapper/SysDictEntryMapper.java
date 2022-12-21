package com.allen.sys.mapper;

import com.allen.sys.model.po.SysDictEntry;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysDictEntryMapper extends BaseMapper<SysDictEntry> {

    List<SysDictEntry> findList(@Param("typeCode")String typeCode,@Param("dictCode")String dictCode);

}