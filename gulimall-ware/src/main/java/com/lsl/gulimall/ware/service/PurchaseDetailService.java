package com.lsl.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsl.common.utils.PageUtils;
import com.lsl.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-09-13 14:29:53
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

