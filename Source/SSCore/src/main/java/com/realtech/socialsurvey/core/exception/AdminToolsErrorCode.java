package com.realtech.socialsurvey.core.exception;

public class AdminToolsErrorCode implements ErrorCode
{
    private int errorCode;
    private int serviceId;
    private String message;


    public AdminToolsErrorCode( int errorCode, int serviceId, String message )
    {
        this.errorCode = errorCode;
        this.serviceId = serviceId;
        this.message = message;
    }


    @Override
    public int getErrorCode()
    {
        return errorCode;
    }


    @Override
    public int getServiceId()
    {
        return serviceId;
    }


    @Override
    public String getMessage()
    {
        return message;
    }
}