package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.LogDto;
import bsep.tim4.hospitalApp.model.Log;
import bsep.tim4.hospitalApp.repository.LogRepository;
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

}