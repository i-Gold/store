import Vue from 'vue';
import { AutoCompletePlugin } from '@syncfusion/ej2-vue-dropdowns';
import App from './App.vue';
import { router } from './router';
import store from './store';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import VueGoodTable from 'vue-good-table';
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue';
import VeeValidate from 'vee-validate';
import Vuex from 'vuex';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faHome,
  faUser,
  faUserPlus,
  faSignInAlt,
  faSignOutAlt
} from '@fortawesome/free-solid-svg-icons';

library.add(faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt);

Vue.config.productionTip = false;

Vue.use(VeeValidate, { fieldsBagName: 'formFields' });
Vue.component('font-awesome-icon', FontAwesomeIcon);

Vue.use(Vuex);

Vue.use(AutoCompletePlugin);

Vue.use(BootstrapVue);
Vue.use(BootstrapVueIcons);

Vue.use(VueGoodTable);

Vue.component('dropdown', {
  template: `
		<div>
			<button @click='toggleShow' class='anchor'>Super Menu</button>
			<div v-if='showMenu' class='menu'>
				<div class='my-menu-item' v-for='item in this.items' @click='itemClicked(item)'>{{item}}</div>
			</div>
		</div>
  `,

  data: function() {
    return {
      showMenu: false
    };
  },
  props: {
    onClick: Function,
    items: {
      type: Array,
      default: []
    }
  },
  methods: {
    toggleShow: function() {
      this.showMenu = !this.showMenu;
    },
    itemClicked: function(request) {
      this.toggleShow();
      this.onClick(request);
    }
  }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
