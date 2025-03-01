<template>
    <div class="card">
        <h2 class="text">Sign Up</h2>
        <form @submit.prevent="addUser">
            <div class="flex items-center gap-4 mb-8">
                <label for="full_name">Full name</label>
                <InputText id="full_name" v-model="user.full_name" class="flex-auto" autocomplete="off" />
            </div>
            <div class="flex items-center gap-4 mb-8">
                <label for="username">Username</label>
                <InputText id="username" v-model="user.username" class="flex-auto" autocomplete="off" />
            </div>
            <div class="flex items-center gap-4 mb-8">
                <label for="email">Email</label>
                <InputText id="email" v-model="user.email" placeholder="example@email.com" />
            </div>
            <div class="flex items-center gap-4 mb-8">
                <label for="password">Password</label>
                <Password id="password" v-model="user.password" toggleMask :feedback="false" />
            </div>
            <div class="flex justify-center">
                <Button type="button" label="Cancel" severity="secondary" @click="visible = false"></Button>
                <Toast position="top-right" />
                <Button type="submit" label="Sign Up" class="dialog-button"></Button>
            </div>
        </form>
        <div class="mt-3">
            <span>Already registered? <router-link to="/login">Login here</router-link></span>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import AuthService from '@/services/authService.js';
import { useRouter } from 'vue-router';

const toast = useToast();
const router = useRouter();
const user = ref({
    full_name: '',
    username: '',
    email: '',
    password: '',
});

async function addUser(){
    try {
        const response = await AuthService.register(user.value);
        const token = response.data.token;
        localStorage.setItem('token', token);
        router.push('/home');
        toast.add({
            severity: 'success',
            summary: 'Success',
            detail: 'You have been successfully registered',
            life: 3000,
        });
    } catch (error) {
        console.error('Stack trace:', error.stack);
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: `Registration failed. Please try again. ${error.message}`,
            life: 3000,
        });
    }
};
</script>

<style scoped>
.card {
    padding: 1rem;
    border-radius: 16px;
    background-color: #f3f5f8;
}

.text {
    margin: 1rem 0;
    font-family: 'Roboto', sans-serif;
}

.flex {
    display: flex;
    flex-direction: column;
}

.justify-center {
    justify-content: center;
    align-items: center;
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
</style>
