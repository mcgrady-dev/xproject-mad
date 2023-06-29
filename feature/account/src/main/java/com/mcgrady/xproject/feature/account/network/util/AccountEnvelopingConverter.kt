/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        retrofit: Retrofit,
    ): Converter<ResponseBody, *> {
        // val envelopedType = TypeToken.getParameterized(BaseResponse::class.java, type).type
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
