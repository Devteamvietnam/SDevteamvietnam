package com.devteamvietnam.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.system.domain.SysNotice;
import com.devteamvietnam.system.mapper.SysNoticeMapper;
import com.devteamvietnam.system.service.ISysNoticeService;

/**
 * Announcement service layer implementation
 *
 * @author ivan
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private SysNoticeMapper noticeMapper;

    /**
     * Query announcement information
     *
     * @param noticeId notice ID
     * @return announcement information
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * Check the announcement list
     *
     * @param notice announcement information
     * @return announcement collection
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * New announcement
     *
     * @param notice announcement information
     * @return result
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
        return noticeMapper.insertNotice(notice);
    }

    /**
     * Modification announcement
     *
     * @param notice announcement information
     * @return result
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * Delete the announcement object
     *
     * @param noticeId notice ID
     * @return result
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        return noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * Delete announcement information in bulk
     *
     * @param noticeIds The ID of the notice to be deleted
     * @return result
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }
}
