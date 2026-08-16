<template>
  <div class="live-room">
    <!-- 直播播放器区域 -->
    <div class="live-player">
      <video ref="videoPlayer" class="player" controls autoplay>
        <source :src="liveStream?.pullUrl" type="application/x-mpegURL">
        您的浏览器不支持直播播放器
      </video>
      <div class="player-overlay">
        <div class="live-info">
          <h2>{{ liveStream?.title }}</h2>
          <div class="anchor-info">
            <img :src="liveStream?.coverUrl" class="anchor-avatar" alt="主播头像">
            <div>
              <span class="anchor-name">{{ liveStream?.anchorName }}</span>
              <span class="viewer-count">观看人数: {{ liveStream?.viewerCount }}</span>
            </div>
            <button class="follow-btn">关注</button>
          </div>
        </div>
        <div class="player-controls">
          <button @click="toggleBarrage" :class="{ active: barrageEnabled }">
            {{ barrageEnabled ? '关闭弹幕' : '开启弹幕' }}
          </button>
          <button @click="toggleFullscreen">全屏</button>
        </div>
      </div>
    </div>

    <!-- 聊天和礼物区域 -->
    <div class="live-sidebar">
      <!-- 聊天区域 -->
      <div class="chat-section">
        <ChatRoom :roomId="roomId" />
      </div>

      <!-- 礼物区域 -->
      <div class="gift-section">
        <div class="gift-header">
          <span>礼物</span>
          <button @click="toggleGiftPanel" class="gift-toggle">
            {{ showGiftPanel ? '收起' : '展开' }}
          </button>
        </div>
        <div v-if="showGiftPanel" class="gift-panel">
          <div v-for="gift in gifts" :key="gift.id" class="gift-item" @click="selectGift(gift)">
            <img :src="gift.imageUrl" :alt="gift.name">
            <span class="gift-name">{{ gift.name }}</span>
            <span class="gift-price">{{ gift.price }} 币</span>
          </div>
          <div v-if="selectedGift" class="gift-send">
            <span>数量: {{ giftCount }}</span>
            <div class="gift-count-control">
              <button @click="giftCount > 1 && giftCount--">&minus;</button>
              <span>{{ giftCount }}</span>
              <button @click="giftCount < 99 && giftCount++">+</button>
            </div>
            <button @click="sendGift" class="send-gift-btn">赠送</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 弹幕效果 -->
    <div v-if="barrageEnabled" class="barrage-container">
      <div v-for="(barrage, index) in barrages" :key="index" class="barrage" :style="barrage.style">
        {{ barrage.content }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';
import ChatRoom from './ChatRoom.vue';

const props = defineProps({
  roomId: { type: [Number, String], default: 1001 },
  streamId: { type: [Number, String], default: null },
});

const videoPlayer = ref(null);
const liveStream = ref(null);
const barrageEnabled = ref(true);
const showGiftPanel = ref(false);
const selectedGift = ref(null);
const giftCount = ref(1);
const barrages = ref([]);

// 礼物列表
const gifts = ref([
  { id: 1, name: '鲜花', price: 1, imageUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=flower%20gift%20icon&image_size=square' },
  { id: 2, name: '掌声', price: 5, imageUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=clapping%20hands%20gift%20icon&image_size=square' },
  { id: 3, name: '火箭', price: 100, imageUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=rocket%20gift%20icon&image_size=square' },
  { id: 4, name: '跑车', price: 500, imageUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sports%20car%20gift%20icon&image_size=square' },
]);

// 模拟获取直播流信息
function fetchLiveStream() {
  // 实际项目中应该从API获取
  liveStream.value = {
    streamId: props.streamId || 1001,
    anchorId: 10001,
    anchorName: '主播小明',
    roomId: props.roomId,
    roomName: '欢乐直播间',
    title: '欢迎来到我的直播间！',
    coverUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=live%20stream%20cover%20with%20blue%20theme&image_size=square_hd',
    pushUrl: 'rtmp://localhost:1935/live/stream_10001_1234567890',
    pullUrl: 'http://localhost:8080/hls/stream_10001_1234567890.m3u8',
    status: 1,
    viewerCount: 1234,
  };
}

// 切换弹幕
function toggleBarrage() {
  barrageEnabled.value = !barrageEnabled.value;
}

// 切换全屏
function toggleFullscreen() {
  if (!videoPlayer.value) return;
  
  if (!document.fullscreenElement) {
    videoPlayer.value.requestFullscreen().catch(err => {
      console.error(`全屏错误: ${err.message}`);
    });
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    }
  }
}

// 切换礼物面板
function toggleGiftPanel() {
  showGiftPanel.value = !showGiftPanel.value;
}

// 选择礼物
function selectGift(gift) {
  selectedGift.value = gift;
  giftCount.value = 1;
}

// 发送礼物
function sendGift() {
  if (!selectedGift.value) return;
  
  // 实际项目中应该通过WebSocket发送礼物消息
  console.log('发送礼物:', selectedGift.value.name, 'x', giftCount.value);
  
  // 模拟发送礼物效果
  showGiftAnimation(selectedGift.value);
  selectedGift.value = null;
  giftCount.value = 1;
}

// 礼物动画效果
function showGiftAnimation(gift) {
  // 实际项目中应该实现更复杂的礼物动画
  alert(`赠送了 ${gift.name} x ${giftCount.value}`);
}

// 处理弹幕
function addBarrage(content) {
  if (!barrageEnabled.value) return;
  
  const barrage = {
    content,
    style: {
      top: `${Math.random() * 80}%`,
      animationDuration: `${8 + Math.random() * 4}s`,
      opacity: 0,
      animationDelay: '0s',
    },
  };
  
  barrages.value.push(barrage);
  
  // 3秒后移除弹幕
  setTimeout(() => {
    barrages.value.shift();
  }, 12000);
}

onMounted(() => {
  fetchLiveStream();
  
  // 模拟接收弹幕
  setInterval(() => {
    if (Math.random() > 0.7) {
      addBarrage(`这是一条测试弹幕 ${Math.floor(Math.random() * 1000)}`);
    }
  }, 2000);
});

onUnmounted(() => {
  // 清理资源
});
</script>

<style scoped>
.live-room {
  display: flex;
  height: 100vh;
  background: #000;
  position: relative;
  overflow: hidden;
}

.live-player {
  flex: 1;
  position: relative;
  min-width: 70%;
}

.player {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.player-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.8), transparent 30%, transparent 70%, rgba(0,0,0,0.3));
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  color: #fff;
}

.live-info h2 {
  font-size: 24px;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.5);
}

.anchor-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.anchor-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 2px solid #fff;
}

