<template>
  <div class="container">
    <div v-if="isHidden">
      <h3 style="margin-top: 1%; color: blue">
        <strong><ins>Please, create the waybill for export</ins></strong>
      </h3>
    </div>
    <div style="margin-left: 80%">
      <button v-if="isHidden" class="btn btn-warning" @click="createExportWaybill">
        CONFIRM
      </button>
    </div>
    <h3>Create Waybill for Export</h3>
    <div v-if="message" class="alert alert-success"></div>
    <div class="container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Manufacturer</th>
            <th>Weight</th>
            <th>Produced Date</th>
            <th>Expired Date</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" v-bind:key="product.id">
            <td>{{ product.id }}</td>
            <td>{{ product.name }}</td>
            <td>{{ product.manufacturer }}</td>
            <td>{{ product.weight }}</td>
            <td>{{ product.producedAt }}</td>
            <td>{{ product.expiredAt }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import ProductService from '../services/product.service';
import WaybillService from '../services/waybill.service';

export default {
  name: 'ProductsPendingExport',

  data() {
    return {
      message: null,
      products: [],
      number: null,
      totalElements: null,
      totalPages: null,
      isHidden: false
    };
  },
  methods: {
    refreshProducts() {
      this.isHidden = false;
      ProductService.getPendingExportProductsForCurrentUser().then(response => {
        if(response.data.content !== null) {
          this.products = response.data.content;
          this.number = response.data.number;
          this.totalElements = response.data.totalElements;
          this.totalPages = response.data.totalPages;
          this.isHidden = true;
        }
      });
    },
    createExportWaybill() {
      WaybillService.createWaybillForExport();
      this.$router.push('/profile');
    }
  },
  created() {
    this.refreshProducts();
  }
};
</script>

<style></style>
