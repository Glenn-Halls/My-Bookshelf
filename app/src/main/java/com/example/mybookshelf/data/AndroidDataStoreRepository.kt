package com.example.mybookshelf.data

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.mybookshelf.ProtoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
    override suspend fun setSearchString(string: String) {
        dataStore.updateData {
            it.toBuilder().setSearchString(string).build()
        }
    }

    override suspend fun getSearchString(): String {
       return dataStore.data.map { it.searchString }.first()
    }


    override suspend fun setDarkMode(mode: ProtoData.DarkMode) {
        dataStore.updateData {
            it.toBuilder().setDarkMode(mode).build()
        }
    }

    override suspend fun setStartupScreen(protoStartupScreen: ProtoData.ProtoScreenSelect) {
        dataStore.updateData {
            it.toBuilder().setScreenSelect(protoStartupScreen).build()
        }
    }
}
