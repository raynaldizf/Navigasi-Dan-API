package com.example.navigasidanapi

import com.example.navigasidanapi.room.FavDatabase
import com.example.navigasidanapi.room.Favorite
import com.example.navigasidanapi.room.FavoriteDao
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Test


class FavoriteDaoTest {


    private lateinit var dao: FavoriteDao
    private lateinit var database: FavDatabase

    @Before
    fun setup() {
        database = mockk()
        dao = mockk()
        every { database.favDao() } returns dao
    }

    @Test
    fun insertFavorite() {
        val mockFavorite = mockk<Favorite>()
        every { mockFavorite.username } returns "Test User"
        every { mockFavorite.avatar_url } returns "test_avatar_url"
        every { mockFavorite.is_favorite } returns 1

        coEvery { dao.insertFavs(mockFavorite) } returns 1

        val result = runBlocking { dao.insertFavs(mockFavorite) }

        assertThat(result, equalTo(1))
    }
}