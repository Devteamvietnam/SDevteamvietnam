package com.devteamvietnam.common.core.domain.model;

import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.devteamvietnam.common.core.domain.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Login user identity permissions
 *
 * @author Ivan
 */
public class LoginUser implements UserDetails
{
    private static final long serialVersionUID = 1L;

    /**
     * Unique user ID
     */
    private String token;

    /**
     * Log in time
     */
    private Long loginTime;

    /**
     * Expiration
     */
    private Long expireTime;

    /**
     * Login IP address
     */
    private String ipaddr;

    /**
     * Login location
     */
    private String loginLocation;

    /**
     * Browser type
     */
    private String browser;

    /**
     * operating system
     */
    private String os;

    /**
     * Permission list
     */
    private Set<String> permissions;

    /**
     * User Info
     */
    private SysUser user;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public LoginUser()
    {
    }

    public LoginUser(SysUser user, Set<String> permissions)
    {
        this.user = user;
        this.permissions = permissions;
    }

    @JsonIgnore
    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getUserName();
    }

    /**
     * Whether the account has not expired, it cannot be verified after expiration
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    /**
     * Specify whether the user is unlocked, and locked users cannot be authenticated
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    /**
     * Indicate whether the user's credentials (password) have expired, expired credentials prevent authentication
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    /**
     * Is it available, disabled users cannot be authenticated
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isEnabled()
    {
        return true;
    }
    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }

    public SysUser getUser()
    {
        return user;
    }

    public void setUser(SysUser user)
    {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }
}
