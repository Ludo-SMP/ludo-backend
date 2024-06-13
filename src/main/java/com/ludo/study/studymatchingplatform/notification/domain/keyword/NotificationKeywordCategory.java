package com.ludo.study.studymatchingplatform.notification.domain.keyword;

import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class NotificationKeywordCategory {

	@EmbeddedId
	private NotificationKeywordCategoryId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("categoryId")
	@JoinColumn(name = "category_id")
	private Category category;

	/**
	 * do not initialize: new NotificationKeywordCategory();
	 * should use this static method
	 */
	public static NotificationKeywordCategory of(final User user, final Category category) {
		final NotificationKeywordCategoryId id = new NotificationKeywordCategoryId(user.getId(), category.getId());
		return new NotificationKeywordCategory(id, user, category);
	}

	public Long getCategoryId() {
		return category.getId();
	}

}
