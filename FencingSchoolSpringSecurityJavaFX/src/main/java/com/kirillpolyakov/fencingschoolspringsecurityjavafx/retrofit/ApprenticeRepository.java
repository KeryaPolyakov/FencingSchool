package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Apprentice;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.security.BasicAuthInterceptor;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class ApprenticeRepository {

    private final ObjectMapper objectMapper;
    private ApprenticeService service;

    public ApprenticeRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(ApprenticeService.class);
    }

    public ApprenticeRepository(String username, String password) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(ApprenticeService.class);
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

    public Apprentice post(Apprentice apprentice) throws IOException {
        Response<Apprentice> execute = this.service.post(apprentice).execute();
        return getData(execute);
    }

    public Apprentice get(long id) throws IOException {
        Response<Apprentice> execute = service.get(id).execute();
        return getData(execute);
    }

    public List<Apprentice> get() throws IOException {
        Response<List<Apprentice>> execute = service.get().execute();
        return getData(execute);
    }

    public Apprentice delete(long id) throws IOException {
        Response<Apprentice> execute = service.delete(id).execute();
        return getData(execute);
    }

    public Apprentice update(Apprentice apprentice) throws IOException {
        Response<Apprentice> execute = service.update(apprentice).execute();
        return getData(execute);
    }

    public Apprentice updateWithPassword(Apprentice apprentice) throws IOException {
        Response<Apprentice> execute = service.updateWithPassword(apprentice).execute();
        return getData(execute);
    }
}
