<template>
  <div class="login-wrap">
    <div class="bg-shapes">
      <span class="shape s1"></span>
      <span class="shape s2"></span>
      <span class="shape s3"></span>
    </div>

    <div class="login-card">
      <div class="brand">
        <div class="brand-icon">✦</div>
        <h1>Welcome</h1>
        <p>로그인하여 계속하세요</p>
      </div>

      <form @submit.prevent="handleLogin" class="form">
        <div class="field" :class="{ focused: focusedField === 'email', error: errors.email }">
          <label>이메일</label>
          <input
            type="email"
            v-model="form.email"
            placeholder="example@email.com"
            @focus="focusedField = 'email'"
            @blur="focusedField = ''"
          />
          <span class="error-msg" v-if="errors.email">{{ errors.email }}</span>
        </div>

        <div class="field" :class="{ focused: focusedField === 'password', error: errors.password }">
          <label>비밀번호</label>
          <div class="input-wrap">
            <input
              :type="showPassword ? 'text' : 'password'"
              v-model="form.password"
              placeholder="비밀번호를 입력하세요"
              @focus="focusedField = 'password'"
              @blur="focusedField = ''"
            />
            <button type="button" class="toggle-pw" @click="showPassword = !showPassword">
              {{ showPassword ? '숨기기' : '보기' }}
            </button>
          </div>
          <span class="error-msg" v-if="errors.password">{{ errors.password }}</span>
        </div>

        <div class="options">
          <!-- <label class="remember">
            <input type="checkbox" v-model="form.remember" />
            <span>로그인 유지</span>
          </label> -->
          <a href="#" class="forgot">비밀번호 찾기</a>
        </div>

        <button type="submit" class="btn-login" :class="{ loading: isLoading }">
          <span v-if="!isLoading">로그인</span>
          <span v-else class="spinner"></span>
        </button>

        <p class="signup-link">
          계정이 없으신가요? <router-link to="/signup">회원가입</router-link>
        </p>
      </form>

      <div class="api-error" v-if="apiError">{{ apiError }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  email: '',
  password: '',
  remember: false
})

const errors = reactive({ email: '', password: '' })
const focusedField = ref('')
const showPassword = ref(false)
const isLoading = ref(false)
const apiError = ref('')

function validate() {
  errors.email = ''
  errors.password = ''
  let valid = true

  if (!form.email) {
    errors.email = '이메일을 입력해주세요.'
    valid = false
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = '올바른 이메일 형식이 아닙니다.'
    valid = false
  }

  if (!form.password) {
    errors.password = '비밀번호를 입력해주세요.'
    valid = false
  } else if (form.password.length < 6) {
    errors.password = '비밀번호는 6자 이상이어야 합니다.'
    valid = false
  }

  return valid
}

