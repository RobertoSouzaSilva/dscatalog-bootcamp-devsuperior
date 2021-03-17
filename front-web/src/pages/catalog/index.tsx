import axios from 'axios';
import React, { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ProductsResponse } from 'core/types/Product';
import { makeRequest } from 'core/utils/request';
import ProductCard from './components/ProductCard';
import ProductCardLoad from './components/ProductDetails/Loaders/ProductCardLoader';
import './styles.scss';
import Pagination from 'core/components/Pagination';
import ProductFilter, { FilterForm } from 'core/components/ProductFilters';

const Catalog = () => {

    const [productsResponse, setproductsResponse] = useState<ProductsResponse>();
    const [isLoading, setIsLoading] = useState(false);
    const [activePage, setactivePage] = useState(0);

    const getProducts = useCallback((filter?: FilterForm) => {
        const params = {
            page: activePage,
            linesPerPage: 12,
            name: filter?.name,
            categoryId: filter?.categoryId
        }
        setIsLoading(true);
        makeRequest({ url: '/products', params })
            .then(response => setproductsResponse(response.data))
            .finally(() => {
                setIsLoading(false);
            })
    }, [activePage])


    useEffect(() => {
        getProducts()
    }, [getProducts]);

    return (
        <div className="catalog-container">
            <div className="d-flex justify-content-between">
                <h1 className="catalog-title">Catálogo de produtos</h1>
                <ProductFilter onSearch={filter => getProducts(filter)} />
            </div>
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