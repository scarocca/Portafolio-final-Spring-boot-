/**
 * Script para controlar el modal del osito de descuento
 * Solo se ejecuta si el usuario no ha visto la promo en esta sesión
 */
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('modal-descuento');
    const cerrarBtn = document.querySelector('.cerrar-modal');
    
    // Verificamos que los elementos existan (por si sec:authorize lo eliminó)
    if (!modal || !cerrarBtn) return;

    const yaVisto = sessionStorage.getItem('promoVisto');

    // Si no lo ha visto, mostramos el oso tras 3 segundos
    if (!yaVisto) {
        setTimeout(() => {
            modal.style.display = 'flex';
        }, 3000);
    }

    // Lógica para cerrar el modal
    cerrarBtn.onclick = () => {
        cerrarModal(modal);
    };

    // Cerrar si hace clic fuera del contenido blanco
    window.onclick = (event) => {
        if (event.target === modal) {
            cerrarModal(modal);
        }
    };
});

// Función auxiliar para cerrar y guardar estado
function cerrarModal(modalElement) {
    modalElement.style.display = 'none';
    sessionStorage.setItem('promoVisto', 'true');
}