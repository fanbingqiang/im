package com.provider.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDO 单元测试类
 */
public class UserDOTest {

    private UserDO user;

    @BeforeEach
    void setUp() {
        user = new UserDO();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("123456");
        user.setNickname("测试用户");
        user.setEmail("test@example.com");
        user.setPhone("13800138000");
        user.setGender(1);
        user.setStatus(1);
        user.setUserType(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("测试用户", user.getNickname());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("13800138000", user.getPhone());
        assertEquals(1, user.getGender());
        assertEquals(1, user.getStatus());
        assertEquals(1, user.getUserType());
        assertEquals(0, user.getDeleted());
    }

    @Test
    void testIsEnabled() {
        user.setStatus(1);
        assertTrue(user.isEnabled());

        user.setStatus(0);
        assertFalse(user.isEnabled());
    }

    @Test
    void testIsDisabled() {
        user.setStatus(0);
        assertTrue(user.isDisabled());

        user.setStatus(1);
        assertFalse(user.isDisabled());
    }

    @Test
    void testIsVip() {
        user.setUserType(2);
        assertTrue(user.isVip());

        user.setUserType(1);
        assertFalse(user.isVip());
    }

    @Test
    void testIsAdmin() {
        user.setUserType(3);
        assertTrue(user.isAdmin());

        user.setUserType(1);
        assertFalse(user.isAdmin());
    }

    @Test
    void testGetDisplayName() {
        // 有昵称时返回昵称
        user.setNickname("测试昵称");
        assertEquals("测试昵称", user.getDisplayName());

        // 昵称为空时返回用户名
        user.setNickname("");
        assertEquals("testuser", user.getDisplayName());

        // 昵称为null时返回用户名
        user.setNickname(null);
        assertEquals("testuser", user.getDisplayName());
    }

    @Test
    void testGetMaskedPhone() {
        // 正常手机号
        user.setPhone("13800138000");
        assertEquals("138****8000", user.getMaskedPhone());

        // 非11位手机号
        user.setPhone("123456");
        assertEquals("123456", user.getMaskedPhone());

        // null手机号
        user.setPhone(null);
        assertNull(user.getMaskedPhone());
    }

    @Test
    void testGetMaskedEmail() {
        // 正常邮箱
        user.setEmail("test@example.com");
        assertEquals("te***@example.com", user.getMaskedEmail());

        // 短前缀邮箱
        user.setEmail("ab@example.com");
        assertEquals("ab@example.com", user.getMaskedEmail());

        // 无效邮箱
        user.setEmail("invalid-email");
        assertEquals("invalid-email", user.getMaskedEmail());

        // null邮箱
        user.setEmail(null);
        assertNull(user.getMaskedEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        UserDO user1 = new UserDO();
        user1.setId(1L);
        user1.setUsername("user1");

        UserDO user2 = new UserDO();
        user2.setId(1L);
        user2.setUsername("user1");

        UserDO user3 = new UserDO();
        user3.setId(2L);
        user3.setUsername("user2");

        // 测试 equals
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);

        // 测试 hashCode
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        String str = user.toString();
        assertNotNull(str);
        assertTrue(str.contains("UserDO"));
        assertTrue(str.contains("testuser"));
        // 验证手机号已脱敏
        assertFalse(str.contains("13800138000"));
        assertTrue(str.contains("138****8000"));
    }

    @Test
    void testDefaultValues() {
        UserDO newUser = new UserDO();
        assertNull(newUser.getId());
        assertNull(newUser.getUsername());
        assertNull(newUser.getStatus());
        assertNull(newUser.getDeleted());
    }

    @Test
    void testSerialVersionUID() {
        // 验证 serialVersionUID 常量是否存在
        assertEquals(1L, UserDO.serialVersionUID);
    }
}
