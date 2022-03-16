package com.app.medview.service;

import com.app.medview.model.Illness;

import java.util.List;

public interface IllnessService {
    Illness saveIllness(Illness illness);
    List<Illness> getIllnesses(String phone);
}
