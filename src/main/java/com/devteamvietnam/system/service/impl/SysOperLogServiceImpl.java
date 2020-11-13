package com.devteamvietnam.system.service.impl;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.system.domain.SysOperLog;
import com.devteamvietnam.system.mapper.SysOperLogMapper;
import com.devteamvietnam.system.service.ISysOperLogService;
/**
 * Operation log service layer processing
 *
 * @author ivan
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * Added operation log
     *
     * @param operLog operation log object
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * Query system operation log collection
     *
     * @param operLog operation log object
     * @return Operation log collection
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * Batch delete system operation logs
     *
     * @param operIds The ID of the operation log to be deleted
     * @return result
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * Query operation log details
     *
     * @param operId operation ID
     * @return Operation log object
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * Clear operation log
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
