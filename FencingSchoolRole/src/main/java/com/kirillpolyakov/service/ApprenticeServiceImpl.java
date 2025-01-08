package com.kirillpolyakov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.model.Apprentice;
import com.kirillpolyakov.model.User;
import com.kirillpolyakov.model.UserDetailsImpl;
import com.kirillpolyakov.repository.ApprenticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprenticeServiceImpl implements ApprenticeService{

    private ApprenticeRepository apprenticeRepository;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private ObjectMapper mapper;


    @Autowired
    public void setApprenticeRepository(ApprenticeRepository apprenticeRepository) {
        this.apprenticeRepository = apprenticeRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Apprentice add(Apprentice apprentice) {
        try {
            apprentice.setPassword(this.passwordEncoder.encode(apprentice.getPassword()));
            this.apprenticeRepository.save(apprentice);
            return apprentice;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин занят или ученик с данным номером уже существет");
        }
    }

    @Override
    public Apprentice get(long id) {
        return this.apprenticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Такого ученика нет"));
    }

    @Override
    public List<Apprentice> get() {
        return this.apprenticeRepository.findAll();
    }

    @Override
    public Apprentice update(Apprentice apprentice) {
        Apprentice old = this.get(apprentice.getId());
        old.setUserName(apprentice.getUserName());
        old.setSurname(apprentice.getSurname());
        old.setPatronymic(apprentice.getPatronymic());
        old.setName(apprentice.getName());
        old.setPhoneNumber(apprentice.getPhoneNumber());
        try {
            this.apprenticeRepository.save(old);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин занят или ученик с данным номером уже существет");
        }
        return old;
    }

    @Override
    public Apprentice updateWithPassword(Apprentice apprentice) {
        Apprentice old = this.get(apprentice.getId());
        old.setUserName(apprentice.getUserName());
        old.setSurname(apprentice.getSurname());
        old.setPatronymic(apprentice.getPatronymic());
        old.setName(apprentice.getName());
        old.setPhoneNumber(apprentice.getPhoneNumber());
        old.setPassword(passwordEncoder.encode(apprentice.getPassword()));
        try {
            this.apprenticeRepository.save(old);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин занят или ученик с данным номером уже существет");
        }
        return old;
    }

    @Override
    public Apprentice delete(long apprenticeId) {
        Apprentice apprentice = this.get(apprenticeId);
        this.apprenticeRepository.delete(apprentice);
        return apprentice;
    }
}
