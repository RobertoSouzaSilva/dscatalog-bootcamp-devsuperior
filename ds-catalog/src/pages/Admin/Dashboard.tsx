import React, { useState } from 'react';
import { Text, View } from 'react-native';
import { TabBar } from '../../components';
import Categories from './Categories';
import Users from './Users';

//Products
import Products from './Products/ListProducts';
import FormProduct from './Products/FormProducts';
import EditProduct from './Products/EditProducts';

const Dashboard: React.FC = () => {

    const [screen, setScreen] = useState('products');
    const [productId, setProductId] = useState(0);

    return (
        <View>
           <TabBar screen={screen} setScreen={setScreen}/>
           {screen === "products" && <Products setScreen={setScreen} setProductId={setProductId}/>}
           {screen === "newProduct" && <FormProduct setScreen={setScreen} />}
           {screen === "editProduct" && <EditProduct setScreen={setScreen} productId={productId} />}
           {screen === "categories" && <Categories/>}
           {screen === "users" && <Users/>}
        </View>

    );
}

export default Dashboard;