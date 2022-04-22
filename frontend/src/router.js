import Vue from 'vue';
import Router from 'vue-router';
import Profile from './views/Profile.vue';
import Login from './views/Login.vue';
import Register from './views/Register.vue';
import WaybillList from './views/WaybillList.vue';
import ProductImport from './views/ProductImport.vue';

Vue.use(Router);

export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/api/v1/profile',
      name: 'user',
      // lazy-loaded
      component: () => import('./views/Profile.vue')
    },
    {
      path: '/products/pending-export',
      name: 'Products for export',
      component: () => import('./views/ProductListPendingExport')
    },
    {
      path: '/products/:id',
      name: 'Product Details',
      component: () => import('./views/Product')
    },
    {
      path: '/products',
      name: 'Product List',
      component: () => import('./views/ProductList')
    },
    {
      path: '/api/v1/waybills/completed',
      name: 'Waybill List',
      component: () => import('./views/WaybillList')
    },
    {
      path: '/waybills/pending-import',
      name: 'Waybill Pending Import',
      component: () => import('./views/WaybillListPendingImport')
    },
    {
      path: '/waybills/pending-export',
      name: 'Waybill Pending Export',
      component: () => import('./views/WaybillListPendingExport')
    },
    {
      path: '/waybills/pending-auto-export',
      name: 'Waybill Pending Auto Export',
      component: () => import('./views/WaybillListPendingAutoExport')
    },
    {
      path: '/waybills/current/completed',
      name: 'Waybill Current Complited',
      component: () => import('./views/WaybillListCompleted')
    },
    {
      path: '/waybills/current/pending',
      name: 'Waybill Current Pending',
      component: () => import('./views/WaybillListCurrentPending')
    },
    {
      path: '/waybills/current/rejected',
      name: 'Waybill Current Rejected',
      component: () => import('./views/WaybillListCurrentRejected')
    },
    {
      path: '/waybills/:id',
      name: 'Waybill Details',
      component: () => import('./views/Waybill')
    },
    {
      path: '/upload-from-csv',
      name: 'Upload from CSV',
      component: () => import('./views/UploadFromCSV')
    },

    {
      path: '/',
      name: 'home',
      component: Profile
    },
    {
      path: '/home',
      component: Profile
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('./views/Profile.vue')
    },
    {
      path: '/waybills',
      component: WaybillList
    },
    {
      path: '/for-import',
      component: ProductImport
    }
  ]
});

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register', '/home'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('user');

  // trying to access a restricted page + not logged in
  // redirect to login page
  if (authRequired && !loggedIn) {
    next('/login');
  } else {
    next();
  }
});
