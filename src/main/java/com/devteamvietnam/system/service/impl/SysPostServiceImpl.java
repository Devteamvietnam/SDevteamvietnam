package com.devteamvietnam.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.domain.SysPost;
import com.devteamvietnam.system.mapper.SysPostMapper;
import com.devteamvietnam.system.mapper.SysUserPostMapper;
import com.devteamvietnam.system.service.ISysPostService;
/**
 * Post information service layer processing
 *
 * @author ivan
 */
@Service
public class SysPostServiceImpl implements ISysPostService
{
    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    /**
     * Query job information collection
     *
     * @param post job information
     * @return Post information collection
     */
    @Override
    public List<SysPost> selectPostList(SysPost post)
    {
        return postMapper.selectPostList(post);
    }

    /**
     * Query all positions
     *
     * @return post list
     */
    @Override
    public List<SysPost> selectPostAll()
    {
        return postMapper.selectPostAll();
    }

    /**
     * Query job information through job ID
     *
     * @param postId post ID
     * @return role object information
     */
    @Override
    public SysPost selectPostById(Long postId)
    {
        return postMapper.selectPostById(postId);
    }

    /**
     * Get the list of post selection boxes based on user ID
     *
     * @param userId user ID
     * @return Selected post ID list
     */
    @Override
    public List<Integer> selectPostListByUserId(Long userId)
    {
        return postMapper.selectPostListByUserId(userId);
    }

    /**
     * Verify that the job name is unique
     *
     * @param post job information
     * @return result
     */
    @Override
    public String checkPostNameUnique(SysPost post)
    {
        Long postId = StringUtils.isNull(post.getPostId())? -1L: post.getPostId();
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Check whether the post code is unique
     *
     * @param post job information
     * @return result
     */
    @Override
    public String checkPostCodeUnique(SysPost post)
    {
        Long postId = StringUtils.isNull(post.getPostId())? -1L: post.getPostId();
        SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Query the number of positions used by the position ID
     *
     * @param postId post ID
     * @return result
     */
    @Override
    public int countUserPostById(Long postId)
    {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * Delete post information
     *
     * @param postId post ID
     * @return result
     */
    @Override
    public int deletePostById(Long postId)
    {
        return postMapper.deletePostById(postId);
    }

    /**
     * Delete job information in bulk
     *
     * @param postIds ID of the post to be deleted
     * @return result
     * @throws Exception
     */
    @Override
    public int deletePostByIds(Long[] postIds)
    {
        for (Long postId: postIds)
        {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId)> 0)
            {
                throw new CustomException(String.format("%1$s has been allocated and cannot be deleted", post.getPostName()));
            }
        }
        return postMapper.deletePostByIds(postIds);
    }

    /**
     * Added save job information
     *
     * @param post job information
     * @return result
     */
    @Override
    public int insertPost(SysPost post)
    {
        return postMapper.insertPost(post);
    }

    /**
     * Modify and save post information
     *
     * @param post job information
     * @return result
     */
    @Override
    public int updatePost(SysPost post)
    {
        return postMapper.updatePost(post);
    }
}
