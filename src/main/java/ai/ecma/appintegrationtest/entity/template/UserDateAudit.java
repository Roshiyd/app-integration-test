package ai.ecma.appintegrationtest.entity.template;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * OBJECTNI OCHGAN YOKI UNI
 * O'ZGARTIRGAN USERNI OLIB BERISH UCHUN XIZMAT QILADI
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class UserDateAudit extends DateAudit {

    @CreatedBy
    @JoinColumn(updatable = false)
    private UUID createdById;
    //OBJECTNI KIM OCHGANI.
    // AGAR U SECURITYCONTEXTDA BO'LSA YOZILADI AKS HOLDA NULL

    @LastModifiedBy
    private UUID updatedById;
    //OBJECTNI KIM O'ZGARTIRGANI.
    // AGAR U SECURITYCONTEXTDA BO'LSA YOZILADI AKS HOLDA NULL

}
