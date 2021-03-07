import PrivateRoute from 'core/Routes/PrivateRoute';
import React from 'react';
import { Switch } from 'react-router-dom';
import Navbar from './Admin/components/Navbar';
import Products from './Admin/components/Products';
import './styles.scss'

const Admin = () => (
    <div className="admin-container">
        <Navbar/>
        <div className="admin-content">
            <Switch>
                <PrivateRoute path="/admin/products">
                    <Products/>
                </PrivateRoute>
                <PrivateRoute path="/admin/categories">
                    <h1>bbbbbbbbbbbbbbbbbbbb</h1>
                </PrivateRoute>
                <PrivateRoute path="/admin/users" allowedRoutes={['ROLE_ADMIN']}>
                    <h1>ccccccccccccccccccc</h1>
                </PrivateRoute>
            </Switch>
        </div>
    </div>

);

export default Admin;