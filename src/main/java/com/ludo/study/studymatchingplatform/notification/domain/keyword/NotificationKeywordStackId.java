package com.ludo.study.studymatchingplatform.notification.domain.keyword;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationKeywordStackId implements Serializable {

	private Long userId;
	private Long stackId;

}
