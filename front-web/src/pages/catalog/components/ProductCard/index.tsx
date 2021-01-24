import React from 'react';
import './styles.scss'
import {ReactComponent as Computer} from '../../../../core/assets/images/image.svg'
import ProductPrice from '../../../../core/components/ProductPrice';

const ProductCard = () => (
    <div className="card-base border-radius-10 product-card">
        <Computer/>
        <div className="product-info">
            <h6 className="product-name">
                Computador Desktop - Intel Core i7
            </h6>
            <ProductPrice price="2.799,00"/>
        </div>
    </div>
);

export default ProductCard;