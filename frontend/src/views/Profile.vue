<template>
  <div class="container">
    <div style="margin-top: 2%">
      <h3>
        <strong>{{content.username}}</strong>
        <strong style="margin-left: 46%; font-family: Arial; color: gray">Menu</strong>
      </h3>
    </div>
    <hr>
    <table style="margin-top: 3%">
      <td>
        <div style="margin-top: 5%">
          <p><strong>Email:</strong> {{content.email}}</p>
          <p><strong>Role:</strong> {{roleName}}</p>  
        </div>
      </td>
      <td>
        <div style="width: 50%; margin-left: 45%; margin-top: -10%">
          <p><h6 style="text-align: center; color: grey"><i><ins>Products:</ins></i></h6></p>
          <table class="buttons">
            <tr>
              <td><button class="btn btn-outline-secondary" v-on:click="forImport()" type="submit" style="margin-right: 5%;">IMPORT</button></td>
              <td><button class="btn btn-outline-secondary" v-on:click="getAllProducts()" type="submit" style="margin-right: 5%">IN STOCK</button></td>
            </tr>
            <tr v-if="roleName === 'MANAGER'">
              <td><button class="btn btn-outline-secondary" v-on:click="uploadFromCSV()" type="submit" style="margin-right: 5%; width: 231px">UPLOAD</button></td>
            </tr>
          </table>
        </div>
      </td>
      <td>
        <div style="width: 50%; margin-left: 10%; margin-top: -10%">
          <p><h6 style="margin-top: 5%; text-align: center; color: grey"><i><ins>Waybills:</ins></i></h6></p>
          <table class="buttons" style="margin-bottom: 30%">
            <tr>
              <td><button class="btn btn-outline-secondary" v-on:click="getProductsForExport()" type="submit" style="margin-right: 5%">EXPORT</button></td>
              <td><button class="btn btn-outline-secondary" v-on:click="getCompletedWaybills()" type="submit" style="margin-right: 5%; text-align: center">COMPLETED</button></td>
            </tr>
            <tr v-if="roleName === 'MANAGER'">
              <td><button class="btn btn-outline-secondary" v-on:click="getPendingWaybillsOfCurentUser()" type="submit" style="margin-right: 5%">PENDING</button></td>
              <td><button class="btn btn-outline-secondary" v-on:click="getRejectedWaybillsOfCurentUser()" type="submit" style="margin-right: 5%; text-align: center">REJECTED</button></td>
            </tr>
          </table>
        </div>
      </td>
    </table>
  </div>
</template>
  
<script>
import UserService from '../services/user.service';

export default {
  name: 'User',
  data() {
    return {
      content: '',
      roleName: '',
      file:''
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
      return;
    }
    
    UserService.getUserBoard()
      .then(
        response => {
          this.content = response.data;
          this.roleName = response.data.role.name;
        },
        error => {
          this.content =
            (error.response && error.response.data) ||
            error.message ||
            error.toString();
        }
    );
  },
  methods: {
    getAllProducts() {
      this.$router.push(`/products`);
    },
    forImport() {
      this.$router.push(`/for-import`);
    },
    getProductsForExport() {
      this.$router.push(`/products/pending-export`);
    },
    getCompletedWaybills() {
      this.$router.push(`/waybills/current/completed`);
    },
    getPendingWaybillsOfCurentUser() {
      this.$router.push(`/waybills/current/pending`);
    },
    getRejectedWaybillsOfCurentUser() {
      this.$router.push(`/waybills/current/rejected`);
    },
    uploadFromCSV() {
      this.$router.push(`/upload-from-csv`);
    }
  }
};
</script>

<style>

  .buttons { 
    width: 100%;
    table-layout: fixed;
    border-collapse: collapse;
  }

  .buttons button { 
    width: 100%;
  }

</style>