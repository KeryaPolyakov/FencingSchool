package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Trainer;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.security.BasicAuthInterceptor;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class TrainerRepository {

    private final ObjectMapper objectMapper;
    private TrainerService service;

    public TrainerRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(TrainerService.class);
    }

    public TrainerRepository(String username, String password) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(TrainerService.class);
    }

    private <T> T getData(Response<T> execute) throws IOException {
        if (execute.code() != 200) {
            String message = objectMapper.readValue(execute.errorBody().string(),
                    new TypeReference<ResponseResult<T>>() {
                    }).getMessage();
            throw new IllegalArgumentException(message);
        }
        return execute.body();
    }

    public Trainer post(Trainer trainer) throws IOException {
        Response<Trainer> execute = this.service.post(trainer).execute();
        return getData(execute);
    }

    public Trainer get(long id) throws IOException {
        Response<Trainer> execute = service.get(id).execute();
        return getData(execute);
    }

    public List<Trainer> get() throws IOException {
        Response<List<Trainer>> execute = service.get().execute();
        return getData(execute);
    }

    public Trainer delete(long id) throws IOException {
        Response<Trainer> execute = service.delete(id).execute();
        return getData(execute);
    }

    public Trainer update(Trainer trainer) throws IOException {
        Response<Trainer> execute = service.update(trainer).execute();
        return getData(execute);
    }

    public Trainer updateWithPassword(Trainer trainer) throws IOException {
        Response<Trainer> execute = service.updateWithPassword(trainer).execute();
        return getData(execute);
    }
}
