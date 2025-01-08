package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.TrainerSchedule;
import retrofit2.Call;
import retrofit2.http.*;

import java.time.LocalTime;
import java.util.List;

public interface TrainerScheduleService {

    @POST("trainer_schedule/add/{trainerId}/params")
    Call<ResponseResult<TrainerSchedule>> post(@Path("trainerId") long id, @Query("day") String day,
                                               @Query("start") LocalTime start, @Query("finish") LocalTime finish);

    @GET("trainer_schedule/{id}")
    Call<ResponseResult<TrainerSchedule>> get(@Path("id") long id);

    @GET("trainer_schedule")
    Call<ResponseResult<List<TrainerSchedule>>> get();


    @DELETE("trainer_schedule/delete/{trainerId}/day")
    Call<ResponseResult<TrainerSchedule>> delete(@Path("trainerId") long trainerId, @Query("day") String day);

}
