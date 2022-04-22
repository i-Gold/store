<template>
  <div class="container">
    <h3 style="margin-top: 3%">Please, Upload Your .CSV File</h3>
    <div style="margin-top: 5%" v-if="roleName === 'MANAGER'">
      <table>
        <tr>
          <td>
            <input
              type="file"
              id="file"
              ref="file"
              v-on:change="handleFileUpload()"
            />
          </td>
        </tr>
        <tr>
          <td>
            <button
              class="btn btn-warning"
              v-on:click="upload()"
              type="submit"
              style="margin-top: 2%; margin-right: 5%; width: 231px"
            >
              UPLOAD
            </button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import UserService from '../services/user.service';
import WaybillsService from '../services/waybill.service';

export default {
  name: 'Upload',
  data() {
    return {
      file: '',
      roleName: ''
    };
  },
  methods: {
    upload() {
      let csvFile = new FormData();
      csvFile.append('csvFile', this.file);
      WaybillsService.uploadFromCSV(csvFile);
    },
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },
    getUserRole() {
      const current = this.$store.state.auth.user;
      if (current) {
        UserService.getUserRole().then(response => {
          this.roleName = response.data;
        });
      }
      return current;
    }
  },
  created() {
    this.getUserRole();
  }
};
</script>
