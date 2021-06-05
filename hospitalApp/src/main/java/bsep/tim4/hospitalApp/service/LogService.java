package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.LogDto;
import bsep.tim4.hospitalApp.dto.LogFilterDTO;
import bsep.tim4.hospitalApp.model.Log;
import bsep.tim4.hospitalApp.model.LogLevel;
import bsep.tim4.hospitalApp.model.QLog;
import bsep.tim4.hospitalApp.repository.LogRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Page<LogDto> findAll(Pageable pageable) {
        List<LogDto> logDtoList = new ArrayList<>();
        Page<Log> page = logRepository.findAllByOrderByTimestampDesc(pageable);
        for(Log log : page.toList()) {
            logDtoList.add(new LogDto(log.getLevel(), log.getMessage(), log.getTimestamp()));
        }

        return new PageImpl<>(logDtoList, page.getPageable(), page.getTotalElements());
    }

    public Page<LogDto> filterAllLogs(Pageable pageable, LogFilterDTO logFilterDTO) {
        List<LogDto> logDtoList = new ArrayList<>();
        QLog qLog = new QLog("log");
        BooleanBuilder builder = new BooleanBuilder();
        if(!logFilterDTO.getLevel().equals("") && logFilterDTO.getLevel() != null) {
            Predicate predicate = qLog.level.eq(LogLevel.valueOf(logFilterDTO.getLevel()));
            builder.and(predicate);
        }
        if(logFilterDTO.getFromDate() != null){
            Predicate predicate = qLog.timestamp.gt(logFilterDTO.getFromDate());
            builder.and(predicate);
        }
        if(logFilterDTO.getToDate() != null){
            Predicate predicate = qLog.timestamp.lt(logFilterDTO.getToDate());
            builder.and(predicate);
        }
        if(!logFilterDTO.getMessage().equals("") && logFilterDTO.getMessage() != null) {
            Predicate predicate = qLog.message.matches(logFilterDTO.getMessage());
            builder.and(predicate);
        }
        if(!logFilterDTO.getSource().equals("") && logFilterDTO.getSource() != null) {
            Predicate predicate = qLog.source.matches(logFilterDTO.getSource());
            builder.and(predicate);
        }
        if(!logFilterDTO.getIpAddress().equals("") && logFilterDTO.getMessage() != null) {
            Predicate predicate = qLog.ipAddress.matches(logFilterDTO.getIpAddress());
            builder.and(predicate);
        }
        Page<Log> logs = logRepository.findAll(builder, pageable);
        for(Log log : logs.toList()) {
            logDtoList.add(new LogDto(log.getLevel(), log.getMessage(), log.getTimestamp()));
        }

        return new PageImpl<>(logDtoList, logs.getPageable(), logs.getTotalElements());
    }
}