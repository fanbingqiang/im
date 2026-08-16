import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true
})

api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export function sendCode(phone) {
  return api.post('/user/sendSMSCode', phone, { headers: { 'Content-Type': 'text/plain' } })
}

export function mobileLogin(mobile, code) {
  return api.post('/user/mobileLogin', { mobile, code })
}

export function getUserById() {
  return api.get('/user/getUserById')
}

export function getOnlineRooms() {
  return api.get('/im/room/list')
}

export function getRoomInfo(roomId) {
  return api.get('/im/room/info', { params: { roomId } })
}

export function getOnlineCount(roomId) {
  return api.get('/im/room/onlineCount', { params: { roomId } })
}

export function createRoom(roomName) {
  return api.post('/im/room/create', null, { params: { roomName } })
}

export function getMessageHistory(roomId, limit = 50) {
  return api.get('/im/message/history', { params: { roomId, limit } })
}
