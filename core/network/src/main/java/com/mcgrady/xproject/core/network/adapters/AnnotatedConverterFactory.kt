package com.mcgrady.xproject.core.network.adapters

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class AnnotatedConverterFactory(
    val factories: Map<Class<*>, Converter.Factory> = LinkedHashMap()
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        for (annotation in annotations) {
            factories[annotations.javaClass]?.let { factory ->
                return factory.responseBodyConverter(type, annotations, retrofit)
            }
        }

        return super.responseBodyConverter(type, annotations, retrofit)
    }
}