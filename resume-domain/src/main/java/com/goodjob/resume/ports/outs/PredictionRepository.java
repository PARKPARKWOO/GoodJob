package com.goodjob.resume.ports.outs;

import com.goodjob.resume.domain.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction,Long> {

    Optional<Prediction> findByMemberId(Long member);

    List<Prediction> findAllByMemberId(Long member);
}