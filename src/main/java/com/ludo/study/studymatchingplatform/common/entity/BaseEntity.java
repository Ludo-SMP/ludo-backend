package com.ludo.study.studymatchingplatform.common.entity;

import com.ludo.study.studymatchingplatform.common.auditing.PrePersistUtcNow;
import com.ludo.study.studymatchingplatform.common.auditing.PreUpdateUtcNow;
import com.ludo.study.studymatchingplatform.common.auditing.UtcNowAuditor;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(UtcNowAuditor.class)
@ToString(of = "createdDateTime")
public abstract class BaseEntity {

    @PrePersistUtcNow
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @PreUpdateUtcNow
    @Column(nullable = false)
    private LocalDateTime updatedDateTime;

    @Column(nullable = true)
    @Builder.Default
    private LocalDateTime deletedDateTime = null;

    public boolean isDeleted() {
        return deletedDateTime != null;
    }

    public void softDelete() {
        deletedDateTime = LocalDateTime.now();
    }

    public void activate() {
        deletedDateTime = null;
    }

}
