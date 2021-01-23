import React from 'react';
import './styles.scss'
import {ReactComponent as Computer} from '../../../../core/assets/images/image.svg'

const ProductCard = () => (
    <div className="card-base border-radius-10 product-card">
        <Computer/>
        <div className="product-info">
            <h6 className="product-name">
                Computador Desktop - Intel Core i7
            </h6>
            <div className="product-price-container">
                <span className="product-currency">R$</span>
                <h3 className="product-price">2.799,00</h3>
            </div>
        </div>
    </div>
);

export default ProductCard;