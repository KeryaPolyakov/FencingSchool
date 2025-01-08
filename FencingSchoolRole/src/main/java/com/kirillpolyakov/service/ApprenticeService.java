package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Apprentice;

import java.util.List;

public interface ApprenticeService {

    Apprentice add(Apprentice apprentice);

    Apprentice get(long id);

    List<Apprentice> get();

    Apprentice update(Apprentice apprentice);

    Apprentice updateWithPassword(Apprentice apprentice);

    Apprentice delete(long id);
}
