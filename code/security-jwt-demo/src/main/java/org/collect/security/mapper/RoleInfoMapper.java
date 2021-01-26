package org.collect.security.mapper;

import org.collect.security.entity.RoleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-01-18
 */
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    List<RoleInfo> listRoleByUserId(Long userId);
}
