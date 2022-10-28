package com.wl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wl.entity.MyFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<MyFile> {
}
