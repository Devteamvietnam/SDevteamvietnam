package com.devteamvietnam.admin.controller.system;

import java.util.List;
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

import com.devteamvietnam.common.annotation.Log;
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.controller.BaseController;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.page.TableDataInfo;
import com.devteamvietnam.common.enums.BusinessType;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.poi.ExcelUtil;
import com.devteamvietnam.system.domain.SysPost;
import com.devteamvietnam.system.service.ISysPostService;

/**
 * Post information operation processing
 *
 * @author ivan
 */
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController
{
    @Autowired
    private ISysPostService postService;

    /**
     * Get job list
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPost post)
    {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }
    
    @Log(title = "Job Management", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @GetMapping("/export")
    public AjaxResult export(SysPost post)
    {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "Post Data");
    }

    /**
     * Get detailed information according to the job number
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable Long postId)
    {
        return AjaxResult.success(postService.selectPostById(postId));
    }

    /**
     * New posts
     */
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "Job Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysPost post)
    {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return AjaxResult.error("New post'" + post.getPostName() + "'Failed, post name already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return AjaxResult.error("New post'" + post.getPostName() + "'Failed, post code already exists");
        }
        post.setCreateBy(SecurityUtils.getUsername());
        return toAjax(postService.insertPost(post));
    }

    /**
     * Modify position
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "Job Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysPost post)
    {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return AjaxResult.error("Modify post'" + post.getPostName() + "'Failed, post name already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return AjaxResult.error("Modify post'" + post.getPostName() + "'Failed, post code already exists");
        }
        post.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(postService.updatePost(post));
    }

    /**
     * Delete post
     */
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "Job Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds)
    {
        return toAjax(postService.deletePostByIds(postIds));
    }

    /**
     * Get the list of post selection boxes
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<SysPost> posts = postService.selectPostAll();
        return AjaxResult.success(posts);
    }
}
