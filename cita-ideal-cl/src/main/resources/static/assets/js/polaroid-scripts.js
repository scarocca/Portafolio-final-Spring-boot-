// js/polaroid-scripts.js
document.addEventListener('DOMContentLoaded', () => {
    const photos = document.querySelectorAll('.photo');
	let highZ = 100;
    photos.forEach(photo => {
        photo.addEventListener('click', function() {
            // Buscamos cuál es el z-index más alto actualmente
            let maxZ = Math.max(...Array.from(document.querySelectorAll('.photo'))
                        .map(p => parseInt(getComputedStyle(p).zIndex) || 0));
            
            // Le asignamos a la foto clickeada el máximo + 1
            this.style.zIndex = maxZ + 1;
            
            // Un pequeño efecto de "sacudida" al seleccionar
            this.style.transform = 'scale(1.1) rotate(0deg)';
            
            // Volvemos al estado normal según el CSS para que no se vea rígido
            setTimeout(() => {
                this.style.transform = '';
            }, 300);
        });
    });
});