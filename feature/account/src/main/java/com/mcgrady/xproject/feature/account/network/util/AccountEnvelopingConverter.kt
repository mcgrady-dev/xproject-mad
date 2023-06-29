package com.mcgrady.xproject.feature.account.network.util

import com.mcgrady.xproject.feature.account.data.model.BaseResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class AccountEnvelopingConverter : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        //val envelopedType = TypeToken.getParameterized(BaseResponse::class.java, type).type
        val envelopedType: ParameterizedType = object : ParameterizedType {
            override fun getActualTypeArguments(): Array<Type> {
                return arrayOf(type)
            }

            override fun getRawType(): Type {
                return BaseResponse::class.java
            }

            override fun getOwnerType(): Type? {
                return null
            }
        }
        val delegate =
            retrofit.nextResponseBodyConverter<BaseResponse<Any>>(this, envelopedType, annotations)
        return Converter<ResponseBody, Any> { responseBody ->
            val envelope: BaseResponse<Any>? = delegate.convert(responseBody)
            envelope?.data
        }
    }
}