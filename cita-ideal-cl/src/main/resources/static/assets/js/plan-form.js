/**
 * plan-form.js
 * Gestión de previsualización y validación de imágenes para Cita Ideal.
 * Soporta carga de archivos locales y URLs externas.
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Selección de elementos del DOM
    const inputUrl = document.getElementById('inputUrl');
    const inputArchivo = document.getElementById('inputArchivo');
    const imgPreview = document.getElementById('imgPreview');
    const btnGuardar = document.querySelector('button[type="submit"]');
    
    // Expresión regular para validar extensiones de imagen comunes
    const regexImagen = /\.(jpeg|jpg|gif|png|webp|avif)$/i;

    /**
     * Valida la URL, cambia el estado visual del input y actualiza la miniatura.
     */
    function gestionarUrl() {
        const url = inputUrl.value.trim();

        if (url === "") {
            inputUrl.classList.remove('is-invalid', 'is-valid');
            btnGuardar.disabled = false;
            return;
        }

        if (regexImagen.test(url)) {
            // URL Válida: Mostrar éxito y activar previsualización
            inputUrl.classList.remove('is-invalid');
            inputUrl.classList.add('is-valid');
            imgPreview.src = url;
            imgPreview.style.display = 'block';
            btnGuardar.disabled = false;
        } else {
            // URL Inválida: Mostrar error y bloquear botón
            inputUrl.classList.remove('is-valid');
            inputUrl.classList.add('is-invalid');
            imgPreview.style.display = 'none';
            btnGuardar.disabled = true;
        }
    }

    /**
     * Procesa el archivo local seleccionado y limpia el campo de URL para evitar conflictos.
     */
    function gestionarArchivoLocal() {
        const file = inputArchivo.files[0];
        
        if (file) {
            // Validación de tamaño (opcional: 2MB)
            if (file.size > 2 * 1024 * 1024) {
                alert("El archivo es demasiado grande. El máximo es 2MB.");
                inputArchivo.value = "";
                return;
            }

            const reader = new FileReader();
            
            reader.onload = function(e) {
                imgPreview.src = e.target.result;
                imgPreview.style.display = 'block';
                
                // Limpieza cruzada: Si hay archivo, no debe haber URL
                inputUrl.value = ""; 
                inputUrl.classList.remove('is-invalid', 'is-valid');
                btnGuardar.disabled = false;
            };
            
            reader.readAsDataURL(file);
        }
    }

    // 2. Asignación de Eventos
    if (inputUrl) {
        inputUrl.addEventListener('input', gestionarUrl);
    }

    if (inputArchivo) {
        inputArchivo.addEventListener('change', gestionarArchivoLocal);
    }
});
document.addEventListener('DOMContentLoaded', function () {
    const inputUrl = document.getElementById('inputUrl');
    const imgPreview = document.getElementById('imgPreview');
    const spinner = document.getElementById('spinnerCarga');
    const btnGuardar = document.querySelector('button[type="submit"]');
    const regexImagen = /\.(jpeg|jpg|gif|png|webp|avif)$/i;

    // Función para mostrar/ocultar carga
    function mostrarCargando(estado) {
        if (estado) {
            spinner.classList.remove('d-none');
            imgPreview.style.display = 'none';
        } else {
            spinner.classList.add('d-none');
            imgPreview.style.display = 'block';
        }
    }

    inputUrl.addEventListener('input', function() {
        const url = this.value.trim();

        if (url !== "" && regexImagen.test(url)) {
            mostrarCargando(true); // Iniciamos animación
            imgPreview.src = url;
            
            // Evento cuando la imagen descarga con éxito
            imgPreview.onload = function() {
                mostrarCargando(false);
                inputUrl.classList.remove('is-invalid');
                inputUrl.classList.add('is-valid');
                btnGuardar.disabled = false;
            };

            // Evento si la URL es válida pero el link está roto
            imgPreview.onerror = function() {
                mostrarCargando(false);
                imgPreview.style.display = 'none';
                inputUrl.classList.add('is-invalid');
                btnGuardar.disabled = true;
            };

        } else if (url === "") {
            mostrarCargando(false);
            imgPreview.style.display = 'none';
            btnGuardar.disabled = false;
        }
    });
});