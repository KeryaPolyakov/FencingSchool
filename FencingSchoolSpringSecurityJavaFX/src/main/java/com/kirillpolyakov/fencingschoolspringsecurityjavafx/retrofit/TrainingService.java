package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Training;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TrainingService {

    @POST("training/apprentice_trainer")
    Call<ResponseResult<Training>> post(@Body Training training, @Query("apprenticeId") long apprenticeId,
                                        @Query("trainerId") long trainerId);

    @GET("training/{id}")
    Call<ResponseResult<Training>> get(@Path("id") long id);

    @GET("training/apprentice/{apprenticeId}")
    Call<ResponseResult<List<Training>>> getByApprenticeId(@Path("apprenticeId") long id);

    @GET("training/trainer/{trainerId}")
    Call<ResponseResult<List<Training>>> getByTrainerId(@Path("trainerId") long id);

    @GET("training/free_time/{trainerId}")
    Call<ResponseResult<List<String>>> getFreeTime(@Path("trainerId") long id, @Query("date") String date,
                                                   @Query("numberGym") int numberGym);

    @DELETE("training/delete/{id}")
    Call<ResponseResult<Training>> delete(@Path("id") long id);


}
