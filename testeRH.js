import http from 'k6/http';
import { check, group, sleep } from 'k6';

export let options = {
  stages: [
    { duration: '1m', target: 1200 }, 
    { duration: '3m', target: 5400}, 
    { duration: '1m', target: 800 }, 
  ],
  thresholds: {
    http_req_duration: ['p(99)<1500'], // 99% of requests must complete below 1.5s
    'logged in successfully': ['p(99)<1500'], // 99% of requests must complete below 1.5s
  },
};

const BASE_URL = 'https://right-shot-club-api.herokuapp.com';
const USERNAME = 'admin';
const PASSWORD = 'admin';

export default () => {
  let headersLogin = {
    headers: {
      Authorization: 'Basic YWlyc29mdDphaXJzb2Z0',
    },
  };
  
  let formdata = { "grant_type": "password",
  "username" : USERNAME,
  "password" : PASSWORD};
  
  let loginRes = http.post(`${BASE_URL}/oauth/token`, formdata, headersLogin);
 
  check(loginRes, {
    'Logado com sucesso': (resp) => resp.status === 200,
  });

  //console.log(loginRes.json('access_token'));

  let authHeaders = {
    headers: {
      Authorization: `Bearer ${loginRes.json('access_token')}`,
    },
  };

  let myObjects = http.get(`${BASE_URL}/infoRSC/getInfoRSC`, authHeaders);
  
  //console.log(JSON.stringify(myObjects));
  
  const checkRes = check(myObjects, {
    'End-Point: /infoRSC/getInfoRSC 200': (r) => r.status === 200,
    'End-Point: /infoRSC/getInfoRSC BODY_NOT_NULL': (r) => r.body.length !== -1,
  });

  sleep(1);
};