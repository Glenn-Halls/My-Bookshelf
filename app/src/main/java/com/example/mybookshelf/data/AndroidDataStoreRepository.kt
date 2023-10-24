package com.example.mybookshelf.data

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.mybookshelf.ProtoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

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
        dataStore.updateData {
            it.toBuilder().setTestNumber(newNumber).build()
        }
    }

    override suspend fun setDarkMode(mode: ProtoData.DarkMode) {
        dataStore.updateData {
            it.toBuilder().setDarkMode(mode).build()
        }
    }
}
