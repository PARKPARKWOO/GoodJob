package com.goodjob.job.service;



import com.goodjob.job.dto.JobResponseDto;
import com.goodjob.job.entity.JobStatistic;
import com.goodjob.job.repository.JobStatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobStatisticService {
    private final JobStatisticRepository jobStatisticRepository;


    /**
     * create
     */
    @Transactional
    public void create(JobResponseDto jobResponseDto) {

        try {
            validateDuplicateCompany(jobResponseDto);
            JobStatistic company = JobStatistic.create(jobResponseDto);

            jobStatisticRepository.save(company);
        } catch (IllegalStateException | DataIntegrityViolationException e) {
            log.info("중복데이터");
        }
    }

    private void validateDuplicateCompany(JobResponseDto jobResponseDto) {
        List<JobStatistic> companyList = jobStatisticRepository.findByUrl(jobResponseDto.getUrl());

        if (!companyList.isEmpty()) {
            throw new IllegalStateException("중복 입력");
        }
    }


    // update
    public List<JobResponseDto> getFilterDto(List<JobResponseDto> mainDto, List<JobResponseDto> filterDto) {
        return mainDto.stream().filter(md -> filterDto.stream().noneMatch(
                fd -> md.getSubject().equals(fd.getSubject()) &&
                        md.getCompany().equals(fd.getCompany()) &&
                        md.getCareer() == fd.getCareer())).toList();
    }

    /**
     * select Logic
     */

    public Page<JobStatistic> getList(String sectorCode, String place,String career, int page){
        int sectorNum = Integer.parseInt(sectorCode);
        int careerCode = Integer.parseInt(career);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("startDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return jobStatisticRepository.noKeyword(sectorNum, careerCode, place, pageable);
    }



    public Page<JobStatistic> getQueryList(String keyword, String sectorCode, String career, String place,int page) {
        int sectorNum = Integer.parseInt(sectorCode);
        int careerCode = Integer.parseInt(career);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("startDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return jobStatisticRepository.filterList(sectorNum, careerCode,place, keyword, pageable);
    }

    @Transactional
    public void delete(JobStatistic jobStatistic) {
        jobStatisticRepository.delete(jobStatistic);
    }



    public List<JobStatistic> getAll() {
        return jobStatisticRepository.findAll();
    }

    /**
     * batch Logic
     */

    // upsert
    @Transactional
    @Async
    public void upsert(JobResponseDto dto) {
        jobStatisticRepository.upsert(dto);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void regularlyDeadLineFind() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(dateTimeFormatter);
        String aMonthAgo = now.minusMonths(1).format(dateTimeFormatter);
        regularlyDelete(jobStatisticRepository.findDeadLine(today, aMonthAgo));
    }

    @Transactional
    public void regularlyDelete(List<JobStatistic> deadLine) {
        jobStatisticRepository.deleteAllInBatch(deadLine);
    }
}




