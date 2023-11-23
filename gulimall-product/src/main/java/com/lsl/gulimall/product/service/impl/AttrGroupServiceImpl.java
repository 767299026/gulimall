package com.lsl.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsl.common.utils.PageUtils;
import com.lsl.common.utils.Query;
import com.lsl.gulimall.product.dao.AttrGroupDao;
import com.lsl.gulimall.product.entity.AttrEntity;
import com.lsl.gulimall.product.entity.AttrGroupEntity;
import com.lsl.gulimall.product.service.AttrGroupService;
import com.lsl.gulimall.product.service.AttrService;
import com.lsl.gulimall.product.vo.AttrGroupWithAttrsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Long catelogId, Map<String, Object> params) {
        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        // 判断是否携带查询参数
        if(!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> obj.eq(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName,key));
        }
        //三级分类Id是否为0
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<>());
            return new PageUtils(page);
        } else {
            wrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }

    /**
     * 根据分类id查出所有的分组以及这些组里面的属性*
     *
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        LambdaQueryWrapper<AttrGroupEntity> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(AttrGroupEntity::getCatelogId, catelogId);
        List<AttrGroupEntity> attrGroupEntities = this.list(qw1);
        List<AttrGroupWithAttrsVO> vos = attrGroupEntities.stream().map(item -> {
            AttrGroupWithAttrsVO vo = new AttrGroupWithAttrsVO();
            BeanUtils.copyProperties(item, vo);
            List<AttrEntity> attrs = attrService.getRelationAttr(vo.getAttrGroupId());
            vo.setAttrs(attrs);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }


}