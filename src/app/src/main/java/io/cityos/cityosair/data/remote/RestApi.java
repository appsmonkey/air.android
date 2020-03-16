package io.cityos.cityosair.data.remote;

import io.cityos.cityosair.data.datasource.base.Base;
import io.cityos.cityosair.data.messages.requests.AddDevicePayload;
import io.cityos.cityosair.data.messages.requests.RegisterAccountRequest;
import io.cityos.cityosair.data.messages.requests.SignUpPayload;
import io.cityos.cityosair.data.messages.requests.VerifyEmailPayload;
import io.cityos.cityosair.data.messages.responses.RegisterAccountResponse;
import io.cityos.cityosair.data.messages.responses.SignUpResponse;
import io.cityos.cityosair.data.messages.responses.VerifyEmailResponse;
import io.cityos.cityosair.data.model.AuthCredentials;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.DeviceIdEntity;
import io.cityos.cityosair.data.model.ForgotPasswordEndPayload;
import io.cityos.cityosair.data.model.LatestMeasurement;
import io.cityos.cityosair.data.messages.requests.RefreshTokenPayload;
import io.cityos.cityosair.data.messages.requests.RegisterPayload;
import io.cityos.cityosair.data.messages.requests.SocialLoginPayload;
import io.cityos.cityosair.data.messages.requests.UpdateProfilePayload;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.data.model.UserInfo;
import io.cityos.cityosair.data.model.entities.ValidateEmailEntity;
import io.cityos.cityosair.data.messages.requests.ValidateEmailPayload;
import io.cityos.cityosair.data.model.map.MapData;
import io.cityos.cityosair.data.model.reading.ChartReading;
import io.cityos.cityosair.data.model.schema.SchemaSensorType;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RestApi {

  @POST("/auth/login")
  Observable<Base<User>> login(@Body AuthCredentials model);

  @POST("/auth/login") Observable<Base<User>> socialLogin(@Body SocialLoginPayload socialLoginPayload);

  @POST("/auth/register") Observable<Base<RegisterAccountResponse>> registerAccount(@Body RegisterAccountRequest registerAccountRequest);

  @GET("/auth/validate")
  Observable<Base<VerifyEmailResponse>> verifyEmail(@Query("client_id") String clientId,
                                                    @Query("user_name") String userName,
                                                    @Query("confirmation_code") String confirmationCode,
                                                    @Query("type") String type,
                                                    @Query("cog_id") String cognitoId);

  @POST("/auth/profile")
  Observable<SignUpResponse> signUp(@Body SignUpPayload signUpPayload);

  @PUT("/profile") Observable<Object> updateProfile(@Body UpdateProfilePayload updateProfilePayload);

  @POST("/auth/refresh") Observable<Base<User>> refreshToken(@Body RefreshTokenPayload refreshTokenPayload);

  @POST("/auth/validate/email") Observable<Base<ValidateEmailEntity>> validateEmail(@Body ValidateEmailPayload validateEmailPayload);

  @POST("/device/add") Observable<Base<Object>> addDevice(@Body AddDevicePayload addDevicePayload);

  @GET("/map") Observable<Base<MapData>> getMapDevices(@Query("zone_data") String sensor,
                                                       @Query("device_data") String deviceData,
                                                       @Query("filter") String filter);

  @GET("/chart/exists") Observable<Base<Boolean>> checkIfChartExists(@Query("chart") String chart,
                                                                     @Query("sensor") String sensor,
                                                                     @Query("from") long from,
                                                                     @Query("token") String token);

  @POST("/auth/password/start") Observable<Object> startForgotPassword(@Body ValidateEmailPayload validateEmailPayload);

  @POST("/auth/password/end") Observable<Object> endForgotPassword(@Body ForgotPasswordEndPayload forgotPasswordEndPayload);

  @GET("/user/me") Observable<Base<UserInfo>> getUserInfoNew();

  @GET("/device/list") Observable<Base<List<Device>>> getUserDevices();

  @GET Observable<DeviceIdEntity> getDeviceId(@Url String url);

  @GET("/device/get") Observable<Base<Device>> getDevice(@Query("token") String token);
  @GET("/device/get") Observable<Base<LatestMeasurement>> getLatestForDevice(@Query("token") String token);

  @GET("/schema")
  Observable<Base<Map<String, SchemaSensorType>>> getSchema();

  @GET("/chart/{timeframe}/all") Observable<Base<List<ChartReading>>> getAverageGraphReadings(@Path("timeframe") String timeframe,
                                                                                              @Query("from") long from,
                                                                                              @Query("sensor") String sensor);

  @GET("chart/{timeframe}/device")
  Observable<Base<List<ChartReading>>> getGraphReadings(@Path("timeframe") String timeframe,
                                                        @Query("token") String token,
                                                        @Query("from") long from,
                                                        @Query("sensor") String sensor);
}