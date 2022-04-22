import axios from 'axios';
import authHeader from './auth-header';

const WAYBILL_API_URL = 'http://localhost:8081/api/v1';

class WaybillService {
  getWaybillById(id) {
    return axios.get(`${WAYBILL_API_URL}/waybills/${id}`, {
      headers: authHeader()
    });
  }

  createWaybillForImport(waybill) {
    debugger;
    return axios.post(`${WAYBILL_API_URL}/waybills/for-import`, waybill, {
      headers: authHeader()
    });
  }

  createWaybillForExport(waybill) {
    return axios.post(`${WAYBILL_API_URL}/waybills/for-export`, waybill, {
      headers: authHeader()
    });
  }

  approveOrRejectWaybill(id, status) {
    debugger;
    return axios.put(
      `${WAYBILL_API_URL}/waybills/${id}?status=${status}`,
      status,
      { headers: authHeader() }
    );
  }

  getCompletedWaybills(page, size) {
    return axios.get(`${WAYBILL_API_URL}/waybills/completed?page=${page}&size=${size}`, {
      headers: authHeader()
    });
  }

  getCompletedWaybillsForCurrentUser(page) {
    return axios.get(
      `${WAYBILL_API_URL}/waybills/current/completed?page=${page}`,
      {
        headers: authHeader()
      }
    );
  }

  getPendingWaybillsOfCurrentUser(page) {
    return axios.get(
      `${WAYBILL_API_URL}/waybills/current/pending?page=${page}`,
      {
        headers: authHeader()
      }
    );
  }

  getRejectedWaybillsOfCurrentUser(page) {
    return axios.get(
      `${WAYBILL_API_URL}/waybills/current/rejected?page=${page}`,
      {
        headers: authHeader()
      }
    );
  }

  getPendingImportWaybills(page) {
    return axios.get(
      `${WAYBILL_API_URL}/waybills/pending-import?page=${page}`,
      {
        headers: authHeader()
      }
    );
  }

  getPendingExportWaybills(page) {
    return axios.get(
      `${WAYBILL_API_URL}/waybills/pending-export?page=${page}`,
      {
        headers: authHeader()
      }
    );
  }

  getPendingAutoExportWaybills(page) {
    return axios.get(
      `${WAYBILL_API_URL}/waybills/pending-auto-export?page=${page}`,
      {
        headers: authHeader()
      }
    );
  }

  exportToCSV(id) {
    return axios.get(`${WAYBILL_API_URL}/waybills/download/${id}`, {
      headers: authHeader()
    });
  }

  uploadFromCSV(csvFile) {
    let user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
      return axios
        .post(`${WAYBILL_API_URL}/waybills/upload`, csvFile, {
          headers: {
            Authorization: 'Bearer ' + user.token,
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(function() {
          console.log('SUCCESS!!');
        })
        .catch(function() {
          console.log('FAILURE!!');
        });
    }
  }
}

export default new WaybillService();
