import React from 'react';
import { useNavigation } from '@react-navigation/native';
import {View, Text, ImageSourcePropType, TouchableOpacity, Image} from 'react-native';
import { theme, text } from '../styles';
import {TextInputMask} from 'react-native-masked-text';

interface ProductProps {
    id: Number,
    name: String,
    imgUrl: String,
    price: string,
    role?: String,
    handleDelete: Function,
    handleEdit: Function
}

const ProductCard: React.FC<ProductProps> = ({id, name, imgUrl, price, role, handleDelete, handleEdit}) => {

    const navigation = useNavigation();

    return(
        <TouchableOpacity style={theme.productCard} onPress={() => role ? " " : navigation.navigate('ProductDetails', {id})}>
            <Image source={{uri: imgUrl}} style={theme.productImage}/>
            <View style={theme.productDescription}>
                <Text style={text.productName}>{name}</Text>
                <View style={theme.priceContainer}>
                    <Text style={text.currency}>R$</Text>
                    <TextInputMask type={'money'} options= {{precision: 2, separator: ',', delimiter:'.', suffixUnit: ''}} value={price} editable={false} style={text.productPrice} />
                    {/* <Text style={text.productPrice}>{price}</Text> */}
                </View>

            	{
                    role === 'admin' && (
                        <View style={theme.buttonContainer}>
                            <TouchableOpacity style={theme.deleteBtn} onPress={() => handleDelete(id)}>
                                <Text style={text.deleteTxt}>Excluir</Text>
                            </TouchableOpacity>
                            <TouchableOpacity style={theme.editBtn} onPress={() => handleEdit(id)}>
                                <Text style={text.editTxt}>Editar</Text>
                            </TouchableOpacity>
                        </View>
                    )
                }



            </View>
        </TouchableOpacity>
    )
}

export default ProductCard;