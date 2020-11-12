package com.devteamvietnam.common.exception.user;

/**
 * Verification code invalidation exception
 *
 * @author Ivan
 */
public class CaptchaExpireException extends UserException
{

	private static final long serialVersionUID = 1958885275665976347L;

	public CaptchaExpireException()
    {
        super("user.jcaptcha.expire", null);
    }
}
