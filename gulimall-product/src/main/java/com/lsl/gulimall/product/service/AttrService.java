package com.lsl.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsl.common.utils.PageUtils;
import com.lsl.gulimall.product.dto.AttrGroupRelationDTO;
import com.lsl.gulimall.product.entity.AttrEntity;
import com.lsl.gulimall.product.vo.AttrRespVO;
import com.lsl.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-08-01 17:39:46
 */
public interface AttrService extends IService<AttrEntity> {

    /**
     * 普通分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存商品属性
     * @param attr
     */
    void saveAttr(AttrVo attr);

    /**
     * 分页查询商品属性
     *
     * @param params
     * @param catelogId
     * @param attrType
     * @return
     */
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType);

    AttrRespVO getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    /**
     * 根据分组id查找关联的所有属性
     *
     * @param attrgroupId
     * @return
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationDTO[] dtos);

    /**
     * 获取当前分组没有关联的所有属性
     *
     * @param params
     * @param attrgroupId
     * @return
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}

