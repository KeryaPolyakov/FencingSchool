package com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Admin;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Apprentice;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.security.BasicAuthInterceptor;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class AdminRepository {

    private final ObjectMapper objectMapper;
    private AdminService service;

    public AdminRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(AdminService.class);
    }

    public AdminRepository(String username, String password) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(AdminService.class);
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

    public Admin post(Admin admin) throws IOException {
        Response<Admin> execute = this.service.post(admin).execute();
        return getData(execute);
    }

    public Admin get(long id) throws IOException {
        Response<Admin> execute = service.get(id).execute();
        return getData(execute);
    }

    public List<Admin> get() throws IOException {
        Response<List<Admin>> execute = service.get().execute();
        return getData(execute);
    }

    public Admin delete(long id) throws IOException {
        Response<Admin> execute = service.delete(id).execute();
        return getData(execute);
    }

    public Admin update(Admin admin) throws IOException {
        Response<Admin> execute = service.update(admin).execute();
        return getData(execute);
    }

    public Admin updateWithPassword(Admin admin) throws IOException {
        Response<Admin> execute = service.updateWithPassword(admin).execute();
        return getData(execute);
    }
}
