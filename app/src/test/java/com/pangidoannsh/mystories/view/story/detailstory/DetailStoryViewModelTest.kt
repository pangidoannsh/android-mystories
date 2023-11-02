package com.pangidoannsh.mystories.view.story.detailstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.pangidoannsh.mystories.MainDispatcherRule
import com.pangidoannsh.mystories.data.repository.FavoriteRepository
import com.pangidoannsh.mystories.view.favorite.FavoriteStoriesViewModel
import com.pangidoannsh.mystories.view.utils.DataDummy
import com.pangidoannsh.mystories.view.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var favStoriesRepo: FavoriteRepository
    private lateinit var detailStoryViewModel: DetailStoryViewModel
    private val dummyStory = DataDummy.generateFavoriteStory()

    @Before
    fun setup() {
        detailStoryViewModel = DetailStoryViewModel(favStoriesRepo)
        detailStoryViewModel.initStoryData(dummyStory)
    }

    @Test
    fun `when isFavoriteStory false Should call addFavorite`() = runTest {
        val expectedBoolean = MutableLiveData<Boolean>()
        expectedBoolean.value = false
        Mockito.`when`(favStoriesRepo.isFavoriteStory(dummyStory.id)).thenReturn(expectedBoolean)
        detailStoryViewModel.isFavoriteStory.getOrAwaitValue()

        detailStoryViewModel.changeStatusFavorite()

        Mockito.verify(favStoriesRepo).addFavorite(dummyStory)
    }

    @Test
    fun `when isFavoriteStory true Should call deleteFromFavorite`() = runTest {
        val expectedBoolean = MutableLiveData<Boolean>()
        expectedBoolean.value = true
        Mockito.`when`(favStoriesRepo.isFavoriteStory(dummyStory.id)).thenReturn(expectedBoolean)
        detailStoryViewModel.isFavoriteStory.getOrAwaitValue()

        detailStoryViewModel.changeStatusFavorite()

        Mockito.verify(favStoriesRepo).deleteFromFavorite(dummyStory.id)
    }
}