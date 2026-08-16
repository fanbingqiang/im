<template>
  <div class="live-page">
    <h2>直播管理</h2>
    
    <!-- 直播列表 -->
    <div class="live-list">
      <div class="live-item" v-for="live in liveList" :key="live.streamId">
        <div class="live-cover">
          <img :src="live.coverUrl" :alt="live.title">
          <div class="live-status" :class="{ 'live': live.status === 1 }">
            {{ live.status === 1 ? '直播中' : '未开始' }}
          </div>
        </div>
        <div class="live-info">
          <h3 class="live-title">{{ live.title }}</h3>
          <div class="live-meta">
            <span class="anchor-name">{{ live.anchorName }}</span>
            <span class="viewer-count">观看人数: {{ live.viewerCount }}</span>
            <span class="start-time">开始时间: {{ formatTime(live.startTime) }}</span>
          </div>
          <div class="live-actions">
            <router-link :to="`/live-room/${live.roomId}?streamId=${live.streamId}`" class="watch-btn">
              观看
            </router-link>
            <button v-if="live.status === 1" class="end-btn">
              结束直播
            </button>
            <button v-else class="start-btn">
              开始直播
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 创建直播 -->
    <div class="create-live">
      <h3>创建新直播</h3>
      <form @submit.prevent="createLive">
        <div class="form-group">
          <label>直播标题</label>
          <input v-model="newLive.title" type="text" placeholder="请输入直播标题" required>
        </div>
        <div class="form-group">
          <label>房间名称</label>
          <input v-model="newLive.roomName" type="text" placeholder="请输入房间名称" required>
        </div>
        <div class="form-group">
          <label>直播封面</label>
          <input v-model="newLive.coverUrl" type="text" placeholder="请输入封面图片URL">
        </div>
        <button type="submit" class="create-btn">创建直播</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const liveList = ref([
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
    startTime: new Date(),
  },
  {
    streamId: 1002,
    anchorId: 10002,
    anchorName: '主播小红',
    roomId: 1002,
    roomName: '技术分享',
    title: 'Vue 3 实战教程',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=vue%203%20tutorial%20live%20cover&image_size=landscape_16_9',
    status: 0,
    viewerCount: 0,
    startTime: null,
  },
]);

const newLive = ref({
  title: '',
  roomName: '',
  coverUrl: '',
});

function createLive() {
  // 实际项目中应该调用API创建直播
  const newStream = {
    streamId: liveList.value.length + 1000,
    anchorId: 10001,
    anchorName: '主播小明',
    roomId: liveList.value.length + 1000,
    roomName: newLive.value.roomName,
    title: newLive.value.title,
    coverUrl: newLive.value.coverUrl || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=live%20stream%20cover&image_size=landscape_16_9',
    status: 0,
    viewerCount: 0,
    startTime: null,
  };
  
  liveList.value.push(newStream);
  
  // 重置表单
  newLive.value = {
    title: '',
    roomName: '',
    coverUrl: '',
  };
}

function formatTime(date) {
  if (!date) return '未开始';
  if (!(date instanceof Date)) {
    date = new Date(date);
  }
  return date.toLocaleString('zh-CN');
}
</script>

<style scoped>
.live-page {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.live-page h2 {
  font-size: 20px;
  color: #333;
  margin: 0 0 24px 0;
}

.live-list {
  margin-bottom: 32px;
}

.live-item {
  display: flex;
  gap: 20px;
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
  transition: all 0.3s;
}

.live-item:hover {
  background: #f5f7fa;
  border-radius: 8px;
}

.live-cover {
  position: relative;
  width: 200px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
}

.live-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.live-status {
  position: absolute;
  top: 8px;
  left: 8px;
  padding: 4px 12px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  border-radius: 12px;
  font-size: 12px;
}

.live-status.live {
  background: #ff4d4f;
}

.live-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.live-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 12px 0;
}

.live-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 14px;
  color: #606266;
  margin-bottom: 16px;
}

.live-actions {
  display: flex;
  gap: 12px;
}

.watch-btn, .start-btn, .end-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
}

.watch-btn {
  background: #409eff;
  color: #fff;
}

.watch-btn:hover {
  background: #66b1ff;
}

.start-btn {
  background: #67c23a;
  color: #fff;
}

.start-btn:hover {
  background: #85ce61;
}

.end-btn {
  background: #f56c6c;
  color: #fff;
}

.end-btn:hover {
  background: #f78989;
}

.create-live {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e0e0e0;
}

.create-live h3 {
  font-size: 16px;
  color: #333;
  margin: 0 0 16px 0;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
}

.form-group input:focus {
  border-color: #409eff;
}

.create-btn {
  padding: 10px 24px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.create-btn:hover {
  background: #66b1ff;
}

@media (max-width: 768px) {
  .live-item {
    flex-direction: column;
  }
  
  .live-cover {
    width: 100%;
    height: 200px;
  }
  
  .live-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
