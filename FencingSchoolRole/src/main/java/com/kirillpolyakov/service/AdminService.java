package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Admin;

import java.util.List;

public interface AdminService {

    Admin add (Admin admin);

    List<Admin> get();

    Admin get(long id);

    Admin update(Admin admin);

    Admin updateWithPass(Admin admin);

    Admin delete(long id);
}
