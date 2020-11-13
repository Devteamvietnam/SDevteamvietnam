package com.devteamvietnam.system.mapper;

import java.util.List;

import com.devteamvietnam.system.domain.SysPost;

/**
 * Position information data layer
 *
 * @author ivan
 */
public interface SysPostMapper
{
    /**
     * Query job data collection
     *
     * @param post job information
     * @return post data collection
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * Query all positions
     *
     * @return post list
     */
    public List<SysPost> selectPostAll();

    /**
     * Query job information through job ID
     *
     * @param postId post ID
     * @return role object information
     */
    public SysPost selectPostById(Long postId);

    /**
     * Get the list of post selection boxes according to the user ID
     *
     * @param userId user ID
     * @return Selected post ID list
     */
    public List<Integer> selectPostListByUserId(Long userId);

    /**
     * Query the post group that the user belongs to
     *
     * @param userName username
     * @return result
     */
    public List<SysPost> selectPostsByUserName(String userName);

    /**
     * Delete post information
     *
     * @param postId post ID
     * @return result
     */
    public int deletePostById(Long postId);

    /**
     * Delete job information in bulk
     *
     * @param postIds ID of the post to be deleted
     * @return result
     */
    public int deletePostByIds(Long[] postIds);

    /**
     * Modify post information
     *
     * @param post job information
     * @return result
     */
    public int updatePost(SysPost post);

    /**
     * New job information
     *
     * @param post job information
     * @return result
     */
    public int insertPost(SysPost post);

    /**
     * Check post name
     *
     * @param postName post name
     * @return result
     */
    public SysPost checkPostNameUnique(String postName);

    /**
     * Check post code
     *
     * @param postCode post code
     * @return result
     */
    public SysPost checkPostCodeUnique(String postCode);
}
