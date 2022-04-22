<template>
  <div class="container">
    <h3>PENDING WAYBILLS</h3>
    <div v-if="message" class="alert alert-success"></div>
    <div class="container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Type</th>
            <th>Status</th>
            <th>OWNER ID</th>
            <th>APPROVER ID</th>
            <th>DATE OF CREATION</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="waybill in waybills" v-bind:key="waybill.id">
            <td>{{ waybill.id }}</td>
            <td>{{ waybill.type }}</td>
            <td>{{ waybill.status }}</td>
            <td>{{ waybill.ownerId }}</td>
            <td>{{ waybill.approverId }}</td>
            <td>{{ waybill.createdAt }}</td>
            <td>
              <button
                class="btn btn-info"
                v-on:click="getWaybillById(waybill.id)"
              >
                Details
              </button>
            </td>
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
          nextLabel: 'Next',
          prevLabel: 'Previous'
        }"
      />
    </div>
  </div>
</template>

<script>
import WaybillService from '../services/waybill.service';

export default {
  name: 'WaybillCurrentPending',
  data() {
    return {
      message: null,
      waybills: [],
      rows: [{}],
      columns: [],
      page: 0,
      size: 20,
      totalElements: 0
    };
  },
  computed: {
    id() {
      return this.$route.params.id;
    }
  },
  methods: {
    refreshWaybills() {
      this.page = 0;
      this.size = 10;
      WaybillService.getPendingWaybillsOfCurrentUser(this.page, this.size).then(response => {
        this.waybills = response.data.content;
        this.page = response.data.page;
        this.totalElements = response.data.totalElements;
      });
    },
    getWaybillById(id) {
      this.$router.push(`/waybills/${id}`);
    },
    onPageChange(page) {
      this.page = page;
      this.loadItems();
    },
    onPerPageChange(page) {
      this.page = page;
      this.size = this.page.currentPerPage;
      WaybillService.getCompletedWaybills(this.page.currentPage - 1, this.size).then(
        response => {
          this.waybills = response.data.content;
          this.page = response.data.page;
          this.totalElements = response.data.totalElements;
        }
      );
    },
    loadItems() {
      WaybillService.getPendingWaybillsOfCurrentUser(this.page.currentPage - 1, this.size).then(
        response => {
          this.waybills = response.data.content;
          this.page = response.data.page;
          this.totalElements = response.data.totalElements;
        }
      );
    }
  },
  created() {
    this.refreshWaybills();
  }
};
</script>

<style></style>
