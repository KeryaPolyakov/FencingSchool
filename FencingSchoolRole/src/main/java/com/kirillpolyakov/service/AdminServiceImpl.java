package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Admin;
import com.kirillpolyakov.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private AdminRepository adminRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAdminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin add(Admin admin) {
        try {
            admin.setPassword(this.passwordEncoder.encode(admin.getPassword()));
            this.adminRepository.save(admin);
            return admin;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин или электронная почта уже заняты");
        }
    }

    @Override
    public List<Admin> get() {
        return this.adminRepository.findAll();
    }

    @Override
    public Admin get(long id) {
        return this.adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Такого админа не существует"));
    }

    @Override
    public Admin update(Admin admin) {
        Admin old = this.get(admin.getId());
        old.setUserName(admin.getUserName());
        old.setSurname(admin.getSurname());
        old.setPatronymic(admin.getPatronymic());
        old.setName(admin.getName());
        old.setEmail(admin.getEmail());
        old.setSalary(admin.getSalary());
        try {
            this.adminRepository.save(old);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин или электронная почта уже заняты");
        }
        return old;
    }

    @Override
    public Admin updateWithPass(Admin admin) {
        Admin old = this.get(admin.getId());
        old.setUserName(admin.getUserName());
        old.setSurname(admin.getSurname());
        old.setPatronymic(admin.getPatronymic());
        old.setName(admin.getName());
        old.setEmail(admin.getEmail());
        old.setSalary(admin.getSalary());
        old.setPassword(passwordEncoder.encode(admin.getPassword()));
        try {
            this.adminRepository.save(old);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин или электронная почта уже заняты");
        }
        return old;
    }

    @Override
    public Admin delete(long id) {
        Admin admin = this.get(id);
        this.adminRepository.delete(admin);
        return admin;
    }
}
