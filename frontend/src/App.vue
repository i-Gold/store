<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <a href class="navbar-brand" @click.prevent>STOREHOUSE</a>
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <router-link to="/home" class="nav-link">
            <font-awesome-icon icon="home" />Home
          </router-link>
        </li>
      </div>
      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/login" class="nav-link">
            <font-awesome-icon icon="sign-in-alt" />Login
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/register" class="nav-link">
            <font-awesome-icon icon="user-plus" />Sign Up
          </router-link>
        </li>
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li>
          <dropdown
            v-if="roleName === 'SUPER_MANAGER'"
            class="nav-item"
            :on-click="executeRequest"
            :items="instruments"
          ></dropdown>
        </li>
        <li class="nav-item">
          <router-link to="/profile" class="nav-link">
            <font-awesome-icon icon="user" />
            {{ currentUser.username }}
          </router-link>
        </li>
        <li class="nav-item">
          <a class="nav-link" href @click.prevent="logOut">
            <font-awesome-icon icon="sign-out-alt" />LogOut
          </a>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view />
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import UserService from './services/user.service';

export default {
  data() {
    return {
      roleName: '',
      instruments: [
        'ALL WAYBILLS',
        'IMPORT PENDING',
        'EXPORT PENDING',
        'AUTO-EXPORT'
      ]
    };
  },

  computed: {
    currentUser() {
      const current = this.$store.state.auth.user;
      if (current) {
        UserService.getUserRole().then(response => {
          this.roleName = response.data;
        });
      }
      return current;
    }
  },

  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    },
    executeRequest: function(request) {
      if (request === 'ALL WAYBILLS') {
        const path = '/waybills';
        if (this.$route.path !== path) {
          this.$router.push(path);
        }
      }
      if (request === 'IMPORT PENDING') {
        const path = '/waybills/pending-import';
        if (this.$route.path !== path) {
          this.$router.push(path);
        }
      }
      if (request === 'EXPORT PENDING') {
        const path = '/waybills/pending-export';
        if (this.$route.path !== path) {
          this.$router.push(path);
        }
      }
      if (request === 'AUTO-EXPORT') {
        const path = '/waybills/pending-auto-export';
        if (this.$route.path !== path) {
          this.$router.push(path);
        }
      }
    }
  }
};
</script>

<style>
.anchor {
  margin-top: -3px;
  margin-right: 50px;
  height: 40px;
  width: 170px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 0px solid transparent;
  padding: 0.75rem 2rem;
  font-size: 1rem;
  border-radius: 0.25rem;
  transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out,
    border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
  color: rgba(255, 255, 255, 0.5);
  background-color: #343a40;
  border-color: #343a40;
}

.anchor::after {
  display: inline-block;
  width: 0;
  height: 0;
  margin-left: 0.5em;
  vertical-align: 0.255em;
  content: '';
  border-top: 0.3em solid;
  border-right: 0.28em solid transparent;
  border-bottom: 0;
  border-left: 0.28em solid transparent;
}

.anchor:hover {
  color: #fff;
  background-color: #343a40;
  border-color: #343a40;
  cursor: pointer;
}

.menu {
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 0.25rem;
  color: #212529;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  font-size: 1rem;
  list-style: none;
  margin: 0.125rem 0 0;
  padding: 0.5rem 0;
  position: absolute;
  text-align: left;
}

.my-menu-item {
  color: #212529;
  padding: 0.25rem 1.5rem;
  transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out,
    border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}

.my-menu-item:hover {
  background-color: #f4f6f6;
  cursor: pointer;
}

.menu-span {
  font-weight: bold;
  color: #229954;
  font-size: 1.25rem;
}
</style>
