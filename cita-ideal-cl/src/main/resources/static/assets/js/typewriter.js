document.addEventListener('DOMContentLoaded', () => {
    const elemento = document.getElementById('maquina-texto');
    const frases = [
        "Diseñamos la cita perfecta frente al mar.",
		"Momentos inolvidables",
        "Creamos experiencias mágicas para dos.",
		"Tu historia de amor merece un escenario único.",
		
    ];
    
    let fraseIndice = 0;
    let caracterIndice = 0;
    let estaBorrando = false;
    let velocidadEscribir = 100;

    function animarMaquina() {
        const fraseActual = frases[fraseIndice];
        
        if (estaBorrando) {
            elemento.textContent = fraseActual.substring(0, caracterIndice - 1);
            caracterIndice--;
            velocidadEscribir = 50; // Borra más rápido
        } else {
            elemento.textContent = fraseActual.substring(0, caracterIndice + 1);
            caracterIndice++;
            velocidadEscribir = 100; // Escribe a ritmo normal
        }

        // Lógica de pausas
        if (!estaBorrando && caracterIndice === fraseActual.length) {
            velocidadEscribir = 3000; // Pausa al final de la frase
            estaBorrando = true;
        } else if (estaBorrando && caracterIndice === 0) {
            estaBorrando = false;
            fraseIndice = (fraseIndice + 1) % frases.length;
            velocidadEscribir = 500;
        }

        setTimeout(animarMaquina, velocidadEscribir);
    }

    animarMaquina();
});

// Este código hace que la página se desvanezca antes de ir a la otra
document.querySelector('.btn-romance').addEventListener('click', function(e) {
    e.preventDefault(); // Detiene el salto inmediato
    const destino = this.href;
    
    document.body.style.opacity = '0';
    document.body.style.transition = 'opacity 0.5s ease';
    
    setTimeout(() => {
        window.location.href = destino;
    }, 500);
});

const hero = document.querySelector('.hero-section');

// Función base para crear corazones
function crearCorazon(x, y, isAutomatic) {
    const corazon = document.createElement('div');
    corazon.classList.add('corazon');
    corazon.innerHTML = '❤️';

    // Si es automático, posición aleatoria abajo. Si es mouse, posición del puntero.
    if (isAutomatic) {
        corazon.style.left = Math.random() * 100 + "vw";
        corazon.style.bottom = "-50px";
    } else {
        corazon.style.left = x + "px";
        corazon.style.top = y + "px";
    }

    // Variedad de tamaños y colores
    const size = (Math.random() * 15) + 10 + "px";
    corazon.style.fontSize = size;
    
    // Un toque de colores variados (Rojo y Rosado)
    const colores = ['#ff4d4d', '#ff85a2', '#ff0000'];
    corazon.style.color = colores[Math.floor(Math.random() * colores.length)];

    hero.appendChild(corazon);

    setTimeout(() => {
        corazon.remove();
    }, 4000);
}

// 1. Corazones automáticos (flujo constante desde abajo)
setInterval(() => {
    crearCorazon(0, 0, true);
}, 500);

// 2. Corazones al mover el mouse
hero.addEventListener('mousemove', (e) => {
    // Obtenemos la posición relativa al header
    const rect = hero.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    // Solo creamos uno de cada 5 movimientos para no saturar
    if (Math.random() > 0.8) {
        crearCorazon(x, y, false);
    }
});

