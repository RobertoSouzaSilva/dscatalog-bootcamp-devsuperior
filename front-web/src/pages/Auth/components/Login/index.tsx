import React, { useState } from 'react';
import './styles.scss';
import AuthCard from '../Card';
import { Link, useHistory } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import ButtonIcon from 'core/components/ButtonIcon';
import { makeLogin } from 'core/utils/request';
import { saveSessionData } from 'core/utils/auth';

type FormData = {
    username: string;
    password: string;
}

const Login = () => {

    const { register, handleSubmit, errors } = useForm<FormData>(); 
    const [hasError, setHasError] = useState(false);
    const history = useHistory();

    const onSubmit = (data: FormData) => {
        makeLogin(data)
            .then(response => {
                setHasError(false); 
                saveSessionData(response.data);
                history.push('/admin');
                })
            .catch(() => {setHasError(true)});
    }

    return (
        <AuthCard title="Login">
            {hasError && (
                <div className="alert alert-danger mt-5">
                    Usuário ou senha inválidos!
                </div>
            )}
            <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
                <div className="margin-bottom-30">
                    <input name="username" type="email" ref={register({required: "Campo obrigatório", pattern: {value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i, message: "Email inválido"}})} className={`form-control input-base margin-bottom-30 ${errors.username ? 'is-invalid' : ''}`} placeholder="Email"/>
                    {errors.username && (
                        <div className="invalid-feedback">{errors.username.message}</div>
                    )}
                </div>
                <div className="margin-bottom-30">
                    <input name="password" type="password" ref={register({required: "Campo obrigatório"})} className={`form-control input-base ${errors.password ? 'is-invalid' : ''}`} placeholder="Senha"/>
                    {errors.password && (
                        <div className="invalid-feedback">{errors.password.message}</div>
                    )}
                </div>
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