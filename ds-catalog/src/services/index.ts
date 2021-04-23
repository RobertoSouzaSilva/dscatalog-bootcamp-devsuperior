import axios from 'axios';

export const api = axios.create({
    baseURL: 'https://bootcamp-dscatalog.herokuapp.com/',
})