package com.homework.coursework.data.frameworks.network

import com.homework.coursework.data.frameworks.network.dto.*
import com.homework.coursework.data.frameworks.database.dao.AuthDao
import com.homework.coursework.data.frameworks.network.utils.NetworkConstants.BASE_URL_API
import com.homework.coursework.data.frameworks.network.utils.addJsonConverter
import com.homework.coursework.data.frameworks.network.utils.setClient
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.*

interface ApiService {

    /**
     * Get all organization streams
     * @return Observable of streams
     */
    @ExperimentalSerializationApi
    @GET("streams")
    fun getAllStreams(): Observable<StreamsResponse>

    /**
     * Get subscribed streams
     * @return Observable of streams
     */
    @ExperimentalSerializationApi
    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Observable<StreamsResponse>

    /**
     * Get topics of selected stream
     * @param streamId id of selected stream
     */
    @ExperimentalSerializationApi
    @GET("users/me/{stream_id}/topics")
    fun getTopics(@Path("stream_id") streamId: Int): Observable<TopicsResponse>

    /**
     * get messages of selected topic
     */
    @ExperimentalSerializationApi
    @GET("messages")
    fun loadMessages(
        @QueryMap queryMap: Map<String, String>
    ): Observable<MessagesResponse>

    /**
     * Add emoji to message
     * @param messageId the target message's ID.=
     */
    @POST("messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Int,
        @QueryMap queryMap: Map<String, String>
    ): Completable

    /**
     * Delete emoji from message
     * @param messageId the target message's ID.=
     */
    @DELETE("messages/{message_id}/reactions")
    fun deleteReaction(
        @Path("message_id") messageId: Int,
        @QueryMap queryMap: Map<String, String>
    ): Completable

    /**
     * Add new message to topic
     */
    @ExperimentalSerializationApi
    @POST("messages")
    fun addMessage(
        @QueryMap queryMap: Map<String, String>
    ): Observable<AddMessageResponse>

    /**
     * Delete message
     */
    @DELETE("messages/{id}")
    fun deleteMessage(
        @Path("id") messageId: Int
    ): Completable

    /**
     * Edit message
     */
    @PATCH("messages/{id}")
    fun editMessage(
        @Path("id") messageId: Int,
        @QueryMap queryMap: Map<String, String>
    ): Completable

    /**
     * Get actual user data
     * @return Observable of user data
     */
    @GET("users/me")
    fun getMe(): Observable<UserDto>

    /**
     * Get user data
     * @param id is id of user who data need
     */
    @ExperimentalSerializationApi
    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Observable<UserResponse>

    /**
     * Get status of user
     * @param userId id of user, which status need to be known
     */
    @ExperimentalSerializationApi
    @GET("users/{user_id}/presence")
    fun getStatus(@Path("user_id") userId: Int): Observable<StatusResponse>

    /**
     * Authorization
     * @param username is username of auth user
     * @param password is password of auth user
     */
    @POST("fetch_api_key")
    fun authUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<AuthDto>

    /**
     * get list of all users
     */
    @ExperimentalSerializationApi
    @GET("users")
    fun getAllUsers(): Observable<UserResponseList>

    /**
     * create new stream or subscribed to stream if it existed
     */
    @ExperimentalSerializationApi
    @POST("users/me/subscriptions")
    fun createStream(
        @QueryMap queryMap: Map<String, String>
    ): Observable<SubscribeResponse>

    @ExperimentalSerializationApi
    @Multipart
    @POST("user_uploads")
    fun addFile(@Part file: MultipartBody.Part): Observable<AddFileResponse>

    companion object {
        fun create(authDao: AuthDao): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .setClient(authDao)
                .addJsonConverter()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(ApiService::class.java)
        }
    }
}
