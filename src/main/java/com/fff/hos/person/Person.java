package com.fff.hos.person;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("name")
    private String m_strName;

    public Person(String m_strName) {
        this.m_strName = m_strName;
    }

    public String getName() {
        return m_strName;
    }

    public void setName(String m_strName) {
        this.m_strName = m_strName;
    }
}
