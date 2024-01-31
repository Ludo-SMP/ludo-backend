package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.service.exception.ServerException;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;

@Repository
public abstract class QuerydslRepositorySupporter {

	private final PathBuilder<?> builder;
	private Querydsl querydsl;
	private EntityManager entityManager;
	private JPAQueryFactory queryFactory;

	protected QuerydslRepositorySupporter(final Class<?> domainClass) {
		if (domainClass == null) {
			throw new ServerException("Domain class must not be null!");
		}
		this.builder = new PathBuilderFactory().create(domainClass);
	}

	@Autowired
	public void setEntityManager(final EntityManager entityManager) {
		if (entityManager == null) {
			throw new ServerException("EntityManager must not be null!");
		}
		this.entityManager = entityManager;
		this.querydsl = new Querydsl(entityManager, builder);
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@PostConstruct
	public void validate() {
		if (entityManager == null) {
			throw new ServerException("EntityManager must not be null!");
		}
		if (querydsl == null) {
			throw new ServerException("Querydsl must not be null!");
		}
		if (queryFactory == null) {
			throw new ServerException("QueryFactory must not be null!");
		}
	}

	protected <T> JPAQuery<T> selectFrom(final EntityPath<T> from) {
		return getQueryFactory().selectFrom(from);
	}

	protected JPAQueryFactory getQueryFactory() {
		return queryFactory;
	}

}
