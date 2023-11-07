package com.lsl.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsl.common.constant.ProductConstant;
import com.lsl.common.utils.PageUtils;
import com.lsl.common.utils.Query;
import com.lsl.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.lsl.gulimall.product.dao.AttrDao;
import com.lsl.gulimall.product.dao.AttrGroupDao;
import com.lsl.gulimall.product.dao.CategoryDao;
import com.lsl.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.lsl.gulimall.product.entity.AttrEntity;
import com.lsl.gulimall.product.entity.AttrGroupEntity;
import com.lsl.gulimall.product.entity.CategoryEntity;
import com.lsl.gulimall.product.service.AttrService;
import com.lsl.gulimall.product.service.CategoryService;
import com.lsl.gulimall.product.vo.AttrRespVO;
import com.lsl.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存商品属性
     * @param attr
     */
    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        //1.保存基本数据
        this.save(attrEntity);
        //2.保存关联关系
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public AttrRespVO getAttrInfo(Long attrId) {
        AttrRespVO respVO = new AttrRespVO();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,respVO);

        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AttrAttrgroupRelationEntity::getAttrId,attrId);
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(queryWrapper);
            if(Objects.nonNull(relationEntity)) {
                respVO.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (Objects.nonNull(attrGroupEntity)) {
                    respVO.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        respVO.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if(Objects.nonNull(categoryEntity)){
            respVO.setCatelogName(categoryEntity.getName());
        }
        return respVO;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AttrAttrgroupRelationEntity::getAttrId,attr.getAttrId());
            Integer count = attrAttrgroupRelationDao.selectCount(queryWrapper);
            if(count > 0){
                LambdaUpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(AttrAttrgroupRelationEntity::getAttrId,attr.getAttrId());
                attrAttrgroupRelationDao.update(relationEntity,updateWrapper);
            }else {
                attrAttrgroupRelationDao.insert(relationEntity);
            }
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        return null;
    }

    /**
     * 分页查询商品属性
     *
     * @param params
     * @param catelogId
     * @param attrType
     * @return
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType) {
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrEntity::getAttrType,"base".equalsIgnoreCase(attrType) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        //当catelogId为0时，查询全部
        if(!catelogId.equals(0L)) {
            wrapper.eq(AttrEntity::getCatelogId,catelogId);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)) {
            //attr_id attr_name
            wrapper.and((obj) -> {
                obj.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName,key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVO> respVOList = records.stream().map((attrEntity -> {
            AttrRespVO respVO = new AttrRespVO();
            BeanUtils.copyProperties(attrEntity, respVO);
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId());
            if("base".equalsIgnoreCase(attrType)){
                AttrAttrgroupRelationEntity attrId = attrAttrgroupRelationDao.selectOne(queryWrapper);
                if (Objects.nonNull(attrId)) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    respVO.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (Objects.nonNull(categoryEntity)) {
                respVO.setCatelogName(categoryEntity.getName());
            }
            return respVO;
        })).collect(Collectors.toList());
        pageUtils.setList(respVOList);
        return pageUtils;
    }

    /**
     * 普通分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

}