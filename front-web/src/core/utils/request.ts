import axios,{Method} from 'axios';
import qs from 'qs';
import { CLIENT_ID, CLIENT_SECRET, getSessionData, logout } from './auth';
import history from './history';

type RequestParams = {
    method?: Method;
    url: string;
    data?: object | string;
    params?: object;
    headers?: object;
}

type LoginData = {
    username: string;
    password: string;
}

const BASE_URL='http://localhost:8080'


axios.interceptors.response.use(function (response) {
    console.log('a resposta deu certo', response)
    return response;
  }, function (error) {
      if(error.response.status === 401){
        logout();
    }
      return Promise.reject(error);

   
});


export const makeRequest = ({method = 'GET', url, data, params, headers}: RequestParams) => {
    return axios({
        method,
        url: `${BASE_URL}${url}`,
        data,
        params,
        headers
    });
}

export const makePrivateRequest = ({method = 'GET', url, data, params}:RequestParams) => {
    const sessionData = getSessionData();

    const headers = {
        'Authorization': `Bearer ${sessionData.access_token}`
    }

    return makeRequest({method, url, data, params, headers});
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