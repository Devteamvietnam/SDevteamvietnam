package com.devteamvietnam.system.domain;

import com.devteamvietnam.common.annotation.Excel;
import com.devteamvietnam.common.annotation.Excel.ColumnType;
import com.devteamvietnam.common.core.domain.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Parameter configuration table sys_config
 *
 * @author Ivan
 */
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Parameter primary key */
    @Excel(name = "parameter primary key", cellType = ColumnType.NUMERIC)
    private Long configId;

    /** parameter name */
    @Excel(name = "parameter name")
    private String configName;

    /** Parameter key name */
    @Excel(name = "Parameter key name")
    private String configKey;

    /** Parameter key value */
    @Excel(name = "Parameter key value")
    private String configValue;

    /** Built-in system (Y yes N no) */
    @Excel(name = "System built-in", readConverterExp = "Y=Yes, N=No")
    private String configType;

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    @NotBlank(message = "Parameter name cannot be empty")
    @Size(min = 0, max = 100, message = "The parameter name cannot exceed 100 characters")
    public String getConfigName()
    {
        return configName;
    }

    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    @NotBlank(message = "The length of the parameter key name cannot be empty")
    @Size(min = 0, max = 100, message = "The length of the parameter key name cannot exceed 100 characters")
    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    @NotBlank(message = "Parameter key value cannot be empty")
    @Size(min = 0, max = 500, message = "The length of the parameter key value cannot exceed 500 characters")
    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
