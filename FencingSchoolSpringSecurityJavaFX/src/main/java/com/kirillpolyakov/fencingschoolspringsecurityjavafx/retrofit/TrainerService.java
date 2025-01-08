package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Trainer;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TrainerService {

    @POST("trainer/add")
    Call<Trainer> post(@Body Trainer trainer);

    @GET("trainer/getAll")
    Call<List<Trainer>> get();

    @GET("trainer/get/{id}")
    Call<Trainer> get(@Path("id") long id);

    @PUT("trainer/update")
    Call<Trainer> update(@Body Trainer trainer);

    @PUT("trainer/update/password")
    Call<Trainer> updateWithPassword(@Body Trainer trainer);

    @DELETE("trainer/delete/{id}")
    Call<Trainer> delete(@Path("id") long id);


}
