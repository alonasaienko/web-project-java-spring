//authService.js

import axios from 'axios';

class AuthService {
    async register(user) {
        try {
            return axios.post('/auth/signup', user, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
        }
        catch (error) {
            console.error('Error while registing', error);
            throw error;
        }
    }

    async login(user) {
        try {
            return axios.post('/auth/login', user,{
                headers: {
                    'Content-Type': 'application/json',
                },
            });
        }
        catch (error) {
            console.error('Error while registing', error);
            throw error;
        }
    }
}

export default new AuthService();
