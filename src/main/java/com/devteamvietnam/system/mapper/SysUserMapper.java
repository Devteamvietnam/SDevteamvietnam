package com.devteamvietnam.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.devteamvietnam.common.core.domain.entity.SysUser;

/**
* User table data layer
*
* @author ivan
*/
public interface SysUserMapper
{
   /**
    * Paging query user list according to conditions
    *
    * @param sysUser user information
    * @return user information collection information
    */
   public List<SysUser> selectUserList(SysUser sysUser);

   /**
    * Query users by username
    *
    * @param userName username
    * @return user object information
    */
   public SysUser selectUserByUserName(String userName);

   /**
    * Query users by user ID
    *
    * @param userId user ID
    * @return user object information
    */
   public SysUser selectUserById(Long userId);

   /**
    * Add user information
    *
    * @param user user information
    * @return result
    */
   public int insertUser(SysUser user);

   /**
    * Modify user information
    *
    * @param user user information
    * @return result
    */
   public int updateUser(SysUser user);

   /**
    * Modify user avatar
    *
    * @param userName username
    * @param avatar avatar address
    * @return result
    */
   public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

   /**
    * Reset user password
    *
    * @param userName username
    * @param password password
    * @return result
    */
   public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

   /**
    * Delete user by user ID
    *
    * @param userId user ID
    * @return result
    */
   public int deleteUserById(Long userId);

   /**
    * Delete user information in batch
    *
    * @param userIds The user ID to be deleted
    * @return result
    */
   public int deleteUserByIds(Long[] userIds);

   /**
    * Verify that the user name is unique
    *
    * @param userName user name
    * @return result
    */
   public int checkUserNameUnique(String userName);

   /**
    * Verify that the mobile phone number is unique
    *
    * @param phonenumber phone number
    * @return result
    */
   public SysUser checkPhoneUnique(String phonenumber);

   /**
    * Verify that the email is unique
    *
    * @param email user mailbox
    * @return result
    */
   public SysUser checkEmailUnique(String email);
}
