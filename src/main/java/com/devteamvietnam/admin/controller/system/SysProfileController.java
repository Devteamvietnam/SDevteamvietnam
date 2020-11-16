package com.devteamvietnam.admin.controller.system;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devteamvietnam.common.annotation.Log;
import com.devteamvietnam.common.config.DevteamConfig;
import com.devteamvietnam.common.core.controller.BaseController;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.domain.entity.SysUser;
import com.devteamvietnam.common.core.domain.model.LoginUser;
import com.devteamvietnam.common.enums.BusinessType;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.ServletUtils;
import com.devteamvietnam.common.utils.file.FileUploadUtils;
import com.devteamvietnam.framework.web.service.TokenService;
import com.devteamvietnam.system.service.ISysUserService;

/**
 * Personal information business processing
 *
 * @author ivan
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * Personal information
     */
    @GetMapping
    public AjaxResult profile()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * Modify user
     */
    @Log(title = "Personal Information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        if (userService.updateUserProfile(user)> 0)
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // Update cached user information
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhonenumber(user.getPhonenumber());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("If you modify personal information abnormally, please contact the administrator");
    }

    /**
     * reset Password
     */
    @Log(title = "Personal Information", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return AjaxResult.error("Failed to modify the password, the old password is wrong");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return AjaxResult.error("The new password cannot be the same as the old password");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword))> 0)
        {
            // Update cached user password
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("Change the password is abnormal, please contact the administrator");
    }

    /**
     * Avatar upload
     */
    @Log(title = "User Avatar", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(DevteamConfig.getAvatarPath(), file);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // Update cached user avatar
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("Uploading picture is abnormal, please contact the administrator");
    }
}
