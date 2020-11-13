package com.devteamvietnam.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devteamvietnam.common.core.domain.entity.SysUser;
import com.devteamvietnam.common.core.domain.model.LoginUser;
import com.devteamvietnam.common.enums.UserStatus;
import com.devteamvietnam.common.exception.BaseException;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.service.ISysUserService;

/**
 * User verification processing
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("Login user: {} does not exist.", username);
            throw new UsernameNotFoundException("Login user:" + username + "does not exist");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("Login user: {} has been deleted.", username);
            throw new BaseException("Sorry, your account: "+ username +" has been deleted");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("Login user: {} has been disabled.", username);
            throw new BaseException("Sorry, your account:" + username + "disabled");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}