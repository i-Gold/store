import { mount } from '@vue/test-utils';
import Login from '@/views/Login.vue';
import Vue from 'vue';
import VeeValidate from 'vee-validate';
import Vuex from 'vuex';
import VueRouter from 'vue-router';

Vue.use(Vuex);
Vue.use(VeeValidate);
Vue.use(VueRouter);

describe('Login.vue', () => {
  let store;
  let auth;
  let user;
  let initialState;
  let router;

  beforeEach(() => {
    router = new VueRouter();
    user = 'token';

    initialState = user
      ? { status: { loggedIn: true }, user }
      : { status: { loggedIn: false }, user: null };

    auth = {
      state: initialState
    };

    store = new Vuex.Store({
      modules: {
        auth
      }
    });
  });

  it('displays correct markup', () => {
    const wrapper = mount(Login, { status, store, router });
    expect(wrapper.html()).toContain('<form name="form">');
  });

  it('button is present', () => {
    const wrapper = mount(Login, { status, store, router });
    expect(wrapper.contains('button')).toBe(true);
  });
});
