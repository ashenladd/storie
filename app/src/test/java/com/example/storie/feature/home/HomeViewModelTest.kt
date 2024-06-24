package com.example.storie.feature.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storie.DummyData
import com.example.storie.MainDispatcherRule
import com.example.storie.core.DataStoreManager
import com.example.storie.data.local.entity.StoryEntity
import com.example.storie.domain.repository.AppRepository
import com.example.storie.feature.home.adapter.StoriesPagingAdapter
import com.example.storie.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var appRepository: AppRepository

    @Mock
    private lateinit var dataStoreManager: DataStoreManager

    @Test
    fun `when Get Stories Should Not Null and Return Data`() =
        runTest {
            val dummyStory = DummyData.generateDummyStoriesResponse()
            val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
            val expectedStories = MutableLiveData<PagingData<StoryEntity>>()
            expectedStories.value = data
            Mockito.`when`(appRepository.getStoriesPaging()).thenReturn(expectedStories)

            val homeViewModel = HomeViewModel(appRepository, dataStoreManager)
            val actualStory: PagingData<StoryEntity> = homeViewModel.storiesPagingData.getOrAwaitValue()

            val differ =
                AsyncPagingDataDiffer(
                    diffCallback = StoriesPagingAdapter.DIFF_CALLBACK,
                    updateCallback = noopListUpdateCallback,
                    workerDispatcher = Dispatchers.Main,
                )
            differ.submitData(actualStory)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dummyStory.size, differ.snapshot().size)
            Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
        }

    @SuppressLint("CheckResult")
    @Test
    fun `when Get Stories Should Not Null and Return No Data`() =
        runTest {
            val data: PagingData<StoryEntity> = PagingData.from(emptyList())
            val expectedStories =   MutableLiveData<PagingData<StoryEntity>>()
            expectedStories.value = data
            Mockito.mockStatic(Log::class.java)
            Mockito.`when`(appRepository.getStoriesPaging()).thenReturn(expectedStories)
            val mainViewModel = HomeViewModel(appRepository, dataStoreManager)
            val actualStories: PagingData<StoryEntity> = mainViewModel.storiesPagingData.getOrAwaitValue()
            val differ =
                AsyncPagingDataDiffer(
                    diffCallback = StoriesPagingAdapter.DIFF_CALLBACK,
                    updateCallback = noopListUpdateCallback,
                    workerDispatcher = Dispatchers.Main,
                )
            differ.submitData(actualStories)
            Assert.assertEquals(0, differ.snapshot().size)
        }

    class StoryPagingSource : PagingSource<Int, Flow<List<StoryEntity>>>() {
        companion object {
            fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, Flow<List<StoryEntity>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<StoryEntity>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}