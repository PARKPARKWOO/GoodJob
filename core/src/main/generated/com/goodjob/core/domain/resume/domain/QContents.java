package com.goodjob.core.domain.resume.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContents is a Querydsl query type for Contents
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QContents extends BeanPath<Contents> {

    private static final long serialVersionUID = -1061001959L;

    public static final QContents contents1 = new QContents("contents1");

    public final ListPath<String, StringPath> contents = this.<String, StringPath>createList("contents", String.class, StringPath.class, PathInits.DIRECT2);

    public QContents(String variable) {
        super(Contents.class, forVariable(variable));
    }

    public QContents(Path<? extends Contents> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContents(PathMetadata metadata) {
        super(Contents.class, metadata);
    }

}

