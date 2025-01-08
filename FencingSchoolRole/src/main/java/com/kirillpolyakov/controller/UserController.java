package com.kirillpolyakov.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.User;
import com.kirillpolyakov.model.UserDetailsImpl;
import com.kirillpolyakov.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;

    private ObjectMapper objectMapper;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        {
            objectMapper.registerModule(new JavaTimeModule());
        }
    }

    @Operation(
            summary = "Получение пользователя",
            description = "Осуществляет получение пользователя по Id, доступно Администратору"
    )
    @GetMapping(path = "/{id}")
    public void get(HttpServletResponse response, @PathVariable @Parameter(description = "id",
            example = "1", required = true) long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<User>() {
                }).writeValue(response.getWriter(),
                        this.userService.get(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Аутентификация",
            description = "Осуществляет осуществляет проверку соответствия логина и пароля для пользователя " +
                    "в базе данных, возвращает объект User(c указанием типа объекта)"
    )
    @GetMapping
    public void authenticate(HttpServletResponse response, Authentication authentication) {
        try {
            try {
                if (authentication.isAuthenticated()) {
                    long id = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(200);
                    objectMapper.writerFor(new TypeReference<User>() {
                    }).writeValue(response.getWriter(),
                            this.userService.get(id));
                }
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * delete – осуществляет удаление пользователя с заданным id из базы данных(admin)
     */
    @Operation(
            summary = "Удаление пользователя",
            description = "Осуществляет удаление пользователя по id, доступно Администратору"
    )
    @DeleteMapping(path = "/{id}")
    public void delete(HttpServletResponse response, @PathVariable @Parameter(description = "id",
            example = "1", required = true) long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<User>() {
                }).writeValue(response.getWriter(),
                        this.userService.delete(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
