import axios from "axios";

axios.defaults.withCredentials = true;

let ec2_publicIp = "http://3.27.70.254:8080";
let local_machineIp = "http://localhost:8080";

const instance = axios.create({
  baseURL: local_machineIp,
});

export default instance;
