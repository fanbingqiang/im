<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <div class="logo-area">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="logo-icon">
            <polygon points="23 7 16 12 23 17 23 7"/>
            <rect x="1" y="5" width="15" height="14" rx="2"/>
          </svg>
          <h1>TI Live</h1>
        </div>
        <p class="subtitle">Live Platform</p>
      </div>
      <div class="login-form">
        <div class="form-group">
          <label>Phone</label>
          <input v-model="phone" type="tel" placeholder="Enter phone number" maxlength="11" />
        </div>
        <div class="form-group code-row">
          <input v-model="code" type="text" placeholder="Verification code" maxlength="6" />
          <button class="btn-code" :disabled="codeSending || codeCountdown > 0" @click="handleSendCode">
            {{ codeCountdown > 0 ? codeCountdown + "s" : (codeSending ? "Sending..." : "Send Code") }}
          </button>
        </div>
        <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
        <button class="btn-login" :disabled="loggingIn" @click="handleLogin">
          {{ loggingIn ? "Logging in..." : "Login" }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"
import { sendCode, mobileLogin } from "../api/index.js"

const router = useRouter()
const phone = ref("")
const code = ref("")
const errorMsg = ref("")
const codeSending = ref(false)
const codeCountdown = ref(0)
const loggingIn = ref(false)
let countdownTimer = null

async function handleSendCode() {
  if (!phone.value || phone.value.length < 11) { errorMsg.value = "Invalid phone number"; return }
  errorMsg.value = ""
  codeSending.value = true
  try {
    await sendCode(phone.value)
    codeCountdown.value = 60
    countdownTimer = setInterval(() => {
      codeCountdown.value--
      if (codeCountdown.value <= 0) { clearInterval(countdownTimer); codeCountdown.value = 0 }
    }, 1000)
  } catch(e) { errorMsg.value = "Failed to send code" }
  finally { codeSending.value = false }
}

async function handleLogin() {
  if (!phone.value || !code.value) { errorMsg.value = "Fill in phone and code"; return }
  errorMsg.value = ""
  loggingIn.value = true
  try {
    const r = await mobileLogin(phone.value, parseInt(code.value))
    if (r.data.code === 200) { router.push("/") }
    else { errorMsg.value = r.data.data || "Login failed" }
  } catch(e) { errorMsg.value = "Login failed, check backend services" }
  finally { loggingIn.value = false }
}
</script>

<style scoped>
.login-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #e8f0fe, #f0f4f8); }
.login-card { width: 380px; background: #fff; border-radius: 12px; box-shadow: 0 4px 24px rgba(59,130,246,0.08); overflow: hidden; }
.login-header { background: #1e3a5f; padding: 28px 36px 20px; text-align: center; }
.logo-area { display: flex; align-items: center; justify-content: center; gap: 10px; }
.logo-icon { width: 28px; height: 28px; color: #fff; }
.login-header h1 { font-size: 22px; font-weight: 700; color: #fff; margin: 0; }
.subtitle { color: rgba(255,255,255,0.7); font-size: 13px; margin-top: 4px; }
.login-form { padding: 24px 36px 32px; }
.form-group { margin-bottom: 18px; }
.form-group label { display: block; font-size: 13px; color: #555; margin-bottom: 6px; font-weight: 500; }
.form-group input { width: 100%; padding: 10px 12px; border: 1px solid #d0d5dd; border-radius: 6px; font-size: 14px; color: #333; outline: none; }
.form-group input:focus { border-color: #1e3a5f; }
.code-row { display: flex; gap: 10px; }
.code-row input { flex: 1; }
.btn-code { height: 40px; padding: 0 14px; background: #f0f4f8; border: 1px solid #d0d5dd; border-radius: 6px; color: #555; font-size: 13px; cursor: pointer; white-space: nowrap; flex-shrink: 0; }
.btn-code:hover:not(:disabled) { background: #e2e8f0; }
.btn-code:disabled { color: #aaa; cursor: not-allowed; }
.error-msg { color: #e74c3c; font-size: 13px; margin-bottom: 12px; }
.btn-login { width: 100%; padding: 12px; background: #1e3a5f; color: #fff; border: none; border-radius: 6px; font-size: 15px; font-weight: 500; cursor: pointer; }
.btn-login:hover:not(:disabled) { background: #2a4f7a; }
.btn-login:disabled { background: #8899aa; cursor: not-allowed; }
</style>