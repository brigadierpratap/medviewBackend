package com.app.medview.repository;

import com.app.medview.model.Illness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IllnessRepository extends JpaRepository<Illness, UUID> {
    List<Illness> getIllnessesByUserId(String userId);
}
