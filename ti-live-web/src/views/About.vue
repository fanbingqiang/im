<template>
  <div class="about-page">
    <h2>关于系统</h2>
    <div class="about-content">
      <div class="about-section">
        <h3>系统简介</h3>
        <p>IM直播系统是一个集成了直播、点播、聊天等功能的综合性平台，为用户提供高质量的音视频直播和互动体验。</p>
      </div>
      <div class="about-section">
        <h3>核心功能</h3>
        <ul class="feature-list">
          <li>🎬 高清视频直播</li>
          <li>🎥 视频点播</li>
          <li>💬 实时聊天互动</li>
          <li>🎁 礼物赠送</li>
          <li>📺 弹幕效果</li>
          <li>👥 用户管理</li>
        </ul>
      </div>
      <div class="about-section">
        <h3>技术栈</h3>
        <div class="tech-stack">
          <div class="tech-item">
            <h4>前端</h4>
            <p>Vue 3 + Vite + Element Plus</p>
          </div>
          <div class="tech-item">
            <h4>后端</h4>
            <p>Spring Boot + Dubbo + Nacos</p>
          </div>
          <div class="tech-item">
            <h4>数据库</h4>
            <p>MySQL + Redis</p>
          </div>
          <div class="tech-item">
            <h4>流媒体</h4>
            <p>RTMP + HLS</p>
          </div>
        </div>
      </div>
      <div class="about-section">
        <h3>系统版本</h3>
        <p>当前版本：v1.0.0</p>
        <p>更新时间：2026-04-22</p>
      </div>
      <div class="about-section">
        <h3>联系我们</h3>
        <p>邮箱：support@imlive.com</p>
        <p>电话：400-123-4567</p>
        <p>地址：北京市朝阳区科技园</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// About组件
</script>

<style scoped>
.about-page {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.about-page h2 {
  font-size: 20px;
  color: #333;
  margin: 0 0 24px 0;
}

.about-content {
  max-width: 800px;
}

.about-section {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #e0e0e0;
}

.about-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.about-section h3 {
  font-size: 16px;
  color: #333;
  margin: 0 0 16px 0;
  font-weight: 500;
}

.about-section p {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  margin: 0 0 8px 0;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-list li {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
  padding-left: 24px;
  position: relative;
}

.feature-list li:before {
  content: '';
  position: absolute;
  left: 0;
  top: 4px;
  width: 16px;
  height: 16px;
  background: #409eff;
  border-radius: 50%;
}

.tech-stack {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.tech-item {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.tech-item h4 {
  font-size: 14px;
  color: #333;
  margin: 0 0 8px 0;
  font-weight: 500;
}

.tech-item p {
  font-size: 12px;
  color: #606266;
  margin: 0;
}

@media (max-width: 768px) {
  .tech-stack {
    grid-template-columns: 1fr;
  }
}
</style>
