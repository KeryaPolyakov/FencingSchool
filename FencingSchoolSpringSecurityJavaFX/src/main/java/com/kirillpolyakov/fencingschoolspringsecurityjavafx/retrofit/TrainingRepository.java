package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Training;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.security.BasicAuthInterceptor;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class TrainingRepository {

    private final ObjectMapper objectMapper;
    private TrainingService service;

    public TrainingRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(TrainingService.class);
    }

    public TrainingRepository(String username, String password) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(TrainingService.class);
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

    public Training post(Training training, long apprenticeId, long trainerId) throws IOException {
        Response<ResponseResult<Training>> execute = this.service.post(training, apprenticeId, trainerId).execute();
        return getData(execute);
    }

    public Training get(long id) throws IOException {
        Response<ResponseResult<Training>> execute = service.get(id).execute();
        return getData(execute);
    }

    public List<String> getFreeTime(long id, String localDate, int numberGym) throws IOException {
        Response<ResponseResult<List<String>>> execute = service.getFreeTime(id, localDate, numberGym).execute();
        return getData(execute);
    }

    public List<Training> getByTrainerId(long id) throws IOException {
        Response<ResponseResult<List<Training>>> execute = service.getByTrainerId(id).execute();
        return getData(execute);
    }

    public List<Training> getByApprenticeId(long id) throws IOException {
        Response<ResponseResult<List<Training>>> execute = service.getByApprenticeId(id).execute();
        return getData(execute);
    }

    public Training delete(long id) throws IOException {
        Response<ResponseResult<Training>> execute = service.delete(id).execute();
        return getData(execute);
    }
}
