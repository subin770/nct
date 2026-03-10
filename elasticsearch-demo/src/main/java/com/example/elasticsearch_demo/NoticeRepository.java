package com.example.elasticsearch_demo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends ElasticsearchRepository<Notice, String> {
}