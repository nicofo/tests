/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Error;

import com.crumbits.Utilities.Utilities;

/**
 *
 * @author Miquel Ferriol
 */
public class Errors {
    private Utilities u = new Utilities();

    /**
     *
     */
    public ErrorStatus createError = u.createError("username/mail already exists","username/mail already exists",409);

    public ErrorStatus fbExistsError = u.createError("fb login error","this account has another facebook acc. associated",409);

    /**
     *
     */
    public ErrorStatus existsError = u.createError("mail doesn't exist","mail doesn't exist",409);

    /**
     *
     */
    public ErrorStatus idError = u.createError("error accessing BD","given id's are not correct or don't exist",404);

    /**
     *
     */
    public ErrorStatus loginError = u.createError("login error","mail/user and password don't match",401);

    /**
     *
     */
    public ErrorStatus mailError = u.createError("mail already exists or is incorrect","mail already exists or is incorrect",409);

    /**
     *
     */
    public ErrorStatus otherError = u.createError("internal error","unknown error has ocurred",500);

    /**
     *
     */
    public ErrorStatus passwordError = u.createError("invalid password", "password is not strong or contains invalid characters",412);

    /**
     *
     */
    public ErrorStatus tokenError = u.createError("token authentication error","This token is invalid or has expired",403);

    /**
     *
     */
    public ErrorStatus userError = u.createError("username already exists","username already exists",409);

    /**
     *
     */
    public ErrorStatus userValError = u.createError("username/mail doesn't exist","username/mail doesn't exist",401);

    /**
     *
     */
    public ErrorStatus facebookError = u.createError("facebook login error","fbId and mail doesn't match",401);

    /**
     *
     */
    public ErrorStatus argumentError = u.createError("argument exception","some argument is null or it hasn't the good format",500);
    public ErrorStatus permissionDenied = u.createError("permission denied","This user doesn't have suficient permissions to delete this crumb",401);

}
