package org.collect.security.service;

import org.collect.security.entity.RoleInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-01-18
 */
public interface RoleInfoService extends IService<RoleInfo>  {

    List<RoleInfo> listRoleByUserId(Long userId);
}
