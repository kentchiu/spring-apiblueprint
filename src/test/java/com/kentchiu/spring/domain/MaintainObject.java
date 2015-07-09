package com.kentchiu.spring.domain;

import com.kentchiu.spring.attribute.AttributeInfo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.Optional;


@MappedSuperclass
public abstract class MaintainObject {

    public static final Character TRUE = 'Y';
    public static final Character FALSE = 'N';
    /**
     * 资料有效码:Y 有效
     */
    public static char STATUS_AVAILABLE = 'Y';
    /**
     * 资料有效码: N 无效
     */
    public static char STATUS_UNAVAILABLE = 'N';
    /**
     * 资料有效码: X 作废
     */
    public static char STATUS_INVALID = 'X';
    /**
     * 已审/N 未审/R 退回
     */
    private Character confirm;
    private String uuid;
    private Date createDate;
    private String createUser;
    /**
     * 资料修改日期
     */
    private Date modiDate;
    /**
     * 资料修改者
     */
    private String modiUser;
    /**
     * 资料有效码:Y 有效/N 无效/X 作废
     */
    private Character status;

    public static <T> Optional<T> findByUuid(Map<String, Object> params, JpaRepository<T, String> dao, String uuid) {
        String id = (String) params.getOrDefault(uuid, "");
        if (StringUtils.isNotBlank(id)) {
            return Optional.ofNullable(dao.findOne(id));
        } else {
            return Optional.empty();
        }
    }

    @AttributeInfo
    public Character getConfirm() {
        return confirm;
    }

    public void setConfirm(Character confirm) {
        this.confirm = confirm;
    }

    @AttributeInfo
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    @AttributeInfo(description = "资料创建日期", format = "yyyy/MM/dd HH:mm:ss", defaultValue = "now")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @AttributeInfo(description = "资料创建者")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @AttributeInfo
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getModiDate() {
        return modiDate;
    }

    public void setModiDate(Date modiDate) {
        this.modiDate = modiDate;
    }

    @AttributeInfo
    public String getModiUser() {
        return modiUser;
    }

    public void setModiUser(String modiUser) {
        this.modiUser = modiUser;
    }

    @AttributeInfo
    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaintainObject that = (MaintainObject) o;

        return !(uuid != null ? !uuid.equals(that.uuid) : that.uuid != null);

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MaintainObject{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
