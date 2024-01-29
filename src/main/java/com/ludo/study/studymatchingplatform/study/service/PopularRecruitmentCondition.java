package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.Getter;

@Getter
public class PopularRecruitmentCondition {

	private final PageRequest condition = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "hits"));

}
