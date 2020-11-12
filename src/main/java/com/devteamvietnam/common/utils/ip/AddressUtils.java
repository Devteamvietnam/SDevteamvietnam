package com.devteamvietnam.common.utils.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.devteamvietnam.common.config.DevteamConfig;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.http.HttpUtils;

/**
 * Get address class
 *
 * @author Ivan
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP address query
    public static final String IP_URL = "https://api.myip.com";

    // unknown address
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        String address = UNKNOWN;
        // Intranet does not query
        if (IpUtils.internalIp(ip))
        {
            return "Intranet IP";
        }
        if (DevteamConfig.isAddressEnabled())
        {
            try
            {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error("Access to geographic location exception {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String country = obj.getString("country");
                String cc = obj.getString("cc");
                return String.format("%s %s", country, cc);
            }
            catch (Exception e)
            {
                log.error("Access to geographic location exception {}", ip);
            }
        }
        return address;
    }
}
