package com.kirillpolyakov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.model.Trainer;
import com.kirillpolyakov.repository.TrainerRepository;
import com.kirillpolyakov.service.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerServiceImpl trainerService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private TrainerRepository trainerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void set() {
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    void add() throws Exception {
        String username = "a";
        String password = "a";
        Trainer trainer = new Trainer();
        when(trainerService.add(any())).thenReturn(trainer);

        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                        .with(httpBasic(username, password)) // Добавляем аутентификацию
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainer)))
                .andExpect(status().isOk());

        /*mockMvc.perform(post("/trainer/add")
                .with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer)))
                .andExpect(status().isOk());*/
    }

    @Test
    void getTrainer() throws Exception {
        String username = "a";
        String password = "a";
        Trainer trainer = new Trainer();
        trainer.setName("a");
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(trainer));
        mockMvc.perform(get("/trainer/get/{id}", "1")
                        .with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(trainer)));
    }

}