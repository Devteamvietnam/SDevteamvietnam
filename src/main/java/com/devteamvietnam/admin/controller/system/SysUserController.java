package com.devteamvietnam.admin.controller.system;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devteamvietnam.common.annotation.Log;
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.controller.BaseController;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.domain.entity.SysRole;
import com.devteamvietnam.common.core.domain.entity.SysUser;
import com.devteamvietnam.common.core.domain.model.LoginUser;
import com.devteamvietnam.common.core.page.TableDataInfo;
import com.devteamvietnam.common.enums.BusinessType;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.ServletUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.poi.ExcelUtil;
import com.devteamvietnam.framework.web.service.TokenService;
import com.devteamvietnam.system.service.ISysPostService;
import com.devteamvietnam.system.service.ISysRoleService;
import com.devteamvietnam.system.service.ISysUserService;

/**
 * User Info
 *
 * @author ivan
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get user list
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "User Management", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "User Data");
    }

    @Log(title = "User Management", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("User Data");
    }

    /**
     * Get detailed information according to user number
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId)? roles: roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }
    /**
     * New users
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "User Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return AjaxResult.error("New user'" + user.getUserName() + "'Failed, login account already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("New user'" + user.getUserName() + "'Failed, mobile phone number already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("New user'" + user.getUserName() + "'Failed, the email account already exists");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * Modify user
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "User Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("Modify user'" + user.getUserName() + "'Failed, mobile phone number already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("Modify user'" + user.getUserName() + "'Failed, the email account already exists");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * delete users
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "User Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * reset Password
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "User Management", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * Status modification
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "User Management", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }
}
