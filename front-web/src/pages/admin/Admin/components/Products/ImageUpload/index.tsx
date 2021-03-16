import React, { useState } from 'react';
import './styles.scss';
import {ReactComponent as Upload} from 'core/assets/images/upload-placeholder.svg';
import { makePrivateRequest } from 'core/utils/request';
import { toast } from 'react-toastify';

type Props = {
    onUploadSuccess: (imgUrl: string) => void;
    productImgUrl: string;
}

const ImageUpload = ({onUploadSuccess, productImgUrl}:Props) => {

    const [uploadProgress, setUploadProgress] = useState(0);
    const [uploadedImgUrl, setUploadedImgUrl] = useState('');
    const imgUrl = uploadedImgUrl || productImgUrl;

    const onUploadProgress = (progressEvent: ProgressEvent) => {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
        setUploadProgress(progress);
    }

    const uploadImage = (selectedImage: File) =>{
        const payload = new FormData();
        payload.append('file', selectedImage);

        makePrivateRequest({url:'/products/image', method: 'POST', data: payload, onUploadProgress})
        .then(response => {setUploadedImgUrl(response.data.uri); onUploadSuccess(response.data.uri)})
        .catch(() => {toast.error("Erro ao enviar a imagem")})
        .finally(() => setUploadProgress(0));
    }

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) =>{
        const selectedImage = event.target.files?.[0];

        if(selectedImage){
            uploadImage(selectedImage);
        }
    }

    return (
        <div className="row">
            <div className="col-6">
                <div className="upload-button-container">
                    <input type="file" id="upload" accept="image/png, image/jpg" onChange={handleChange} hidden/>
                    <label htmlFor="upload">ADICIONAR IMAGEM</label>
                </div>
                <small className="upload-text-helper text-primary">
                    A imagem deve ser  JPG ou PNG e não deve ultrapassar <strong>5 mb</strong>.
                </small>
            </div>
            <div className="col-6 upload-placeholder">
               {uploadProgress > 0 && (
                   <>
                    <Upload/>
                    <div className="upload-progress-container">
                        <div className="upload-progress" style={{width: `${uploadProgress}%`}}>
                        </div>
                     </div>
                   </>
               )}
               {(imgUrl && uploadProgress === 0)  && (
                    <img className="uploaded-image" src={imgUrl} alt={imgUrl}/>

               )}
            </div>

        </div>
    )
}

export default ImageUpload;