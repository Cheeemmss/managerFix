package com.wl.mapper;

import com.wl.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 24509
* @description 针对表【sys_dict】的数据库操作Mapper
* @createDate 2022-09-22 11:05:35
* @Entity com.wl.entity.Dict
*/
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

}




