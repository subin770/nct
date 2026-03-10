package com.example.elasticsearch_demo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "notice_final")
public class Notice {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Text)
    private String summary;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String action;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Keyword)
    private String author = "조수빈";

    @Field(type = FieldType.Text)
    private String regDate;

    @Field(type = FieldType.Text)
    private String noticeDate;

    @Field(type = FieldType.Keyword)
    private String fileName;

    @Field(type = FieldType.Text)
    private String relatedSite;

    @Field(type = FieldType.Text)
    private String targetOs;
    
    @Field(type = FieldType.Text)
    private String patchInfo;
}