import React, { useState } from 'react';
import { Text, View, ScrollView, TouchableOpacity, Modal, TextInput, ActivityIndicator, Image } from 'react-native';
import arrow from '../../../assets/leftArrow.png';
import { theme, text } from '../../../styles';

interface FormProductsProps {
    setScreen: Function;
}

const FormProduct: React.FC<FormProductsProps> = (props) => {

    const { setScreen } = props;

    const [loading, setLoading] = useState(false);
    const [edit, setEdit] = useState(false);
    const [categories, setCategories] = useState([
        {
            id: 3,
            name: 'Computadores',
        },
        {
            id: 1,
            name: 'Livros',
        },
        {
            id: 2,
            name: 'Celulares',
        },
        {
            id: 4,
            name: 'Móveis',
        },
    ]);
    const [showCategories, setShowCategories] = useState(true);
    const [product, setProduct] = useState({
        name: null,
        description: null,
        imgUrl: null,
        price: null,
        categories: null,
    });

    return (
        <View style={theme.formContainer}>
            {
                loading ? (
                    <ActivityIndicator size='large' />
                ) : (
                    <View style={theme.formCard}>
                        <ScrollView>


                            <Modal visible={showCategories} animationType='fade' transparent={true} presentationStyle="overFullScreen">
                                <View style={theme.modalContainer}>
                                    <ScrollView contentContainerStyle={theme.modalContent}>
                                        {
                                            categories.map(cat => (
                                                <TouchableOpacity style={theme.modalItem} key={cat.id} onPress={() => {
                                                    setProduct({ ...product, categories: cat.name });
                                                    setShowCategories(!showCategories);
                                                }}>
                                                    <Text>{cat.name}</Text>
                                                </TouchableOpacity>
                                            ))
                                        }
                                    </ScrollView>
                                </View>
                            </Modal>

                            <TouchableOpacity onPress={() => setScreen("products")} style={theme.goBack}>
                                <Image source={arrow} />
                                <Text style={text.goBackText}>Voltar</Text>
                            </TouchableOpacity>

                            <TextInput placeholder="Nome do produto" style={theme.formInput} />

                            <TouchableOpacity onPress={() => setShowCategories(!showCategories)} style={theme.selectInput}>
                                <Text style={product.categories === null ? {color: '#9E9E9E'} : {color: '#000'} }>
                                    {
                                        product.categories === null ? 'Escolha uma categoria' : product.categories
                                    }
                                </Text>
                            </TouchableOpacity>

                            <TextInput placeholder="Preço" style={theme.formInput} />

                            <TouchableOpacity activeOpacity={0.8} style={theme.uploadBtn}>
                                <Text style={text.uploadText}>Carregar imagem</Text>
                            </TouchableOpacity>

                            <Text style={text.fileSize}>As imagens devem ser JPG ou PNG e não devem ultrapassar 5 mb.</Text>

                            <TextInput multiline placeholder="Descrição" style={theme.textArea} />

                            <View style={theme.buttonContainer}> 
                                <TouchableOpacity style={theme.deleteBtn}>
                                    <Text style={text.deleteTxt}>Cancelar</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={theme.saveBtn}>
                                    <Text style={text.saveTxt}>Salvar</Text>
                                </TouchableOpacity>
                            </View>
                        </ScrollView>

                    </View>
                )

            }
        </View>
    )
        ;
}

export default FormProduct;