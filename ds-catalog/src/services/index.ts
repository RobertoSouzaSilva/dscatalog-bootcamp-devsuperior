import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';


export const api = axios.create({
    baseURL: 'https://bootcamp-dscatalog.herokuapp.com/',
});

export const TOKEN = 'Basic ZHNjYXRhbG9nOmRzY2F0YWxvZzEyMw==';

export async function userToken(){
    const token = await AsyncStorage.getItem("@token");
    return token;
}

//Backend requests

export function getProducts(){
    const res = api.get('products?direction=DESC&orderBy=name');
    return res;
}

export async function createProduct(data: object) {
    const authToken = await userToken();
    const res = api.post(`/products`, data, {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    })
    
}

export function getCategories(){
    const res = api.get(`/categories?direction=ASC&orderBy=name`);
    return res;
}

export async function deleteProduct(id: number){
    const authToken = await userToken();
    const res = api.delete(`/products/${id}`, {
        headers: {
            Authorization: `Bearer ${authToken}`,
        }
    })
}

export async function getProduct(id: number){
    const res = await api.get(`/products/${id}`);
    return res;
}

export async function updateProduct(data: object){
    const authToken = await userToken();
    const res = api.put(`/products/${data.id}`, data, {
        headers: {
            Authorization: `Bearer ${authToken}`,
        }
    });
    return res
}

export async function uploadImage(image: string){
    if(!image) return;
    const authToken = await userToken();
    let data = new FormData();
    data.append("file", {
        uri: image,
        name: image,
    });

    const res = await api.post(`/products/image`, data, {
        headers: {
            Authorization: `Bearer ${authToken}`,
            "Content-Type": "multipart/form-data"
        }
    });

    return res;
}
