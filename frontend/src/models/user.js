export default class User {
  constructor(username, email, password, retypePassword, roleName) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.retypePassword = retypePassword;
    this.roleName = roleName;
  }
}
