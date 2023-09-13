package com.lsl.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsl.common.utils.PageUtils;
import com.lsl.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author YIQU
 * @email YIQU@gmail.com
 * @date 2023-09-13 14:16:49
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

