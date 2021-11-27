package com.example.appdid.dto

import com.google.gson.annotations.SerializedName

data class UserInfoDTO(
        @SerializedName("_id")
        val _id: String,
        @SerializedName("groups")
        val userGroupDTOS: List<UserGroupDTO>,
        @SerializedName("name")
        val name: String,
        @SerializedName("email")
        val email: String


) {
        override fun toString(): String {
                return  " ${this._id}\n ${this.name}\n ${this.userGroupDTOS}\n ${this.email}\n"
        }
}