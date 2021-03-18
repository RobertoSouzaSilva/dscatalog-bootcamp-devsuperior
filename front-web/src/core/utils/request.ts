import axios, { AxiosRequestConfig } from 'axios';
import qs from 'qs';
import { CLIENT_ID, CLIENT_SECRET, getSessionData, logout } from './auth';

// type RequestParams = {
//     method?: Method;
//     url: string;
//     data?: object | string;
//     params?: object;
//     headers?: object;
// }

type LoginData = {
    username: string;
    password: string;
}

const BASE_URL=  process.env.REACT_APP_BACKEND_URL ?? 'http://localhost:8080'


axios.interceptors.response.use(function (response) {
    console.log('a resposta deu certo', response)
    return response;
  }, function (error) {
      if(error.response.status === 401){
        logout();
    }
      return Promise.reject(error);

   
});


export const makeRequest = (params: AxiosRequestConfig) => {
    return axios({
      ...params,
      baseURL: BASE_URL
    });
  }
  

export const makePrivateRequest = (params: AxiosRequestConfig) => {
    const sessionData = getSessionData();
  
    const headers = {
      'Authorization': `Bearer ${sessionData.access_token}`
    }
  
    return makeRequest({ ...params, headers });
  }

//yarn add qs
//yarn add @types/qs

export const makeLogin = (longinData: LoginData) => {
    const token = `${CLIENT_ID}:${CLIENT_SECRET}`

    const headers = {
        Authorization: `Basic ${window.btoa(token)}`,
        'Content-Type': 'application/x-www-form-urlencoded'
    }

    const payload = qs.stringify({...longinData, grant_type: 'password'});
    //outra maneira de se fazer
    //const payload = `username=${longinData.username}&password=${longinData.password}&grant_type=password`

    return makeRequest({url: '/oauth/token', data: payload, method: 'POST', headers});


}