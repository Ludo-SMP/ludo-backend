package com.ludo.study.studymatchingplatform.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@ToString(of = "createdDateTime")
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDateTime;

    @Column(nullable = true)
    @Builder.Default
    private LocalDateTime deletedDateTime = null;

    public boolean isDeleted() {
        return deletedDateTime != null;
    }

    public void softDelete(final LocalDateTime deletedDateTime) {
        if (deletedDateTime == null) {
            throw new IllegalArgumentException("deletedDateTime must not be `null`");
        }
        this.deletedDateTime = deletedDateTime;
    }

    public void activate() {
        deletedDateTime = null;
    }

}
