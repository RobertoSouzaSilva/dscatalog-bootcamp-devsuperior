import React from 'react';
import {Text} from 'react-native';
import { createStackNavigator } from "@react-navigation/stack";
import { Home, Catalog, ProductDetails, Login } from "../pages";
import { nav, colors } from "../styles";
import { NavBar } from "../components";

const Stack = createStackNavigator();

const HeaderText: React.FC = () => <Text style={nav.leftText}>DS Catalog</Text>;

const Routes: React.FC = () => {
    return (
        <Stack.Navigator
        screenOptions={{
          headerTitle: " ",
          headerStyle: {
            backgroundColor: colors.primary,
          },
          headerTintColor: colors.white,
          headerLeft: () => <HeaderText />,
          headerRight: () => <NavBar />,
        }}
      >
        <Stack.Screen name="Home" component={Home} />
        <Stack.Screen name="Catalog" component={Catalog} />
        <Stack.Screen name="ProductDetails" component={ProductDetails} />
        <Stack.Screen name="Login" component={Login} />
        </Stack.Navigator>
    )
};

export default Routes;