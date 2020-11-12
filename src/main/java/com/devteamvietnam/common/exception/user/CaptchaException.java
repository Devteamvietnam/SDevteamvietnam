package com.devteamvietnam.common.exception.user;

/**
 * Verification code error exception category
 *
 * @author Ivan
 */
public class CaptchaException extends UserException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7579439036985154959L;

	public CaptchaException()
    {
        super("user.jcaptcha.error", null);
    }
}