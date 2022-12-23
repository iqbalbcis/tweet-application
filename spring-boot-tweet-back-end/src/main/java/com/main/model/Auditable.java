package com.main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.Date;

import static com.main.constants.CommonConstant.UTC_TIME_ZONE;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Data
@EqualsAndHashCode
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @Column(name = "created_date", updatable = false)
    @Temporal(TIMESTAMP)
    @CreatedDate
    @JsonFormat(timezone = UTC_TIME_ZONE)
    protected Date creationDate;

    @Column(name = "last_modified_date")
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @JsonFormat(timezone = UTC_TIME_ZONE)
    protected Date lastModifiedDate;

    @CreatedBy
    @Column(name="created_by")
    protected String createdBy;

    @LastModifiedBy
    @Column(name="modified_by")
    protected String modifiedBy;

}
