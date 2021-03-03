import React from 'react';
import './styles.scss';
import AuthCard from '../Card';
import { Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import ButtonIcon from 'core/components/ButtonIcon';

type FormData = {
    email: string;
    password: string;
}

const Login = () => {

    const { register, handleSubmit, errors } = useForm<FormData>(); 

    const onSubmit = (data: FormData) => {
        console.log(data);
    }

    return (
        <AuthCard title="Login">
            <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
                <input name="email" type="email" ref={register} className="form-control input-base margin-bottom-30" placeholder="Email"/>
                <input name="password" type="password" ref={register} className="form-control input-base" placeholder="Senha"/>
                <Link to="/admin/auth/recover" className="login-link-recover">Esqueci a senha?</Link>
                <div className="login-submit">
                    <ButtonIcon text="Logar"/>
                </div>
                <div className="text-center">
                    <span className="not-registred">Não tem Cadastro?</span>
                    <Link to="/admin/auth/register" className="login-link-register">CADASTRAR</Link>
                </div>
            </form>
        </AuthCard>
    );
}

export default Login;