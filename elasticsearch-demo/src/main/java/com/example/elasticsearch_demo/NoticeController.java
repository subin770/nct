package com.example.elasticsearch_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
@CrossOrigin(origins = "*")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @PostMapping("/save")
    public Notice saveNotice(@RequestBody Notice notice) {
        try {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (notice.getId() == null || notice.getId().isEmpty()) {
                notice.setId(String.valueOf(System.currentTimeMillis()).substring(9));
                notice.setRegDate(now);
                notice.setNoticeDate(now);
                notice.setStatus("통보 완료");
            } else {
                noticeRepository.findById(notice.getId()).ifPresent(existing -> {
                    notice.setRegDate(existing.getRegDate());
                    notice.setNoticeDate(now); 
                    notice.setStatus("통보 완료");
                });
            }
            return noticeRepository.save(notice);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable String id) {
        noticeRepository.deleteById(id);
    }

    @GetMapping("/all")
    public List<Notice> getAllNotices(
            @RequestParam(required = false) String dateType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keywordType,
            @RequestParam(required = false) String keyword) {

        Iterable<Notice> all = noticeRepository.findAll();
        
        return StreamSupport.stream(all.spliterator(), false)
                .filter(n -> {
                    
                    if (type != null && !type.isEmpty() && !n.getType().equals(type)) return false;
                    
                    if (status != null && !status.isEmpty() && !n.getStatus().equals(status)) return false;
                    
                    if (keyword != null && !keyword.isEmpty()) {
                        String target = "";
                        if ("title".equals(keywordType)) target = n.getTitle();
                        else if ("summary".equals(keywordType)) target = n.getSummary();
                        else if ("content".equals(keywordType)) target = n.getContent();
                        if (target == null || !target.contains(keyword)) return false;
                    }
                    
                    if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                        String targetDate = "noticeDate".equals(dateType) ? n.getNoticeDate() : n.getRegDate();
                        if (targetDate != null) {
                            String compareDate = targetDate.substring(0, 10);
                            if (compareDate.compareTo(startDate) < 0 || compareDate.compareTo(endDate) > 0) return false;
                        }
                    }
                    return true;
                })
                .sorted(Comparator.comparing(Notice::getRegDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Notice getNotice(@PathVariable String id) {
        return noticeRepository.findById(id).orElse(null);
    }
}