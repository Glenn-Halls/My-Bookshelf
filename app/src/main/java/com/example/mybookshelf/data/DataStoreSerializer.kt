package com.example.mybookshelf.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import com.example.mybookshelf.ProtoData
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object DataStoreSerializer : Serializer<ProtoData> {
    override val defaultValue = ProtoData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProtoData {
        try {
            return ProtoData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ProtoData, output: OutputStream) = t.writeTo(output)
}
