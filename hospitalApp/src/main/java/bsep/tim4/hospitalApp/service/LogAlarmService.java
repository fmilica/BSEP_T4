package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.LogAlarmDto;
import bsep.tim4.hospitalApp.model.LogAlarm;
import bsep.tim4.hospitalApp.repository.LogAlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogAlarmService {

    @Autowired
    private LogAlarmRepository logAlarmRepository;

    public Page<LogAlarmDto> findAll(Pageable pageable) {
        List<LogAlarmDto> logAlarmDtoList = new ArrayList<>();
        Page<LogAlarm> page = logAlarmRepository.findAllByOrderByTimestampDesc(pageable);
        for( LogAlarm logAlarm : page.toList()) {
            logAlarmDtoList.add(new LogAlarmDto(logAlarm.getType(), logAlarm.getSource(), logAlarm.getMessage(), logAlarm.getTimestamp()));
        }

        return new PageImpl<>(logAlarmDtoList, page.getPageable(), page.getTotalElements());
    }

    public List<String> findAllAlarmTypes() {
        return logAlarmRepository.findLogAlarmTypes();
    }
}
