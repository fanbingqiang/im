<template>
  <div class="vod-page">
    <!-- 页面头部 -->
    <div class="vod-header">
      <h1>视频点播</h1>
      <div class="search-box">
        <input v-model="searchKeyword" type="text" placeholder="搜索视频..." />
        <button @click="searchVideos">搜索</button>
      </div>
    </div>

    <!-- 视频播放区域 -->
    <div class="play-area" v-if="selectedVideo">
      <div class="video-player">
        <video ref="videoPlayer" controls>
          <source :src="selectedVideo.videoUrl" type="video/mp4">
          您的浏览器不支持视频播放
        </video>
      </div>
      <div class="video-info">
        <h2>{{ selectedVideo.title }}</h2>
        <div class="video-meta">
          <span class="uploader">{{ selectedVideo.userName }}</span>
          <span class="play-count">播放 {{ selectedVideo.playCount }} 次</span>
          <span class="duration">{{ formatDuration(selectedVideo.duration) }}</span>
          <span class="upload-time">{{ formatTime(selectedVideo.createTime) }}</span>
        </div>
        <div class="video-description">
          {{ selectedVideo.description }}
        </div>
        <div class="video-tags" v-if="selectedVideo.tags">
          <span v-for="tag in selectedVideo.tags.split(',')" :key="tag" class="tag">
            {{ tag }}
          </span>
        </div>
      </div>
    </div>

    <!-- 视频列表区域 -->
    <div class="video-list">
      <h3>推荐视频</h3>
      <div class="video-grid">
        <div v-for="video in videos" :key="video.videoId" class="video-item" @click="selectVideo(video)">
          <div class="video-thumbnail">
            <img :src="video.coverUrl" :alt="video.title">
            <span class="video-duration">{{ formatDuration(video.duration) }}</span>
          </div>
          <div class="video-item-info">
            <h4 class="video-title">{{ video.title }}</h4>
            <div class="video-item-meta">
              <span class="video-uploader">{{ video.userName }}</span>
              <span class="video-item-playcount">{{ video.playCount }}次播放</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const searchKeyword = ref('');
const videos = ref([]);
const selectedVideo = ref(null);
const videoPlayer = ref(null);

// 模拟视频数据
function loadVideos() {
  videos.value = [
    {
      videoId: 1,
      userId: 10001,
      userName: '用户1',
      title: 'Vue 3 入门教程',
      description: 'Vue 3 基础教程，包括组合式API、响应式系统等核心概念',
      coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=vue%203%20tutorial%20thumbnail&image_size=landscape_16_9',
      videoUrl: 'https://samplelib.com/lib/preview/mp4/sample-5s.mp4',
      duration: 600,
      playCount: 1234,
      status: 1,
      tags: '前端,Vue,教程',
      createTime: new Date('2026-04-20 10:00:00'),
    },
    {
      videoId: 2,
      userId: 10002,
      userName: '用户2',
      title: 'Spring Boot 微服务开发',
      description: 'Spring Boot 微服务架构实战，包括Dubbo、Nacos等技术栈',
      coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=spring%20boot%20microservice%20thumbnail&image_size=landscape_16_9',
      videoUrl: 'https://samplelib.com/lib/preview/mp4/sample-9s.mp4',
      duration: 900,
      playCount: 5678,
      status: 1,
      tags: '后端,Spring Boot,微服务',
      createTime: new Date('2026-04-19 15:30:00'),
    },
    {
      videoId: 3,
      userId: 10001,
      userName: '用户1',
      title: 'Redis 高级特性',
      description: 'Redis 高级特性讲解，包括哨兵模式、集群、持久化等',
      coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=redis%20advanced%20features%20thumbnail&image_size=landscape_16_9',
      videoUrl: 'https://samplelib.com/lib/preview/mp4/sample-15s.mp4',
      duration: 720,
      playCount: 3456,
      status: 1,
      tags: '后端,Redis,缓存',
      createTime: new Date('2026-04-18 09:15:00'),
    },
  ];
  
  // 默认选择第一个视频
  if (videos.value.length > 0) {
    selectedVideo.value = videos.value[0];
  }
}

// 选择视频
function selectVideo(video) {
  selectedVideo.value = video;
  // 增加播放次数（实际项目中应该调用API）
  video.playCount++;
  
  // 自动播放
  if (videoPlayer.value) {
    videoPlayer.value.play();
  }
}

// 搜索视频
function searchVideos() {
  // 实际项目中应该调用API搜索
  console.log('搜索视频:', searchKeyword.value);
  // 这里简单模拟搜索
  if (searchKeyword.value) {
    const filtered = videos.value.filter(video => 
      video.title.includes(searchKeyword.value) ||
      video.description.includes(searchKeyword.value) ||
      video.tags.includes(searchKeyword.value)
    );
    if (filtered.length > 0) {
      selectedVideo.value = filtered[0];
    }
  }
}

// 格式化时长
function formatDuration(seconds) {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
}

// 格式化时间
function formatTime(date) {
  if (!(date instanceof Date)) {
    date = new Date(date);
  }
  return date.toLocaleString('zh-CN');
}

onMounted(() => {
  loadVideos();
});
</script>

<style scoped>
.vod-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.vod-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e0e0e0;
}

.vod-header h1 {
  font-size: 28px;
  color: #333;
  margin: 0;
}

.search-box {
  display: flex;
  gap: 8px;
}

.search-box input {
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  width: 300px;
}

.search-box button {
  padding: 8px 16px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.play-area {
  display: flex;
  gap: 24px;
  margin-bottom: 32px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.video-player {
  flex: 1;
  min-width: 600px;
}

.video-player video {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.video-info {
  width: 360px;
}

.video-info h2 {
  font-size: 20px;
  color: #333;
  margin: 0 0 12px 0;
}

.video-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #909399;
}

.video-description {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  margin-bottom: 16px;
}

.video-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  padding: 2px 8px;
  background: #ecf5ff;
  color: #409eff;
  border-radius: 12px;
  font-size: 12px;
}

.video-list {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.video-list h3 {
  font-size: 18px;
  color: #333;
  margin: 0 0 16px 0;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.video-item {
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
  background: #f9f9f9;
}

.video-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.video-thumbnail {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 比例 */
  overflow: hidden;
}

.video-thumbnail img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
}

.video-item-info {
  padding: 12px;
}

.video-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.video-item-meta {
  font-size: 12px;
  color: #909399;
  display: flex;
  justify-content: space-between;
}

@media (max-width: 1024px) {
  .play-area {
    flex-direction: column;
  }
  
  .video-player {
    min-width: 100%;
  }
  
  .video-info {
    width: 100%;
  }
  
  .video-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  }
}

@media (max-width: 768px) {
  .vod-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .search-box input {
    width: 100%;
  }
  
  .video-player video {
    height: 300px;
  }
  
  .video-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
}
</style>
