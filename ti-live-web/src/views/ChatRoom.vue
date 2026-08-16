<template>
  <div class="chat-room">
    <div class="chat-header">
      <span>直播间聊天</span>
      <span class="online-count">在线: {{ onlineCount }}</span>
    </div>

    <div ref="messageList" class="message-list">
      <div v-if="messages.length === 0" class="empty-tip">暂无消息</div>
      <div v-for="(msg, index) in messages" :key="index"
             :class="['message-item', msg.msgType === 4 ? 'system' : '', msg.msgType === 3 ? 'gift' : '']">
          <div v-if="msg.msgType === 4" class="system-msg">
            {{ msg.content }}
          </div>
          <div v-else-if="msg.msgType === 3" class="gift-msg">
            <span class="sender-name">{{ msg.senderName || '用户' + msg.senderId }}</span>
            <span class="gift-name">赠送了</span>
            <span class="gift-item">
              <img v-if="msg.gift?.imageUrl" :src="msg.gift.imageUrl" :alt="msg.gift.giftName" class="gift-image">
              {{ msg.gift?.giftName || '礼物' }}
              <span class="gift-count">x{{ msg.gift?.count || 1 }}</span>
            </span>
            <span class="gift-price">{{ msg.gift?.price || 0 }} 币</span>
          </div>
          <div v-else class="user-msg" :class="{ 'barrage': msg.isBarrage }">
            <span class="sender-name">{{ msg.senderName || '用户' + msg.senderId }}</span>
            <span class="msg-content">{{ msg.content }}</span>
          </div>
        </div>
    </div>

    <div class="chat-input">
      <input v-model="inputMsg" type="text" placeholder="输入消息..."
             @keyup.enter="sendMessage" maxlength="200" />
      <button @click="sendMessage" :disabled="!connected || !inputMsg.trim()">
        发送
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';

const props = defineProps({
  roomId: { type: [Number, String], default: 1001 },
});

const messages = ref([]);
const inputMsg = ref('');
const connected = ref(false);
const onlineCount = ref(0);
const messageList = ref(null);
let ws = null;
let heartbeatTimer = null;

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

function buildWsUrl() {
  const userId = '10001';
  const roomId = props.roomId || '1001';
  const token = getCookie('tltk') || 'test-token';
  const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
  const host = window.location.hostname;
  const port = '8085';
  return `${protocol}//${host}:${port}/ws/chat?userId=${userId}&roomId=${roomId}&token=${token}`;
}

function connect() {
  if (ws) ws.close();

  ws = new WebSocket(buildWsUrl());

  ws.onopen = () => {
    connected.value = true;
    startHeartbeat();
  };

  ws.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      if (data.type === 'error') {
        console.error('WebSocket error:', data.message);
        return;
      }
      messages.value.push(data);
      scrollToBottom();
    } catch (e) {
      console.error('解析消息失败', e);
    }
  };

  ws.onclose = () => {
    connected.value = false;
    stopHeartbeat();
    setTimeout(connect, 3000);
  };

  ws.onerror = () => {
    connected.value = false;
  };
}

function sendMessage() {
  if (!ws || ws.readyState !== WebSocket.OPEN || !inputMsg.value.trim()) return;

  const msg = {
    content: inputMsg.value.trim(),
    msgType: 1,
    sendTime: Date.now(),
  };

  ws.send(JSON.stringify(msg));
  inputMsg.value = '';
}

function scrollToBottom() {
  nextTick(() => {
    if (messageList.value) {
      messageList.value.scrollTop = messageList.value.scrollHeight;
    }
  });
}

function startHeartbeat() {
  heartbeatTimer = setInterval(() => {
    if (ws && ws.readyState === WebSocket.OPEN) {
      ws.send(JSON.stringify({ type: 'ping' }));
    }
  }, 30000);
}

function stopHeartbeat() {
  if (heartbeatTimer) {
    clearInterval(heartbeatTimer);
    heartbeatTimer = null;
  }
}

onMounted(() => {
  connect();
});

onUnmounted(() => {
  stopHeartbeat();
  if (ws) ws.close();
});
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 500px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: #fff;
  overflow: hidden;
}

.chat-header {
  padding: 12px 16px;
  background: #409eff;
  color: #fff;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
}

.online-count {
  font-size: 12px;
  opacity: 0.8;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
}

.message-item {
  margin-bottom: 8px;
}

.system-msg {
  text-align: center;
  color: #909399;
  font-size: 12px;
  padding: 4px 0;
}

.user-msg {
  display: flex;
  flex-direction: column;
  padding: 6px 10px;
  border-radius: 6px;
  background: #f5f7fa;
}

.sender-name {
  font-size: 12px;
  color: #409eff;
  font-weight: bold;
  margin-bottom: 2px;
}

.msg-content {
  font-size: 13px;
  color: #333;
  word-break: break-all;
}

.user-msg.barrage {
  background: #fffbe6;
  border-left: 3px solid #faad14;
}

.gift-msg {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  background: #fff1f0;
  border-left: 3px solid #ff4d4f;
}

.gift-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: bold;
  color: #ff4d4f;
}

.gift-image {
  width: 20px;
  height: 20px;
  border-radius: 4px;
}

.gift-count {
  font-size: 12px;
}

.gift-price {
  font-size: 12px;
  color: #ff4d4f;
  margin-left: auto;
}

.chat-input {
  display: flex;
  padding: 10px;
  border-top: 1px solid #e0e0e0;
  background: #f9f9f9;
}

.chat-input input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  outline: none;
  font-size: 13px;
}

.chat-input input:focus {
  border-color: #409eff;
}

.chat-input button {
  margin-left: 8px;
  padding: 8px 16px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.chat-input button:hover:not(:disabled) {
  background: #66b1ff;
}

.chat-input button:disabled {
  background: #c0c4cc;
  cursor: not-allowed;
}

.empty-tip {
  text-align: center;
  color: #c0c4cc;
  font-size: 13px;
  padding: 40px 0;
}
</style>
