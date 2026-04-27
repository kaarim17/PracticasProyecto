package com.example.controldealmacen.ui

object SessionManager {
    var currentUserId: Int = -1
    var currentUserRol: String = ""
    var currentUserName: String = ""
    var isAdminAuthenticated: Boolean = false

    fun clearSession() {
        currentUserId = -1
        currentUserRol = ""
        currentUserName = ""
        isAdminAuthenticated = false
    }
}
