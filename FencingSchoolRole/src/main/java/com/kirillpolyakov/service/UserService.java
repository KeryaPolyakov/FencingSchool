package com.kirillpolyakov.service;

import com.kirillpolyakov.model.User;

public interface UserService{

    User get(long id);
    void add(User user);

    User delete(long id);

}
