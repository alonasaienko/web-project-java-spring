<template>
  <div class="card">
      <div class="dialog-content">
          <form @submit.prevent="loginUser">
              <div class="flex items-center gap-4 mb-8">
                  <label for="username" class="font-semibold w-24">Username</label>
                  <InputText id="username" v-model="user.username" class="flex-auto" autocomplete="off" />
              </div>
              <div class="flex items-center gap-4 mb-8">
                  <label for="entered">Password</label>
                  <Password id="entered" v-model="user.password" toggleMask :feedback="false" />
              </div>
              <div class="flex justify-center">
                  <Button type="button" label="Cancel" severity="secondary"></Button>
                  <Toast position="top-right" />
                  <Button type="submit" label="Log In" class="dialog-button"></Button>
              </div>
          </form>
          <div class="mt-3">
                <span> Don't have account? <router-link to="/signup">Sign up here</router-link></span>
        </div>
      </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import 'primeicons/primeicons.css'
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import AuthService from '@/services/authService.js';
import { useRouter } from 'vue-router';

export default {
  components: {
      InputText,
      Password,
      Button,
      Toast,
  },
  setup() {
      const toast = useToast();
      const router = useRouter();
      const user = ref({
          username: '',
          password: ''
      });

      const loginUser = async () => {
          try {
              const response = await AuthService.login(user.value);
              const token = response.data.token;
              localStorage.setItem('token', token);
              router.push('/home');
              console.log(response.data);
              toast.add({
                  severity: 'success',
                  summary: 'Login Success',
                  detail: 'Logged in successfully',
                  life: 3000,
              });
          } catch (error) {
              console.error(error);
              toast.add({
                  severity: 'error',
                  summary: 'Login Failed',
                  detail: 'Invalid username or password',
                  life: 3000,
              });
          }
      };

      return {
          user,
          loginUser
      };
  }
};
</script>

<style scoped>
.dialog-content {
  gap: 20px;
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: #F3F5F8;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  color: #333;
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  line-height: 1.5;
  font-weight: bold;
}

.flex {
  display: flex;
  flex-direction: column;
}

.justify-center {
  justify-content: center;
  align-items: center;
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

</style>
