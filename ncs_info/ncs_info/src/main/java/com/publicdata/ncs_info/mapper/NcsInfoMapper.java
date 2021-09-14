package com.publicdata.ncs_info.mapper;

import com.publicdata.ncs_info.vo.NcsInfoVO;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NcsInfoMapper {
    public void insertNcsInfo(NcsInfoVO vo);
}
