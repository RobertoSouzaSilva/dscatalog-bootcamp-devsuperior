import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ProductsResponse } from 'core/types/Product';
import { makeRequest } from 'core/utils/request';
import ProductCard from './components/ProductCard';
import ProductCardLoad from './components/ProductDetails/Loaders/ProductCardLoader';
import './styles.scss';
import Pagination from 'core/components/Pagination';

const Catalog = () => {

    const [productsResponse, setproductsResponse] = useState<ProductsResponse>();
    const [isLoading, setIsLoading] = useState(false);
    const [activePage, setactivePage] = useState(0);


    useEffect(() => {
        const params = {
            page: activePage,
            linesPerPage: 12
        }
        setIsLoading(true);
        makeRequest({ url: '/products', params })
            .then(response => setproductsResponse(response.data))
            .finally(() => {
                setIsLoading(false);
            })
    }, [activePage]);

    return (
        <div className="catalog-container">
            <h1 className="catalog-title">Catálogo de produtos</h1>

            <div className="catalog-Products">
                {isLoading ? <ProductCardLoad /> : (
                    productsResponse?.content.map(product =>
                        <Link to={`/products/${product.id}`} key={product.id}>
                            <ProductCard product={product} />
                        </Link>
                    )
                )
                }

            </div>
            {productsResponse && (
                <Pagination totalPages={productsResponse.totalPages} activePage={activePage} onChange={page => setactivePage(page)} />
            )}
        </div>
    );
};

export default Catalog;