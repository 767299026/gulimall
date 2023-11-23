package com.lsl.gulimall.product.controller;

import com.lsl.common.utils.PageUtils;
import com.lsl.common.utils.R;
import com.lsl.gulimall.product.dto.AttrGroupRelationDTO;
import com.lsl.gulimall.product.entity.AttrEntity;
import com.lsl.gulimall.product.entity.AttrGroupEntity;
import com.lsl.gulimall.product.service.AttrAttrgroupRelationService;
import com.lsl.gulimall.product.service.AttrGroupService;
import com.lsl.gulimall.product.service.AttrService;
import com.lsl.gulimall.product.service.CategoryService;
import com.lsl.gulimall.product.vo.AttrGroupWithAttrsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-08-01 17:39:46
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrsVO> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data", vos);
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationDTO[] dtos) {
        attrService.deleteRelation(dtos);
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @GetMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@PathVariable("catelogId") Long catelogId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrGroupService.queryPage(catelogId, params);
        return R.ok().put("page", page);
    }

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationDTO> dtos) {
        relationService.saveBatch(dtos);
        return R.ok();

    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
