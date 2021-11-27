package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

class TestDto {
    //Data transfer object
    @SerializedName("body")
    private var body: String?=" "
    @SerializedName("id")
    private var id: Int?=0
    @SerializedName("title")
    private var title: String?=" "
    @SerializedName("userId")
    private var userId: Int?=0


    override fun toString(): String {
        return "PostResult{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}