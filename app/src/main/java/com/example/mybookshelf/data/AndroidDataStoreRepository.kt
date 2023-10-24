package com.example.mybookshelf.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.mybookshelf.ProtoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import androidx.compose.runtime.getValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val TAG = "Android DataStore Repo"


// Local repository for data store, used in default app container
class AndroidDataStoreRepository(
    private val dataStore: DataStore<ProtoData>
) : DataStoreRepository {
    // Produces a flow, to be used by ViewModel
    override val dataStoreFlow: Flow<ProtoData> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading from DataStore", exception)
                emit(ProtoData.getDefaultInstance())
            } else {
                throw exception
            }
        }
    // Sets the number in the data store
    override suspend fun setNumber(newNumber: Int) {
        dataStore.updateData { data ->
            data.toBuilder().setTestNumber(newNumber).build()
        }
    }
}
