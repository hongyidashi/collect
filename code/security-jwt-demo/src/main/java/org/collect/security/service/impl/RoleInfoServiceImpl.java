package org.collect.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.collect.security.entity.RoleInfo;
import org.collect.security.mapper.RoleInfoMapper;
import org.collect.security.service.RoleInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-01-18
 */
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements RoleInfoService {

    @Override
    public List<RoleInfo> listRoleByUserId(Long userId) {
        return super.baseMapper.listRoleByUserId(userId);
    }
}
