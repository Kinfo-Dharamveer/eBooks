package com.kinfoitsolutions.ebooks.ui.restclient;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.model.ForgetResponse.ForgetResponse;
import com.kinfoitsolutions.ebooks.ui.model.GetAllBooksResponse.GetAllBooksResponse;
import com.kinfoitsolutions.ebooks.ui.model.Getprofile.GetProfileResponse;
import com.kinfoitsolutions.ebooks.ui.model.LoginResponse;
import com.kinfoitsolutions.ebooks.ui.model.Logout.LogoutResponse;
import com.kinfoitsolutions.ebooks.ui.model.RegisterResponse.RegisterResponse;
import com.kinfoitsolutions.ebooks.ui.model.ResetPassword.ResetPasswordResponse;
import com.kinfoitsolutions.ebooks.ui.model.UpdateProfile.UpdateProfileResponse;
import com.kinfoitsolutions.ebooks.ui.model.VerifyOtp.VerifyOtpResponse;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.io.*;
import java.util.HashMap;


public class RestClient {

    private static GitApiInterface gitApiInterface;

    private static String baseUrl = "http://112.196.85.179:9083/ebook/api/";

    public static GitApiInterface getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .build();


        gitApiInterface = retrofit.create(GitApiInterface.class);

        return gitApiInterface;



    }


    public interface GitApiInterface {

        @POST("users/login")
        Call<LoginResponse> login(@Body HashMap<String, String> hashMap);

        @POST("users/register")
        Call<RegisterResponse> register(@Body HashMap<String, String> hashMap);

        @POST("users/logout")
        Call<LogoutResponse> logout(@Body HashMap<String, String> hashMap);

        @GET("users/get_profile")
        Call<GetProfileResponse> getprofile(@Query("token") String token);

        @Multipart
        @POST("users/update_profile")
        Call<UpdateProfileResponse> updateProfile(@Part("token") RequestBody token,
                                                  @Part("name") RequestBody name,
                                                  @Part("email") RequestBody email,
                                                  @Part("phone") RequestBody phone,
                                                  @Part MultipartBody.Part image);

        @POST("users/reset_password_request")
        Call<ForgetResponse> forget_password(@Body HashMap<String, String> hashMap);


        @POST("users/verify_otp")
        Call<VerifyOtpResponse> verifyOTp(@Body HashMap<String, String> hashMap);


        @POST("users/reset_password")
        Call<ResetPasswordResponse> reset_password(@Body HashMap<String, String> hashMap);


        @POST("users/getBooks")
        Call<GetAllBooksResponse> get_books(@Body HashMap<String, String> hashMap);


    }
}

