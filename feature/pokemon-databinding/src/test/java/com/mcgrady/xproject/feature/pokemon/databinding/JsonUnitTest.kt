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
package com.mcgrady.xproject.feature.pokemon.databinding

import junit.framework.TestCase
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by mcgrady on 2022/6/8.
 */
class JsonUnitTest {
    @Serializable
    data class UserBean(
        val name: String,
        val email: String,
        val age: Int = 16,
        val role: Role = Role.Viewer,
    )

    enum class Role { Viewer, Editor, Owner }

    @Serializable
    data class ExtraData(
        val nameOfDevice: String,
        @Serializable(with = DateAsStringSerializer::class) val dateOfBuy: Date,
        @Serializable(with = DateAsTimeStampSerializer::class) val timeOfWarranty: Date,
        @Serializable(with = DecimalAsStringSerializer::class) val price: BigDecimal,
    )

    object DateAsStringSerializer : KSerializer<Date> {

        private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ")

        override fun deserialize(decoder: Decoder): Date {
            val s = decoder.decodeString()
            return formatter.parse(s) as Date
        }

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("date", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Date) {
            val s = formatter.format(value)
            encoder.encodeString(s)
        }
    }

    object DateAsTimeStampSerializer : KSerializer<Date> {

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("date", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: Date) {
            encoder.encodeString(value.time.toString())
        }

        override fun deserialize(decoder: Decoder): Date {
            val string = decoder.decodeString()
            return Date(string.toLong())
        }
    }

    object DecimalAsStringSerializer : KSerializer<BigDecimal> {

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("decimal", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: BigDecimal) {
            encoder.encodeString(value.toPlainString())
        }

        override fun deserialize(decoder: Decoder): BigDecimal {
            val string = decoder.decodeString()
            return BigDecimal(string)
        }
    }

    private val userJson: String
        get() = """
            {
                "name": "mcgrady",
                "email": "mcgrady.dev@gmail.com",
                "phone": "10086"
            }
        """.trimIndent()

    private val extraDataJson: String
        get() = """
            {
                "nameOfDevice":"foo",
                "dateOfBuy":"2021-01-27 22:26:25.919+0100",
                "timeOfWarranty":"1643318785917",
                "price":"12.345678912"
            }
        """.trimIndent()

    private lateinit var json: Json

    @Before
    fun setUp() {
        json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    @After
    fun clearUp() {
    }

    @Test
    fun ksDecodeUserBean() {
        val user = json.decodeFromString<UserBean>(userJson)
        println("decodeFromString=$user")
        TestCase.assertEquals("mcgrady", user.name)
        TestCase.assertEquals(Role.Viewer, user.role)
        TestCase.assertEquals(16, user.age)
    }

    @Test
    fun ksEncodeDefaults() {
        val user = UserBean(name = "mcgrady", "mcgrady.dev@gmail.com")
        val encodeToString = json.encodeToString(UserBean.serializer(), user)
        println(encodeToString)
    }

    @Test
    fun ksDecodeExtraData() {
        val extraData = json.decodeFromString<ExtraData>(extraDataJson)
        TestCase.assertEquals("foo", extraData.nameOfDevice)

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ")
        TestCase.assertEquals("2021-01-28 05:26:25.919+0800", formatter.format(extraData.dateOfBuy))
        TestCase.assertEquals("1643318785917", extraData.timeOfWarranty.time.toString())
        TestCase.assertEquals("12.345678912", extraData.price.toString())
        val encodeToString = json.encodeToString(ExtraData.serializer(), extraData)
        println(encodeToString)
    }
}
