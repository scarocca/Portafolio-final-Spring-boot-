document.addEventListener('DOMContentLoaded', () => {
    const elementos = document.querySelectorAll('.reveal');

    const mostrarElementos = (entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('active');
                // Una vez que aparece, dejamos de observarlo para ahorrar recursos
                observer.unobserve(entry.target);
            }
        });
    };

    const observer = new IntersectionObserver(mostrarElementos, {
        root: null, // el viewport
        threshold: 0.15 // se activa cuando el 15% de la imagen es visible
    });

    elementos.forEach(el => observer.observe(el));
});

window.addEventListener('DOMContentLoaded', () => {
  const avion = document.getElementById('contenedor-avion');
  
  // Despega después de 1 segundo
  setTimeout(() => {
    avion.classList.remove('oculto');
    avion.classList.add('animar-vuelo');
  }, 1000);
});