import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8081/api/v1/';

class UserService {
  getUserBoard() {
    return axios.get(API_URL + 'profile', { headers: authHeader() });
  }

  getUserRole() {
    return axios.get(API_URL + 'current/role', { headers: authHeader() });
  }
}

export default new UserService();
