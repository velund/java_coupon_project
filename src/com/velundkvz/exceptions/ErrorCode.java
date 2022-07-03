package com.velundkvz.exceptions;
public enum ErrorCode
{
    ERR_ADD_OR_UPDATE_FK1(1216),
    ERR_ADD_OR_UPDATE_FK2(1452);
    private int code;
    private ErrorCode(int code){ this.code = code; }
    public int code() {return code;}
    public static boolean isAddOrUpdateFk(int errCode)
    {
        return errCode == ERR_ADD_OR_UPDATE_FK1.code ||
                errCode == ERR_ADD_OR_UPDATE_FK2.code;
    }
}

