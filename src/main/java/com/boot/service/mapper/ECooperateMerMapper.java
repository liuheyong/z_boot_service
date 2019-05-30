package com.boot.service.mapper;

import com.boot.commons.dto.ECooperateMer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ECooperateMerMapper {

    /**
     * @date: 2019/5/24
     * @param: [eCooperateMer]
     * @return: com.boot.commons.dto.ECooperateMer
     * @description: 详情
     */
    ECooperateMer selectECooperateMerInfo(ECooperateMer eCooperateMer);

    /**
     * @date: 2019/5/24
     * @param: [eCooperateMer]
     * @return: com.boot.commons.dto.ECooperateMer
     * @description: 列表
     */
    List<ECooperateMer> queryECooperateMerListPage();
}