async function handleLogin() {
  if (!validate()) return

  isLoading.value = true
  apiError.value = ''

  try {
    const res = await axios.post('/api/auth/login', {
      email: form.email,
      password: form.password
    })

    // JWT 토큰 저장
    const token = res.data.token
    if (form.remember) {
      localStorage.setItem('token', token)
    } else {
      sessionStorage.setItem('token', token)
    }

    // axios 기본 헤더에 토큰 설정
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

    // 로그인 후 메인 페이지로 이동
    router.push('/')
  } catch (err) {
    if (err.response?.status === 401) {
      apiError.value = '이메일 또는 비밀번호가 올바르지 않습니다.'
    } else {
      apiError.value = '로그인 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap');

* { box-sizing: border-box; margin: 0; padding: 0; }

.login-wrap {
  min-height: 100vh;
  background: #0a0a0f;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'DM Sans', sans-serif;
  position: relative;
  overflow: hidden;
}

/* 배경 도형 */
.bg-shapes { position: absolute; inset: 0; pointer-events: none; }
.shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.15;
}
.s1 { width: 500px; height: 500px; background: #6c63ff; top: -100px; left: -100px; animation: drift 8s ease-in-out infinite; }
.s2 { width: 400px; height: 400px; background: #00d4ff; bottom: -80px; right: -80px; animation: drift 10s ease-in-out infinite reverse; }
.s3 { width: 300px; height: 300px; background: #ff6b9d; top: 50%; left: 50%; transform: translate(-50%, -50%); animation: drift 12s ease-in-out infinite; }

@keyframes drift {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(30px, -30px); }
}

/* 카드 */
.login-card {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.08);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 48px 44px;
  width: 100%;
  max-width: 420px;
  position: relative;
  z-index: 1;
  animation: fadeUp 0.6s ease both;
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(24px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* 브랜드 */
.brand { text-align: center; margin-bottom: 36px; }
.brand-icon {
  font-size: 28px;
  color: #6c63ff;
  margin-bottom: 16px;
  display: block;
  animation: spin 6s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.brand h1 {
  font-family: 'Syne', sans-serif;
  font-size: 28px;
  font-weight: 800;
  color: #fff;
  letter-spacing: -0.5px;
  margin-bottom: 6px;
}
.brand p { color: rgba(255,255,255,0.4); font-size: 14px; }

/* 폼 */
.form { display: flex; flex-direction: column; gap: 18px; }

.field { display: flex; flex-direction: column; gap: 6px; }
.field label {
  font-size: 12px;
  font-weight: 500;
  color: rgba(255,255,255,0.5);
  text-transform: uppercase;
  letter-spacing: 0.8px;
  transition: color 0.2s;
}
.field.focused label { color: #6c63ff; }
.field.error label { color: #ff6b6b; }

.field input {
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 12px;
  padding: 14px 16px;
  color: #fff;
  font-size: 15px;
  font-family: 'DM Sans', sans-serif;
  outline: none;
  transition: border-color 0.2s, background 0.2s;
  width: 100%;
}
.field input::placeholder { color: rgba(255,255,255,0.2); }
.field.focused input { border-color: #6c63ff; background: rgba(108,99,255,0.08); }
.field.error input { border-color: #ff6b6b; }

.input-wrap { position: relative; }
.input-wrap input { padding-right: 70px; }
.toggle-pw {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: rgba(255,255,255,0.3);
  font-size: 12px;
  cursor: pointer;
  font-family: 'DM Sans', sans-serif;
  transition: color 0.2s;
}
.toggle-pw:hover { color: #6c63ff; }

.error-msg { font-size: 12px; color: #ff6b6b; }

/* 옵션 */
.options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.remember {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: rgba(255,255,255,0.4);
  font-size: 13px;
}
.remember input[type="checkbox"] { accent-color: #6c63ff; }
.forgot { font-size: 13px; color: rgba(255,255,255,0.4); text-decoration: none; transition: color 0.2s; }
.forgot:hover { color: #6c63ff; }

/* 로그인 버튼 */
.btn-login {
  margin-top: 4px;
  padding: 15px;
  background: #6c63ff;
  border: none;
  border-radius: 12px;
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  font-family: 'Syne', sans-serif;
  cursor: pointer;
  transition: background 0.2s, transform 0.1s;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 50px;
}
.btn-login:hover { background: #5a52e0; }
.btn-login:active { transform: scale(0.98); }
.btn-login.loading { pointer-events: none; opacity: 0.7; }

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

/* 회원가입 링크 */
.signup-link { text-align: center; font-size: 13px; color: rgba(255,255,255,0.3); }
.signup-link a { color: #6c63ff; text-decoration: none; font-weight: 500; }
.signup-link a:hover { text-decoration: underline; }

/* API 에러 */
.api-error {
  margin-top: 12px;
  padding: 12px 16px;
  background: rgba(255,107,107,0.1);
  border: 1px solid rgba(255,107,107,0.3);
  border-radius: 10px;
  color: #ff6b6b;
  font-size: 13px;
  text-align: center;
}
</style>