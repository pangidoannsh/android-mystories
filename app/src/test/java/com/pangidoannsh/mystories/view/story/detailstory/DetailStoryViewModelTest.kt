package com.pangidoannsh.mystories.view.story.detailstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.pangidoannsh.mystories.MainDispatcherRule
import com.pangidoannsh.mystories.data.repository.FavoriteRepository
import com.pangidoannsh.mystories.utils.DataDummy
import com.pangidoannsh.mystories.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var detailStoryViewModel: DetailStoryViewModel
    private val dummyStory = DataDummy.generateDummyFavorite("story-dummy")

    @Before
    fun setUp() {
        detailStoryViewModel = DetailStoryViewModel(favoriteRepository)
        detailStoryViewModel.initStoryData(dummyStory)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when isFavorite false Should call addFavorite`() = runTest {
        val exceptBoolean = MutableLiveData<Boolean>()
        exceptBoolean.value = false
        `when`(favoriteRepository.isStoryFavorite(dummyStory.id)).thenReturn(exceptBoolean)
        detailStoryViewModel.isFavorite.getOrAwaitValue()

        detailStoryViewModel.changeFavoriteStatus()

        Mockito.verify(favoriteRepository).addFavorite(dummyStory)
    }

    @Test
    fun `when isFavorite true Should call deleteFromFavorite`() = runTest {
        val exceptBoolean = MutableLiveData<Boolean>()
        exceptBoolean.value = true
        `when`(favoriteRepository.isStoryFavorite(dummyStory.id)).thenReturn(exceptBoolean)
        detailStoryViewModel.isFavorite.getOrAwaitValue()

        detailStoryViewModel.changeFavoriteStatus()

        Mockito.verify(favoriteRepository).deleteFromFavorite(dummyStory.id)
    }
}