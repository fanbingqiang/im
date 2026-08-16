<template>
  <div class="dashboard">
    <h2>系统仪表盘</h2>
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-content">
          <h3>在线用户</h3>
          <p class="stat-number">1,234</p>
          <p class="stat-change">+12% 较昨日</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📺</div>
        <div class="stat-content">
          <h3>直播中</h3>
          <p class="stat-number">23</p>
          <p class="stat-change">+5% 较昨日</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🎥</div>
        <div class="stat-content">
          <h3>点播视频</h3>
          <p class="stat-number">567</p>
          <p class="stat-change">+8% 较昨日</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">💬</div>
        <div class="stat-content">
          <h3>消息数</h3>
          <p class="stat-number">12,345</p>
          <p class="stat-change">+15% 较昨日</p>
        </div>
      </div>
    </div>

    <div class="recent-activity">
      <h3>最近活动</h3>
      <div class="activity-list">
        <div class="activity-item">
          <div class="activity-time">10:30</div>
          <div class="activity-content">
            <span class="activity-user">用户10001</span>
            <span class="activity-action">开始了直播</span>
            <span class="activity-target">《Vue 3 教程》</span>
          </div>
        </div>
        <div class="activity-item">
          <div class="activity-time">10:15</div>
          <div class="activity-content">
            <span class="activity-user">用户10002</span>
            <span class="activity-action">上传了视频</span>
            <span class="activity-target">《Spring Boot 微服务》</span>
          </div>
        </div>
        <div class="activity-item">
          <div class="activity-time">09:45</div>
          <div class="activity-content">
            <span class="activity-user">用户10003</span>
            <span class="activity-action">赠送了礼物</span>
            <span class="activity-target">给主播10001</span>
          </div>
        </div>
        <div class="activity-item">
          <div class="activity-time">09:30</div>
          <div class="activity-content">
            <span class="activity-user">系统</span>
            <span class="activity-action">服务器重启</span>
            <span class="activity-target">正常运行中</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Dashboard组件
</script>

<style scoped>
.dashboard {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.dashboard h2 {
  font-size: 20px;
  color: #333;
  margin: 0 0 24px 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s;
  border-left: 4px solid #409eff;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 32px;
  width: 60px;
  height: 60px;
  background: #ecf5ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-content {
  flex: 1;
}

.stat-content h3 {
  font-size: 14px;
  color: #606266;
  margin: 0 0 8px 0;
  font-weight: 500;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0 0 4px 0;
}

.stat-change {
  font-size: 12px;
  color: #67c23a;
  margin: 0;
}

.recent-activity {
  margin-top: 32px;
}

.recent-activity h3 {
  font-size: 16px;
  color: #333;
  margin: 0 0 16px 0;
}

.activity-list {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
}

.activity-item {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #e4e7ed;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  width: 80px;
  font-size: 12px;
  color: #909399;
  font-weight: 500;
}

.activity-content {
  flex: 1;
  font-size: 14px;
  color: #606266;
}

.activity-user {
  color: #409eff;
  font-weight: 500;
  margin-right: 8px;
}

.activity-action {
  margin-right: 8px;
}

.activity-target {
  color: #67c23a;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .activity-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
  
  .activity-time {
    width: auto;
  }
}
</style>
