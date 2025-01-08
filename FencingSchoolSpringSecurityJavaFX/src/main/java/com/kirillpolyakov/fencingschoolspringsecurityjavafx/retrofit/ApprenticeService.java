package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Apprentice;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApprenticeService {

    @POST("apprentice")
    Call<Apprentice> post (@Body Apprentice apprentice);

    @GET("apprentice/getAll")
    Call<List<Apprentice>> get();

    @GET("apprentice/get/{id}")
    Call<Apprentice> get(@Path("id") long id);

    @PUT("apprentice/update")
    Call<Apprentice> update (@Body Apprentice apprentice);

    @PUT("apprentice/update/password")
    Call<Apprentice> updateWithPassword (@Body Apprentice apprentice);

    @DELETE("apprentice/delete/{id}")
    Call<Apprentice> delete(@Path("id") long id);



}
