package com.devteamvietnam.common.exception.user;

/**
 * The user password is incorrect or does not meet the standard exception category
 *
 * @author Ivan
 */
public class UserPasswordNotMatchException extends UserException
{

	private static final long serialVersionUID = -1045563827608838229L;

	public UserPasswordNotMatchException()
    {
        super("user.password.not.match", null);
    }
}
