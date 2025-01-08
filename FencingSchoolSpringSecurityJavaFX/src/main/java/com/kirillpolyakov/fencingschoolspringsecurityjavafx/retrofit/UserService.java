package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserService {

    @GET("user/{id}")
    Call<User> get(@Path("id") long id);

    @GET("user")
    Call<User>  authenticate();

    @DELETE("user/{id}")
    Call<User> delete(@Path("id") long id);



}
