import axios from 'axios';
import authHeader from './auth-header';

const PRODUCT_API_URL = 'http://localhost:8081/api/v1';

class ProductService {
  getProductById(id) {
    return axios.get(`${PRODUCT_API_URL}/products/${id}`, {
      headers: authHeader()
    });
  }

  getAllProducts(page, size) {
    return axios.get(`${PRODUCT_API_URL}/products/filtered?page=${page}&size=${size}`, {
      headers: authHeader()
    });
  }

  searchProducts(name) {
    return axios.get(`${PRODUCT_API_URL}/products/filtered?name=${name}`, {
      headers: authHeader()
    });
  }

  deleteProduct(id) {
    return axios.delete(`${PRODUCT_API_URL}/products/${id}`, {
      headers: authHeader()
    });
  }

  updateProduct(id, product) {
    return axios.put(`${PRODUCT_API_URL}/products/${id}`, product, {
      headers: authHeader()
    });
  }

  addProductsForImport(products) {
    return axios.post(`${PRODUCT_API_URL}/products`, products, {
      headers: authHeader()
    });
  }

  addProductsForExport(requestIds) {
    return axios.put(`${PRODUCT_API_URL}/products/prepare-export`, requestIds, {
      headers: authHeader()
    });
  }

  getPendingExportProductsForCurrentUser() {
    return axios.get(`${PRODUCT_API_URL}/products/pending-export`, {
      headers: authHeader()
    });
  }
}

export default new ProductService();
