package com.provider.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类 (Data Object)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class UserDO implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 用户类型：1-普通用户，2-VIP用户，3-管理员
     */
    private Integer userType;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;

    // ==================== 业务方法 ====================

    /**
     * 检查用户是否启用
     */
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    /**
     * 检查用户是否禁用
     */
    public boolean isDisabled() {
        return status != null && status == 0;
    }

    /**
     * 检查是否是VIP用户
     */
    public boolean isVip() {
        return userType != null && userType == 2;
    }

    /**
     * 检查是否是管理员
     */
    public boolean isAdmin() {
        return userType != null && userType == 3;
    }

    /**
     * 获取用户显示名称（优先返回昵称，其次用户名）
     */
    public String getDisplayName() {
        if (nickname != null && !nickname.isEmpty()) {
            return nickname;
        }
        return username;
    }

    /**
     * 获取脱敏后的手机号
     */
    public String getMaskedPhone() {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 获取脱敏后的邮箱
     */
    public String getMaskedEmail() {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);
        if (prefix.length() <= 2) {
            return prefix + suffix;
        }
        return prefix.substring(0, 2) + "***" + suffix;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + getMaskedPhone() + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", userType=" + userType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                '}';
    }
}
