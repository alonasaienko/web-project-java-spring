//userService.js


import axios from "axios";

export const UserService = {
    createUser(user) {
        return axios.post('/api/users', user);
    },
    getAllUsers() {
        return axios.get('/api/users');
    },
    deleteUser(id) {
        return axios.delete(`/api/users/${id}`);
    },
    updateUser(user) {
        return axios.put(`/api/users/${user.id}`, user);
    },
    getUser(id) {
        return axios.get(`/api/users/${id}`);
    },
    getUserByUsername(username) {
        return axios.get(`/api/users/username/${username}`);
    },
    getUserByEmail(email) {
        return axios.get(`/api/users/email/${email}`);
    },
};