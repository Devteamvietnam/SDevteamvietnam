package com.devteamvietnam.system.service;

import java.util.List;

import com.devteamvietnam.system.domain.SysNotice;

/**
 * Announcement Service Layer
 *
 * @author ivan
 */
public interface ISysNoticeService
{
    /**
     * Query announcement information
     *
     * @param noticeId notice ID
     * @return announcement information
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * Check the announcement list
     *
     * @param notice announcement information
     * @return announcement collection
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * New announcement
     *
     * @param notice announcement information
     * @return result
     */
    public int insertNotice(SysNotice notice);

    /**
     * Modification announcement
     *
     * @param notice announcement information
     * @return result
     */
    public int updateNotice(SysNotice notice);

    /**
     * Delete announcement information
     *
     * @param noticeId notice ID
     * @return result
     */
    public int deleteNoticeById(Long noticeId);
    
    /**
     * Delete announcement information in bulk
     *
     * @param noticeIds The ID of the notice to be deleted
     * @return result
     */
    public int deleteNoticeByIds(Long[] noticeIds);
}
