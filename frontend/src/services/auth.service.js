import axios from 'axios';

const API_URL = 'http://localhost:8081/api/v1/';

class AuthService {
  login(user) {
    return axios
      .post(API_URL + 'auth/login', {
        username: user.username,
        password: user.password
      })
      .then(response => {
        if (response.data.token) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
  }

  register(user) {
    return axios
      .post(API_URL + 'registration', {
        username: user.username,
        email: user.email,
        password: user.password,
        retypePassword: user.retypePassword,
        roleName: user.roleName
      })
      .then(response => {
        if (response.data.token) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
      });
  }
}

export default new AuthService();
