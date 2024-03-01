package com.ludo.study.studymatchingplatform.study.repository.dto.request;

import java.util.List;

public record RecruitmentFindCond(Long categoryId, List<Long> stackIds, Long positionId, String way) {

}
