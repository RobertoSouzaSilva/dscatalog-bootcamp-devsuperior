import { makePrivateRequest, makeRequest } from 'core/utils/request';
import { useForm } from 'react-hook-form';
import BaseForm from '../../BaseForm';
import './styles.scss';
import { toast } from 'react-toastify';
import { useHistory, useParams } from 'react-router-dom';
import  React, { useEffect } from 'react';


type FormState = {
    name: string,
    price: string;
    //category: string;
    description: string;
    imgUrl: string;
}

type ParamsType = {
    productId: string
}
  

const Form = () => {
    const { register, handleSubmit, errors, setValue } = useForm<FormState>();
    const history = useHistory();
    const { productId } = useParams<ParamsType>();
    const isEditing = productId !== 'create';
    const formTitle = isEditing ? 'Editar produto':'Cadastrar produto';

    useEffect(() => {
        if (isEditing) {
          makeRequest({ url: `/products/${productId}` })
            .then(response => {
              setValue('name', response.data.name);
              setValue('price', response.data.price);
              setValue('description', response.data.description);
              setValue('imgUrl', response.data.imgUrl);
            })
        }
      }, [productId, isEditing, setValue]);


    const onSubmit = (data: FormState) => {
        makePrivateRequest({ 
          url: isEditing ? `/products/${productId}` : '/products', 
          method: isEditing ? 'PUT' : 'POST', 
          data 
        })
          .then(() => {
            toast.info('Produto salvo com sucesso!');
            history.push('/admin/products');
          })
          .catch(() => {
            toast.error('Erro ao salvar produto!');
          });
      }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
        <BaseForm title={formTitle}>
                <div className="row">
                    <div className="col-6">
                        <div className="margin-bottom-30">
                            <input type="text" ref={register({ required: "Campo obrigatório", minLength: {value: 5, message: "O campo deve ter no mínimo 5 caracteres"}, maxLength: {value: 60, message: "O campo deve ter no máximo 60 caracteres"} })} name="name" className="form-control  input-base" placeholder="Nome do produto" />
                            {errors.name && (
                                <div className="invalid-feedback d-block">{errors.name.message}</div>
                            )}
                        </div>
                        <div className="margin-bottom-30">
                            <input type="number" ref={register({ required: "Campo obrigatório" })} name="price" className="form-control input-base" placeholder="Preço do produto" />
                            {errors.price && (
                                <div className="invalid-feedback d-block">{errors.price.message}</div>
                            )}
                        </div>
                        <div className="margin-bottom-30">
                            <input type="text" ref={register({ required: "Campo obrigatório" })} name="imgUrl" className="form-control input-base" placeholder="url imagem" />
                            {errors.imgUrl && (
                                <div className="invalid-feedback d-block">{errors.imgUrl.message}</div>
                            )}
                        </div>


                    </div>
                    <div className="col-6">
                            <textarea name="description" ref={register({ required: "Campo obrigatório" })} className="form-control input-base" cols={30} rows={10} placeholder="Descrição" />
                            {errors.description && (
                                <div className="invalid-feedback d-block">{errors.description.message}</div>
                            )}
                    </div>
                </div>
            </BaseForm>
        </form>
    );
}

export default Form;