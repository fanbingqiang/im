<template>
  <div class="broadcast-page">
    <h2>放映厅</h2>
    
    <!-- 推荐直播 -->
    <div class="recommended-lives">
      <h3>推荐直播</h3>
      <div class="live-grid">
        <div class="live-card" v-for="live in recommendedLives" :key="live.streamId">
          <div class="live-card-cover">
            <img :src="live.coverUrl" :alt="live.title">
            <div class="live-badge" v-if="live.status === 1">直播中</div>
            <div class="viewer-count">{{ live.viewerCount }} 人观看</div>
          </div>
          <div class="live-card-info">
            <h4 class="live-card-title">{{ live.title }}</h4>
            <div class="live-card-meta">
              <span class="anchor-name">{{ live.anchorName }}</span>
              <span class="room-name">{{ live.roomName }}</span>
            </div>
            <router-link :to="`/live-room/${live.roomId}?streamId=${live.streamId}`" class="watch-btn">
              观看
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 分类筛选 -->
    <div class="category-filter">
      <h3>分类筛选</h3>
      <div class="category-tabs">
        <button 
          v-for="category in categories" 
          :key="category.id" 
          :class="{ active: selectedCategory === category.id }"
          @click="selectedCategory = category.id"
        >
          {{ category.name }}
        </button>
      </div>
    </div>

    <!-- 热门视频 -->
    <div class="hot-videos">
      <h3>热门视频</h3>
      <div class="video-grid">
        <div class="video-card" v-for="video in hotVideos" :key="video.videoId">
          <div class="video-card-cover">
            <img :src="video.coverUrl" :alt="video.title">
            <span class="video-duration">{{ formatDuration(video.duration) }}</span>
          </div>
          <div class="video-card-info">
            <h4 class="video-card-title">{{ video.title }}</h4>
            <div class="video-card-meta">
              <span class="uploader">{{ video.userName }}</span>
              <span class="play-count">{{ video.playCount }}次播放</span>
            </div>
            <router-link :to="`/vod?id=${video.videoId}`" class="watch-btn">
              观看
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const recommendedLives = ref([
  {
    streamId: 1001,
    anchorId: 10001,
    anchorName: '主播小明',
    roomId: 1001,
    roomName: '欢乐直播间',
    title: '欢迎来到我的直播间！',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=live%20stream%20cover%20with%20blue%20theme&image_size=landscape_16_9',
    status: 1,
    viewerCount: 1234,
  },
  {
    streamId: 1002,
    anchorId: 10002,
    anchorName: '主播小红',
    roomId: 1002,
    roomName: '技术分享',
    title: 'Vue 3 实战教程',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=vue%203%20tutorial%20live%20cover&image_size=landscape_16_9',
    status: 1,
    viewerCount: 892,
  },
  {
    streamId: 1003,
    anchorId: 10003,
    anchorName: '主播小张',
    roomId: 1003,
    roomName: '游戏直播',
    title: '王者荣耀排位赛',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=game%20live%20stream%20cover&image_size=landscape_16_9',
    status: 0,
    viewerCount: 0,
  },
]);

const hotVideos = ref([
  {
    videoId: 1,
    userId: 10001,
    userName: '用户1',
    title: 'Vue 3 入门教程',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=vue%203%20tutorial%20thumbnail&image_size=landscape_16_9',
    duration: 600,
    playCount: 1234,
  },
  {
    videoId: 2,
    userId: 10002,
    userName: '用户2',
    title: 'Spring Boot 微服务开发',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=spring%20boot%20microservice%20thumbnail&image_size=landscape_16_9',
    duration: 900,
    playCount: 5678,
  },
  {
    videoId: 3,
    userId: 10001,
    userName: '用户1',
    title: 'Redis 高级特性',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=redis%20advanced%20features%20thumbnail&image_size=landscape_16_9',
    duration: 720,
    playCount: 3456,
  },
]);

const categories = ref([
  { id: 0, name: '全部' },
  { id: 1, name: '游戏' },
  { id: 2, name: '科技' },
  { id: 3, name: '娱乐' },
  { id: 4, name: '教育' },
]);

const selectedCategory = ref(0);

function formatDuration(seconds) {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
}
</script>

<style scoped>
.broadcast-page {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.broadcast-page h2 {
  font-size: 20px;
  color: #333;
  margin: 0 0 24px 0;
}

.recommended-lives,
.category-filter,
.hot-videos {
  margin-bottom: 32px;
}

.recommended-lives h3,
.category-filter h3,
.hot-videos h3 {
  font-size: 16px;
  color: #333;
  margin: 0 0 16px 0;
}

.live-grid,
.video-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.live-card,
.video-card {
  background: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
}

.live-card:hover,
.video-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.live-card-cover,
.video-card-cover {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 比例 */
  overflow: hidden;
}

.live-card-cover img,
.video-card-cover img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.live-badge {
  position: absolute;
  top: 8px;
  left: 8px;
  padding: 4px 12px;
  background: #ff4d4f;
  color: #fff;
  border-radius: 12px;
  font-size: 12px;
}

.viewer-count {
  position: absolute;
  bottom: 8px;
  right: 8px;
  padding: 4px 8px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  border-radius: 4px;
  font-size: 12px;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  padding: 4px 8px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  border-radius: 4px;
  font-size: 12px;
}

.live-card-info,
.video-card-info {
  padding: 16px;
}

.live-card-title,
.video-card-title {
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

.live-card-meta,
.video-card-meta {
  font-size: 12px;
  color: #909399;
  margin-bottom: 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.watch-btn {
  display: inline-block;
  padding: 6px 16px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  text-decoration: none;
  transition: all 0.3s;
  cursor: pointer;
}

.watch-btn:hover {
  background: #66b1ff;
}

.category-tabs {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.category-tabs button {
  padding: 8px 16px;
  background: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-tabs button:hover {
  background: #ecf5ff;
  border-color: #c6e2ff;
}

.category-tabs button.active {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

@media (max-width: 768px) {
  .live-grid,
  .video-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
  
  .category-tabs {
    justify-content: center;
  }
}
</style>
