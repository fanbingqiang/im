<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="logo">
        <h1>IM直播系统</h1>
      </div>
      <nav class="menu">
        <router-link to="/" class="menu-item">
          <span class="menu-icon">📊</span>
          <span class="menu-text">仪表盘</span>
        </router-link>
        <router-link to="/live" class="menu-item">
          <span class="menu-icon">📺</span>
          <span class="menu-text">直播管理</span>
        </router-link>
        <router-link to="/broadcast" class="menu-item">
          <span class="menu-icon">🎬</span>
          <span class="menu-text">放映厅</span>
        </router-link>
        <router-link to="/vod" class="menu-item">
          <span class="menu-icon">🎥</span>
          <span class="menu-text">视频点播</span>
        </router-link>
        <router-link to="/chatroom" class="menu-item">
          <span class="menu-icon">💬</span>
          <span class="menu-text">聊天室</span>
        </router-link>
        <router-link to="/about" class="menu-item">
          <span class="menu-icon">ℹ️</span>
          <span class="menu-text">关于</span>
        </router-link>
      </nav>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部导航 -->
      <header class="header">
        <div class="user-info">
          <span class="user-name">用户10001</span>
          <button class="logout-btn">退出</button>
        </div>
      </header>

      <!-- 路由视图 -->
      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
// Layout组件
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  background: #f5f7fa;
}

.sidebar {
  width: 200px;
  background: #409eff;
  color: #fff;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.1);
}

.logo {
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.logo h1 {
  font-size: 18px;
  margin: 0;
  font-weight: bold;
  text-align: center;
}

.menu {
  flex: 1;
  padding: 20px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #fff;
  text-decoration: none;
  transition: all 0.3s;
  border-left: 3px solid transparent;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
  border-left-color: #fff;
}

.menu-item.router-link-active {
  background: rgba(255, 255, 255, 0.2);
  border-left-color: #fff;
  font-weight: bold;
}

.menu-icon {
  font-size: 18px;
  margin-right: 12px;
  width: 20px;
  text-align: center;
}

.menu-text {
  font-size: 14px;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-name {
  font-size: 14px;
  color: #333;
}

.logout-btn {
  padding: 6px 12px;
  background: #f56c6c;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  background: #f78989;
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .sidebar {
    width: 60px;
  }
  
  .menu-text {
    display: none;
  }
  
  .logo h1 {
    font-size: 12px;
  }
}
</style>
