//userstore.js

import { defineStore } from 'pinia'
import axios from 'axios'

function jwtToAuthUser(tdata) {
    if (tdata) {
        return {
            id: decodedToken.id || null,
            alias: tdata.sub,
            roles: tdata.auth.split(',')
        }
    } else {
        return null;
    }
}

export const AUTH_TOKEN = 'X-Abon-Auth'
export const ADMIN = 'ADMIN'
export const DEVELOPER = 'DEVELOPER'
export const USER = 'USER'
export const ALL_ROLES = [ADMIN, DEVELOPER, USER]
export const ROLES_NAMES = {
    ADMIN: 'Адміністратор',
    DEVELOPER: 'Розробник',
    USER: 'Користувач'
}

export function decodeJwt (token) {
    if (token) {
        let base64Url = token.split('.')[1];
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        let jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } else {
        return null;
    }
}

export const useUserStore = defineStore('userStore', {
    state: () => ({
        token: localStorage.getItem(AUTH_TOKEN),
        user: jwtToAuthUser(decodeJwt(localStorage.getItem(AUTH_TOKEN)))
    }),
    getters: {
        authToken: (state) => { return state.token},
        authUser: (state) => { return state.user},
        userAlias: (state) => { return state.user ? state.user.alias : null },
        isLogedIn: (state) => { return state.user ? true : false},
        isAdmin: (state) => { return state.user.roles.includes(ADMIN) }
    },
    actions: {
        login(token) {
            localStorage.setItem(AUTH_TOKEN, token)
            this.token = token
            this.user = jwtToAuthUser(decodeJwt(token))
        },
        logout() {
            localStorage.removeItem(AUTH_TOKEN)
            this.token = null
            this.user = null
        },
        isRolesAllowed(roles) {
            let allowed = false
            if (this.user && this.user.roles) {
                for(let i=0; i<roles.length;i++){
                    allowed = allowed || this.user.roles.includes(roles[i])
                    if (allowed) break;
                }
            }
            return allowed;
        },
        async checkToken() {
            await axios.get('/checktoken').then(resp => {this.login(resp.data)})
        }
    }
})