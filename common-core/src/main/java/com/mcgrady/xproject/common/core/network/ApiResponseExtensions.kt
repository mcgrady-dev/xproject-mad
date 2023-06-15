package com.mcgrady.xproject.common.core.network

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.3.1
 *
 *  Returns true if this instance represents an [ApiResponse.Success].
 */
inline val ApiResponse<Any>.isSuccess: Boolean
    get() = this is ApiResponse.Success

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.3.1
 *
 *  Returns true if this instance represents an [ApiResponse.Failure].
 */
inline val ApiResponse<Any>.isFailure: Boolean
    get() = this is ApiResponse.Failure

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.3.1
 *
 *  Returns true if this instance represents an [ApiResponse.Failure.Error].
 */
inline val ApiResponse<Any>.isError: Boolean
    get() = this is ApiResponse.Failure.Error

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.3.1
 *
 *  Returns true if this instance represents an [ApiResponse.Failure.Exception].
 */
inline val ApiResponse<Any>.isException: Boolean
    get() = this is ApiResponse.Failure.Exception

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.3.2
 *
 *  Returns The error message or null depending on the type of [ApiResponse].
 */
inline val ApiResponse<Any>.messageOrNull: String?
    get() = when (this) {
        is ApiResponse.Failure.Error -> message()
        is ApiResponse.Failure.Exception -> message
        else -> null
    }