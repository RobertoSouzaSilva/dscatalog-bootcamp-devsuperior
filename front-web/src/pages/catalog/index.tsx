import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ProductsResponse } from 'core/types/Product';
import { makeRequest } from 'core/utils/request';
import ProductCard from './components/ProductCard';
import ProductCardLoad from './components/ProductDetails/Loaders/ProductCardLoader';
import './styles.scss';

const Catalog = () => {

   const[productsResponse, setproductsResponse] = useState<ProductsResponse>();
   const [isLoading, setIsLoading] = useState(false);
 
    useEffect(() =>{
        const params = {
            page:0,
            linesPerPage:30
        }
        setIsLoading(true);
        makeRequest({url: '/products', params})
        .then(response => setproductsResponse(response.data))
        .finally(() => {
            setIsLoading(false);
        })
    }, []);

    return(
        <div className="catalog-container">
        <h1 className="catalog-title">Catálogo de produtos</h1>

        <div className="catalog-Products">
            {isLoading ? <ProductCardLoad/> : (
                productsResponse?.content.map(product =>
                <Link to={`/products/${product.id}`} key={product.id}>
                    <ProductCard product={product}/>
                </Link>
                )
            )
            }
           
        </div>
    </div>
    );
};

export default Catalog;