.anchor-name {
  font-size: 16px;
  font-weight: bold;
  margin-right: 16px;
}

.viewer-count {
  font-size: 14px;
  opacity: 0.8;
}

.follow-btn {
  margin-left: auto;
  padding: 6px 16px;
  background: #ff4d4f;
  color: #fff;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
}

.player-controls {
  display: flex;
  gap: 12px;
}

.player-controls button {
  padding: 8px 16px;
  background: rgba(0,0,0,0.6);
  color: #fff;
  border: 1px solid rgba(255,255,255,0.3);
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.player-controls button:hover {
  background: rgba(255,255,255,0.2);
}

.player-controls button.active {
  background: #409eff;
  border-color: #409eff;
}

.live-sidebar {
  width: 360px;
  background: #f5f5f5;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #e0e0e0;
}

.chat-section {
  flex: 1;
  padding: 12px;
  overflow: hidden;
}

.gift-section {
  border-top: 1px solid #e0e0e0;
  padding: 12px;
  background: #fff;
}

.gift-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  font-weight: bold;
}

.gift-toggle {
  padding: 4px 12px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 12px;
  cursor: pointer;
}

.gift-panel {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

.gift-item {
  width: 70px;
  text-align: center;
  cursor: pointer;
  padding: 8px;
  border-radius: 6px;
  transition: all 0.2s;
}

.gift-item:hover {
  background: #e6f7ff;
}

.gift-item img {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  margin-bottom: 4px;
}

.gift-name {
  display: block;
  font-size: 12px;
  color: #333;
  margin-bottom: 2px;
}

.gift-price {
  display: block;
  font-size: 11px;
  color: #ff4d4f;
}

.gift-send {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.gift-count-control {
  display: flex;
  align-items: center;
  gap: 8px;
}

.gift-count-control button {
  width: 24px;
  height: 24px;
  border: 1px solid #dcdfe6;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.gift-count-control span {
  min-width: 30px;
  text-align: center;
  font-size: 14px;
}

.send-gift-btn {
  margin-left: auto;
  padding: 6px 20px;
  background: #ff4d4f;
  color: #fff;
  border: none;
  border-radius: 16px;
  font-size: 14px;
  cursor: pointer;
}

.barrage-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
  z-index: 10;
}

.barrage {
  position: absolute;
  white-space: nowrap;
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  text-shadow: 0 1px 2px rgba(0,0,0,0.8);
  animation: barrage 10s linear;
  left: 100%;
  opacity: 1;
}

@keyframes barrage {
  0% {
    left: 100%;
    opacity: 1;
  }
  100% {
    left: -100%;
    opacity: 0;
  }
}

@media (max-width: 1200px) {
  .live-sidebar {
    width: 300px;
  }
}

@media (max-width: 768px) {
  .live-room {
    flex-direction: column;
  }
  
  .live-player {
    height: 70vh;
    min-width: 100%;
  }
  
  .live-sidebar {
    width: 100%;
    height: 30vh;
    border-left: none;
    border-top: 1px solid #e0e0e0;
  }
}
</style>
