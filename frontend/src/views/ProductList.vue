<template>
  <div class="container">
    <h3>ALL PRODUCTS
      <button style="margin-left: 65%" class="btn btn-warning" v-on:click="showExportButton()">Show/Hide Export button</button></td>
        <b-input-group size="sm" style="margin-left: 15px" class="mb-2">
          <b-input-group-prepend is-text>
            <b-icon  style="margin-right: 5px" icon="search"></b-icon>
            <ejs-autocomplete style="margin-left: 10px; width: 300px" v-model="search" :dataSource='products' :fields='productFields' placeholder="Search by Name" :highlight="true"></ejs-autocomplete>
          </b-input-group-prepend>
          <b-input-group-append>
            <b-button @click="searchProducts()" variant="primary">Search</b-button>
          </b-input-group-append>
        </b-input-group>
    </h3>
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
            <td>{{product.id}}</td>
            <td>{{product.name}}</td>
            <td>{{product.manufacturer}}</td>
            <td>{{product.weight}}</td>
            <td>{{product.producedAt}}</td>
            <td>{{product.expiredAt}}</td>
            <td><button class="btn btn-info" v-on:click="getProductById(product.id)">Details</button></td>
            <td><button class="btn btn-danger" v-if="!isHidden" v-on:click="exportProduct(product.id)">Export</button></td>
          </tr>
        </tbody>
      </table>
      <vue-good-table
          mode="remote"
          @on-page-change="onPageChange"
          @on-per-page-change="onPerPageChange"
          :totalRows="totalElements"
          :rows="rows"
          :columns="columns"
          :pagination-options="{
            enabled: true,
            rowsPerPageLabel: 'Rows per page',
            nextLabel: 'Next',
            prevLabel: 'Previous',
          }"
        />
    </div>
  </div>
</template>

<script>
import ProductService from "../services/product.service";

export default {
  name: "ProductList",
  data() {
    return {
      message: null,
      products: [],
      rows: [{}],
      columns: [],
      page: 0,
      size: 20,
      totalElements: 0,
      productFields: {value: 'name'},
      isHidden: true,
      search: '',
      columns: []
    };
  },

  methods: {
    refreshProducts() {
      this.page = 0;
      this.size = 10;
      ProductService.getAllProducts(this.page, this.size)
        .then(response => {
          this.products = response.data.content;
          this.page = response.data.page;
          this.totalElements = response.data.totalElements;
        });
    },
    getProductById(id) {
      this.$router.push(`/products/${id}`);
    },
    showExportButton() {
      this.isHidden = !this.isHidden;
    },
    exportProduct(id) {
      ProductService.addProductsForExport(
            {
              productsIds: [
                id
              ]
            }
        );
    },
    searchProducts() {
      if(this.search) {
        ProductService.searchProducts(this.search)
        .then(response => {
          this.products = response.data.content;
          this.page = response.data.page;
          this.totalElements = response.data.totalElements;
        });
      } else {
        window.location.reload();
      }
    },
    onPageChange(page) {
      this.page = page;
      this.loadItems();
    },
    onPerPageChange(page) {
      this.page = page;
      this.size = this.page.currentPerPage;
      loadItems();
    },
    loadItems() {
      ProductService.getAllProducts(this.page.currentPage - 1, this.size)
        .then(response => {
          this.products = response.data.content;
          this.page = response.data.page;
          this.totalElements = response.data.totalElements;
        });
    }
  },
  created() {
    this.refreshProducts();
  }
};
</script>

<style>
  @import url(https://cdn.syncfusion.com/ej2/material.css);
</style>