package com.devteamvietnam.system.service;

import java.util.List;

import com.devteamvietnam.system.domain.SysPost;

/**
 * Job information service layer
 *
 * @author ivan
 */
public interface ISysPostService
{
    /**
     * Query job information collection
     *
     * @param post job information
     * @return post list
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
     * Check post name
     *
     * @param post job information
     * @return result
     */
    public String checkPostNameUnique(SysPost post);

    /**
     * Check post code
     *
     * @param post job information
     * @return result
     */
    public String checkPostCodeUnique(SysPost post);

    /**
     * Query the number of positions used by the position ID
     *
     * @param postId post ID
     * @return result
     */
    public int countUserPostById(Long postId);

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
     * @throws Exception
     */
    public int deletePostByIds(Long[] postIds);

    /**
     * Added save job information
     *
     * @param post job information
     * @return result
     */
    public int insertPost(SysPost post);

    /**
     * Modify and save post information
     *
     * @param post job information
     * @return result
     */
    public int updatePost(SysPost post);
}
