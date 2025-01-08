package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.TrainerSchedule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.security.BasicAuthInterceptor;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class TrainerScheduleRepository {

    private final ObjectMapper objectMapper;
    private TrainerScheduleService service;

    public TrainerScheduleRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(TrainerScheduleService.class);
    }

    public TrainerScheduleRepository(String userName, String password) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(userName, password)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(TrainerScheduleService.class);
    }

    private <T> T getData(Response<ResponseResult<T>> execute) throws IOException {
        if (execute.code() != 200) {
            String message = objectMapper.readValue(execute.errorBody().string(),
                    new TypeReference<ResponseResult<T>>() {
                    }).getMessage();
            throw new IllegalArgumentException(message);
        }
        return execute.body().getData();
    }

    public TrainerSchedule post(long trainerId, String day, LocalTime start, LocalTime finish) throws IOException {
        Response<ResponseResult<TrainerSchedule>> execute = this.service.post(trainerId, day, start, finish).execute();
        return getData(execute);
    }

    public TrainerSchedule get(long id) throws IOException {
        Response<ResponseResult<TrainerSchedule>> execute = service.get(id).execute();
        return getData(execute);
    }

    public List<TrainerSchedule> get() throws IOException {
        Response<ResponseResult<List<TrainerSchedule>>> execute = service.get().execute();
        return getData(execute);
    }

    public TrainerSchedule delete(long trainerId, String day) throws IOException {
        Response<ResponseResult<TrainerSchedule>> execute = service.delete(trainerId, day).execute();
        return getData(execute);
    }
}
