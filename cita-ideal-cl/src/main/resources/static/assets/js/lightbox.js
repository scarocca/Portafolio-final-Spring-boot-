document.addEventListener('click', function (e) {
    // Verificamos si lo que clickeamos es una imagen de la galería
    if (e.target.classList.contains('img-gallery')) {
        const src = e.target.getAttribute('src'); // Obtenemos la ruta de la imagen
        const modalImg = document.getElementById('modalImage');
        
        modalImg.src = src; // Pasamos la ruta al modal
        
        // Disparamos el modal de Bootstrap manualmente
        const myModal = new bootstrap.Modal(document.getElementById('galleryModal'));
        myModal.show();
    }
});