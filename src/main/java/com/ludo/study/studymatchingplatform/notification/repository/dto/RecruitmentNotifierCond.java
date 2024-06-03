package com.ludo.study.studymatchingplatform.notification.repository.dto;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public record RecruitmentNotifierCond(User owner,
									  Category category,
									  List<Position> positions,
									  List<Stack> stacks
) {
}
