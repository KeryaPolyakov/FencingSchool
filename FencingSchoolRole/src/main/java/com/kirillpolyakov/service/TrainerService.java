package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Trainer;

import java.util.List;

public interface TrainerService {

    Trainer add(Trainer trainer);

    Trainer get(long id);

    Trainer getForApprentice(long id);

    List<Trainer> get();

    Trainer update(Trainer trainer);

    Trainer updateWithPassword(Trainer trainer);

    Trainer delete(long id);

}
