package com.example.donappt5.data.services

import com.example.donappt5.data.services.FirestoreService.getCharityData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith

internal class FirestoreServiceTest {


    @Test
    fun testGivenUnauthenticatedUser_WhenGettingDocuments_ThenReturnNull() {
        val db = mockk<FirebaseFirestore>()
        val user = mockk<FirebaseUser>()
        val authInstance = mockk<FirebaseAuth>()
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns authInstance
        every { authInstance.currentUser } returns user
        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns db
        every { db.collection("users") } throws FirebaseFirestoreException("User unauthenticated", FirebaseFirestoreException.Code.UNAUTHENTICATED)
        assertNull(getCharityData("1JIa7wFRMHZo74uBdSsGw7T02mUr"))
    }

    @org.junit.jupiter.api.Test
    fun getCharityList() {
    }

    @org.junit.jupiter.api.Test
    fun fillFavoritesData() {
    }

    @org.junit.jupiter.api.Test
    fun getPreferences() {
    }

    @org.junit.jupiter.api.Test
    fun setPreferences() {
    }

    @org.junit.jupiter.api.Test
    fun getOwnedCharities() {
    }

    @org.junit.jupiter.api.Test
    fun getUserData() {
    }

    @org.junit.jupiter.api.Test
    fun uploadImage() {
    }

    @org.junit.jupiter.api.Test
    fun loadFav() {
    }

    @org.junit.jupiter.api.Test
    fun removeFav() {
    }

    @org.junit.jupiter.api.Test
    fun addFav() {
    }

    @org.junit.jupiter.api.Test
    fun getUser() {
    }

    @org.junit.jupiter.api.Test
    fun setCharityLocation() {
    }

    @org.junit.jupiter.api.Test
    fun setUser() {
    }

    @org.junit.jupiter.api.Test
    fun putTags() {
    }

    @org.junit.jupiter.api.Test
    fun uploadCharityImage() {
    }

    @org.junit.jupiter.api.Test
    fun getImageUrl() {
    }

    @org.junit.jupiter.api.Test
    fun editCharity() {
    }

    @org.junit.jupiter.api.Test
    fun deleteCharity() {
    }

    @org.junit.jupiter.api.Test
    fun checkName() {
    }
}