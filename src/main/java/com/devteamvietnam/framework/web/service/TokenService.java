package com.devteamvietnam.framework.web.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.core.domain.model.LoginUser;
import com.devteamvietnam.common.core.redis.RedisCache;
import com.devteamvietnam.common.utils.ServletUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.ip.AddressUtils;
import com.devteamvietnam.common.utils.ip.IpUtils;
import com.devteamvietnam.common.utils.uuid.IdUtils;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * Token verification processing
 */
@Component
public class TokenService
{
    // Token custom identification
    @Value("${token.header}")
    private String header;

    // token secret key
    @Value("${token.secret}")
    private String secret;

    // Token validity period (default 30 minutes)
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * Obtain user identity information
     *
     * @return user information
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // Get the token carried in the request
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            Claims claims = parseToken(token);
            // parse the corresponding permissions and user information
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            LoginUser user = redisCache.getCacheObject(userKey);
            return user;
        }
        return null;
    }

    /**
     * Set user identity information
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * Delete user identity information
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }
    /**
     * Create token
     *
     * @param loginUser user information
     * @return token
     */
    public String createToken(LoginUser loginUser)
    {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * Verification token validity period, if the difference is less than 20 minutes, the cache is automatically refreshed
     *
     * @param loginUser
     * @return token
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime-currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * Refresh token validity period
     *
     * @param loginUser login information
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // Cache loginUser according to uuid
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * Set user agent information
     *
     * @param loginUser login information
     */
    public void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * Generate token from data claim
     *
     * @param claims data declaration
     * @return token
     */
    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * Obtain data claims from the token
     *
     * @param token token
     * @return data declaration
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get the username from the token
     *
     * @param token token
     * @return username
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * Get the request token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid)
    {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }
}

