//historyService.js

import axios from 'axios';

export default {
    createHistory(history) {
        return axios.post('/api/jacobi/histories', history);
    },
    getAllHistory() {
        return axios.get('/api/histories/all');
    },
    deleteHistory(id) {
        return axios.delete(`/api/histories/${id}`);
    },
    getHistory(id) {
        return axios.get(`/api/histories/user/${id}`);
    },
    getUserHistories() {
        return axios.get('/api/histories/user')
    }
};
