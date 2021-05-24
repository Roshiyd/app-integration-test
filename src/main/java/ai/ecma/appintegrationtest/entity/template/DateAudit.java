package ai.ecma.appintegrationtest.entity.template;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * OBJECTLAR DB GA QO'SHILGANDA YOKI O'ZGARTIRILGANDA
 * AVTOMAT RAVISHDA O'SHA VAQTNI OLISHI UCHUN
 */
@Data
@MappedSuperclass
public abstract class DateAudit implements Serializable {
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;//OBJECT YANGI OCHIGANDA ISHLATILADI

    @UpdateTimestamp
    private Timestamp updatedAt;//OBJECT O'ZGARGANDA ISHLAYDI

}
