import axios,{Method} from 'axios';
import qs from 'qs';
import { CLIENT_ID, CLIENT_SECRET } from './auth';

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

export const makeRequest = ({method = 'GET', url, data, params, headers}: RequestParams) => {
    return axios({
        method,
        url: `${BASE_URL}${url}`,
        data,
        params,
        headers
    });
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