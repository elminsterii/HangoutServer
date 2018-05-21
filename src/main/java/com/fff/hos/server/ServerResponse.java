package com.fff.hos.server;

public class ServerResponse {

    private STATUS_CODE m_cdStatus;
    private Object m_objContent;

    public enum STATUS_CODE {
        ST_CODE_SUCCESS
        , ST_CODE_USER_NOT_FOUND
        , ST_CODE_USER_EXIST
        , ST_CODE_USER_INVALID
        , ST_CODE_ACTIVITY_NOT_FOUND
        , ST_CODE_MISSING_NECESSARY
        , ST_CODE_JSON_FORMAT_WRONG
        , ST_CODE_INVALID_DATA
        , ST_CODE_FILE_IO_ERROR
        , ST_CODE_FILE_NOT_FOUND
        , ST_CODE_COMMENT_NOT_FOUND
    }

    public ServerResponse() {

    }

    public ServerResponse(STATUS_CODE m_cdStatus, Object m_objContent) {
        this.m_cdStatus = m_cdStatus;
        this.m_objContent = m_objContent;
    }

    public STATUS_CODE getStatus() {
        return m_cdStatus;
    }

    void setStatus(STATUS_CODE m_cdStatus) {
        this.m_cdStatus = m_cdStatus;
    }

    public Object getContent() {
        return m_objContent;
    }

    void setContent(Object m_objContent) {
        this.m_objContent = m_objContent;
    }
}
