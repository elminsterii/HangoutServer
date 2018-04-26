package com.fff.hos.person;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("account")
    private String m_strAccount;

    @SerializedName("name")
    private String m_strName;

    public Person(String m_strAccount, String m_strName) {
        this.m_strAccount = m_strAccount;
        this.m_strName = m_strName;
    }

    public String getAccount() {
        return m_strAccount;
    }

    public void setAccount(String m_strAccount) {
        this.m_strAccount = m_strAccount;
    }

    public String getName() {
        return m_strName;
    }

    public void setName(String m_strName) {
        this.m_strName = m_strName;
    }
}
