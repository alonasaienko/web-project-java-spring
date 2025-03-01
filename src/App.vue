<!-- App.vue -->

<template>
  <div class="parent-container">
    <div class="panel">
      <h1 class="title">Jacobi Solver</h1>
      <div class="loginSignup">
        <UserAvatar/>
        <Button v-if="isAuthenticated" icon="pi pi-sign-out" label="Log Out" class="signup-button" @click="logout" />
        <Button v-else icon="pi pi-home" label="Log In" class="login-button" @click="goToLogin" />
        <router-link v-if="!isAuthenticated" to="/signup">
          <Button icon="pi pi-user-plus" label="Sign Up" class="signup-button" />
        </router-link>
    </div>
    </div>
    
    <Calculator />
    <router-view></router-view>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import Login from './components/Default/Login.vue'
import Signup from './components/Default/Signup.vue'
import Calculator from './components/Default/Calculator.vue'
import MainHomePage from './components/Home/MainHomePage.vue';

export default{
  components: {
    Login,
    Signup,
    Calculator,
    MainHomePage,
  },
  setup() {
    const isAuthenticated = computed(() => !!localStorage.getItem('token'));

    const logout = () => {
      localStorage.removeItem('token'); 
      window.location.reload(); 
    };

    return {
      isAuthenticated,
      logout
    };
  },
  methods: {
    goToLogin() {
      this.$router.push('/login');
    }
  }
};

</script>

<style scoped>
.panel {
  background-color: #cedae6;
  padding: 1rem;
  border-radius: 16px;
  width: 100%;
  max-width: 2000px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  color: white;
  margin: 0;
  font-family: 'Roboto', sans-serif;
  font-size: 24px;
}

.loginSignup {
  display: flex;
  gap: 10px;
}

.search-field {
  border-radius: 16px;
}
.dialog-button {
    font-family: 'Roboto', sans-serif;
    background-color: #EB5436;
    padding: 0.5rem 1rem;
    border: none;
    color: white;
    cursor: pointer;
    border-radius: 16px;
}
.login-button {
  font-family: 'Roboto', sans-serif;
  background-color: #EB5436;
  padding: 0.5rem 1rem;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 16px;
}
.signup-button {
    font-family: 'Roboto', sans-serif;
    padding: 0.5rem 1rem;
    border: none;
    color: white;
    cursor: pointer;
    border-radius: 16px;
}
</style>
