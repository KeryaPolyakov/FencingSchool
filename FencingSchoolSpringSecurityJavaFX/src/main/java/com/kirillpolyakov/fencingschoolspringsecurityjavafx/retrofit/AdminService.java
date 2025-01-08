package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Admin;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface AdminService {

    @POST("admin")
    Call<Admin> post (@Body Admin admin);

    @GET("admin/{id}")
    Call<Admin> get(@Path("id") long id);

    @GET("admin")
    Call<List<Admin>> get();

    @PUT("admin")
    Call<Admin> update (@Body Admin admin);

    @PUT("admin/password")
    Call<Admin> updateWithPassword (@Body Admin admin);

    @DELETE("admin/{id}")
    Call<Admin> delete(@Path("id") long id);
}
