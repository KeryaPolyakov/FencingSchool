package com.kirillpolyakov.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.Admin;
import com.kirillpolyakov.model.User;
import com.kirillpolyakov.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Tag(name = "AdminController", description = "Контроллер для работы с администраторами")
@RestController
@RequestMapping("admin")
public class AdminController {

    private AdminService adminService;

    private ObjectMapper objectMapper;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        {
            objectMapper.registerModule(new JavaTimeModule());
        }
    }

    @Operation(
            summary = "Регистрация администратора",
            description = "Позволяет зарегистрировать администратора, доступно пользователю Администратор"
    )
    @PostMapping
    public void add(HttpServletResponse response, @RequestBody Admin admin) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Admin>() {
                }).writeValue(response.getWriter(),
                        this.adminService.add(admin));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Operation(
            summary = "Получение администратора",
            description = "Позволяет получить объект администратор из БД по Id, доступно пользователю Администратор"
    )
    @GetMapping(path = "/{id}")
    public void get(HttpServletResponse response, @PathVariable @Parameter(description = "id ",
            example = "1", required = true) long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Admin>() {
                }).writeValue(response.getWriter(),
                        this.adminService.get(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Получение всех администраторов",
            description = "Позволяет получить всех администраторов из БД, доступно пользователю Администратор")
    @GetMapping
    public void get(HttpServletResponse response) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<List<Admin>>() {
                }).writeValue(response.getWriter(),
                        this.adminService.get());
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Обновление администратора без пароля",
            description = "Позволяет обновить данные администратора без пароля, доступно пользователю Администратор"
    )
    @PutMapping
    public void update(HttpServletResponse response, @RequestBody Admin admin) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Admin>() {
                }).writeValue(response.getWriter(),
                        this.adminService.update(admin));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Обновление администратора с паролем",
            description = "Позволяет обновить данные администратора с паролем, доступно пользователю Администратор"
    )
    @PutMapping("password")
    public void updatePass(HttpServletResponse response, @RequestBody Admin admin) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Admin>() {
                }).writeValue(response.getWriter(),
                        this.adminService.updateWithPass(admin));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Удаление администратора",
            description = "Позволяет удалить администратора по Id, доступно пользователю Администратор"
    )
    @DeleteMapping(path = "/{id}")
    public void delete(HttpServletResponse response, @PathVariable @Parameter(description = "id", example = "1",
            required = true) long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Admin>() {
                }).writeValue(response.getWriter(),
                        this.adminService.delete(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
