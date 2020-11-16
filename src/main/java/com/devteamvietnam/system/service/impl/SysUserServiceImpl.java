package com.devteamvietnam.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devteamvietnam.common.annotation.DataScope;
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.domain.entity.SysRole;
import com.devteamvietnam.common.core.domain.entity.SysUser;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.domain.SysPost;
import com.devteamvietnam.system.domain.SysUserPost;
import com.devteamvietnam.system.domain.SysUserRole;
import com.devteamvietnam.system.mapper.SysPostMapper;
import com.devteamvietnam.system.mapper.SysRoleMapper;
import com.devteamvietnam.system.mapper.SysUserMapper;
import com.devteamvietnam.system.mapper.SysUserPostMapper;
import com.devteamvietnam.system.mapper.SysUserRoleMapper;
import com.devteamvietnam.system.service.ISysConfigService;
import com.devteamvietnam.system.service.ISysUserService;

/**
 * User business layer processing
 *
 * @author ivan
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    /**
     * Paging query user list according to conditions
     *
     * @param user user information
     * @return user information collection information
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * Query users by username
     *
     * @param userName username
     * @return user object information
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * Query users by user ID
     *
     * @param userId user ID
     * @return user object information
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * Query the role group the user belongs to
     *
     * @param userName username
     * @return result
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role: list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length()-1);
        }
        return idsStr.toString();
    }

    /**
     * Query the post group that the user belongs to
     *
     * @param userName username
     * @return result
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post: list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length()-1);
        }
        return idsStr.toString();
    }

    /**
     * Verify that the user name is unique
     *
     * @param userName user name
     * @return result
     */
    @Override
    public String checkUserNameUnique(String userName)
    {
        int count = userMapper.checkUserNameUnique(userName);
        if (count> 0)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Verify that the user name is unique
     *
     * @param user user information
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId())? -1L: user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Verify that the email is unique
     *
     * @param user user information
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId())? -1L: user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
    /**
     * Verify whether the user allows the operation
     *
     * @param user user information
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new CustomException("Super administrator user is not allowed to operate");
        }
    }

    /**
     * Added save user information
     *
     * @param user user information
     * @return result
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // Add user information
        int rows = userMapper.insertUser(user);
        // Add user post association
        insertUserPost(user);
        // Add user and role management
        insertUserRole(user);
        return rows;
    }

    /**
     * Modify and save user information
     *
     * @param user user information
     * @return result
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // Delete user and role association
        userRoleMapper.deleteUserRoleByUserId(userId);
        // Add user and role management
        insertUserRole(user);
        // Delete user and post association
        userPostMapper.deleteUserPostByUserId(userId);
        // Add user and position management
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * Modify user status
     *
     * @param user user information
     * @return result
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * Modify user basic information
     *
     * @param user user information
     * @return result
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * Modify user avatar
     *
     * @param userName username
     * @param avatar avatar address
     * @return result
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar)> 0;
    }

    /**
     * Reset user password
     *
     * @param user user information
     * @return result
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * Reset user password
     *
     * @param userName username
     * @param password password
     * @return result
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * Added user role information
     *
     * @param user user object
     */
    public void insertUserRole(SysUser user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // Add user and role management
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId: roles)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size()> 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * New user post information
     *
     * @param user user object
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // Add user and position management
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId: posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size()> 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }
    /**
     * Delete user by user ID
     *
     * @param userId user ID
     * @return result
     */
    @Override
    public int deleteUserById(Long userId)
    {
        // Delete user and role association
        userRoleMapper.deleteUserRoleByUserId(userId);
        // Delete user and position table
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * Delete user information in batch
     *
     * @param userIds The user ID to be deleted
     * @return result
     */
    @Override
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId: userIds)
        {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * Import user data
     *
     * @param userList user data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operation user
     * @return result
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new CustomException("The imported user data cannot be empty!");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user: userList)
        {
            try
            {
                // Verify if this user exists
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + ", account number "+ user.getUserName() + "import successfully");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + ", account number "+ user.getUserName() + "updated successfully");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + ", account "+ user.getUserName() + "existing");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + ", account "+ user.getUserName() +" Import failed:";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum> 0)
        {
            failureMsg.insert(0, "Sorry, the import failed! A total of "+ failureNum +" data format is incorrect, the error is as follows:");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "Congratulations, all the data has been successfully imported! There are "+ successNum +" items, the data is as follows:");
        }
        return successMsg.toString();
    }
}
