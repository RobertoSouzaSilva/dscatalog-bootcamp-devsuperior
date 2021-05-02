import React, { useState, useEffect } from 'react';
import { Text, View, ScrollView, ActivityIndicator, TouchableOpacity } from 'react-native';
import { ProductCard, SearchInput } from '../../../components';
import { getProducts } from '../../../services';

import {admin, text} from "../../../styles";

interface ProductProps {
    setScreen: Function;
}

const Products: React.FC<ProductProps> = (props) => {

    const [search, setSearch] = useState('');
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);

    const {setScreen} = props;

    async function fillProducts() {
        setLoading(true);
        const res = await getProducts();
        setProducts(res.data.content);
        setLoading(false);
    }

    const data = search.length > 0 ? products.filter(product => product.name.toLowerCase().includes(search.toLowerCase())) : products;


    useEffect(() => {
        fillProducts();
    }, [])


    return (
        <ScrollView contentContainerStyle={admin.container}>
            <TouchableOpacity style={admin.addButton} onPress={() => setScreen('newProduct')}>
                <Text style={text.textAddButton}>Adicionar</Text>
            </TouchableOpacity>

            <SearchInput placeholder="Nome do produto" search={search} setSearch={setSearch} placeholder="Nome do produto" />

            { loading ? (
                <ActivityIndicator size="large" />
            ) : (data.map((product) => (
                <ProductCard {...product} role="admin" />
            )))
            }

        </ScrollView>
    )
}

export default Products;

