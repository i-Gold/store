<template>
  <div>
    <div class="container">
        <h3 v-if="!isApproved && roleName === 'SUPER_MANAGER'" style="margin-top: 1%; color: blue">
          <strong
            ><ins>Please, approve or reject this waybill</ins
            ></strong
          >
        </h3>
        <select
          style="margin-top: 1%; margin-left: 79%; margin-bottom: 1%"
          v-if="!isApproved && roleName === 'SUPER_MANAGER'"
          v-validate="'required|min:8|max:9'"
          v-model="confirmationStatus"
          name="confirmationStatus"
        >
          <option value="" disabled selected>Select Status</option>
          <option value="COMPLETED">APPROVE</option>
          <option value="REJECTED">REJECT</option>
        </select>
      <div style="margin-left: 80%">
        <button
          class="btn btn-warning"
          v-if="!isApproved && roleName === 'SUPER_MANAGER'"
          @click="confirmWaybill"
        >
          CONFIRM
        </button>
      </div>
      <div style="margin-left: 80%; margin-top: 1%">
        <button
          class="btn btn-secondary"
          v-if="isApproved && roleName === 'SUPER_MANAGER'"
          @click="exportWaybill"
        >
          DOWNLOAD
        </button>
        <a ref="downloadLink" id="download-link"></a>
      </div>
      <div style="margin-top: 3%">
        <h3>
          <p>
            <strong>Waybill ID: {{ this.id }}</strong>
          </p>
        </h3>
      </div>
      <p>
        <strong>Status:</strong>
        {{ this.status }}
      </p>
      <p>
        <strong>Type:</strong>
        {{ this.type }}
      </p>
      <p>
        <strong>Owner ID:</strong>
        {{ this.ownerId }}
      </p>
      <p>
        <strong>Approver ID:</strong>
        {{ this.approverId }}
      </p>
      <p>
        <strong>Date of creation:</strong>
        {{ this.createdAt }}
      </p>
      <strong>Products:</strong>
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
import WaybillService from '../services/waybill.service';
import UserService from '../services/user.service';

export default {
  name: 'Waybill',

  data() {
    return {
      status: '',
      type: '',
      ownerId: null,
      approverId: null,
      createdAt: null,
      isApproved: true,
      confirmationStatus: '',
      products: [],
      roleName: ''
    };
  },
  computed: {
    id() {
      return this.$route.params.id;
    }
  },
  methods: {
    refreshWaybillDetails() {
      WaybillService.getWaybillById(this.id).then(response => {
        this.status = response.data.status;
        this.type = response.data.type;
        this.ownerId = response.data.ownerId;
        this.approverId = response.data.approverId;
        this.createdAt = response.data.createdAt;
        this.isApproved = response.data.isApproved;
        this.products = response.data.products;
      });
    },
    exportWaybill() {
      WaybillService.exportToCSV(this.id).then(response => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        this.$refs.downloadLink.href = url;
        var filename = '';
        var disposition = response.headers['content-disposition'];
        if (disposition && disposition.indexOf('attachment') !== -1) {
          var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
          var matches = filenameRegex.exec(disposition);
          if (matches != null && matches[1]) {
            filename = matches[1].replace(/['"]/g, '');
          }
        }
        this.$refs.downloadLink.setAttribute('download', filename);
        this.$refs.downloadLink.click();
      });
    },
    confirmWaybill() {
      WaybillService.approveOrRejectWaybill(
        this.id,
        this.confirmationStatus
      ).then(() => {
        this.$router.push('/waybills/pending-import');
      });
    },
  },
  created() {
    this.refreshWaybillDetails();
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
  }
};
</script>

<style>
</style>
