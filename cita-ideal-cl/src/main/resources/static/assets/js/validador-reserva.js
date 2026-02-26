document.addEventListener('DOMContentLoaded', function() {
    // 1. Vinculamos con los IDs reales de tu nuevo HTML
    const fechaInput = document.getElementById('fechaInput'); // En este HTML es fechaInput
    const horaInput = document.querySelector('select[name="horaFija"]');
    const btnReservar = document.querySelector('button[type="submit"]');
    const dataContainer = document.getElementById('reserva-data');
    
    // 2. Obtenemos el ID del plan
    const planId = dataContainer ? dataContainer.getAttribute('data-plan-id') : null;

    // 3. Crear el div de error dinámicamente para no tocar tu HTML
    let alertaError = document.getElementById('mensaje-error-js');
    if (!alertaError) {
        alertaError = document.createElement('div');
        alertaError.id = 'mensaje-error-js';
        alertaError.className = 'alert alert-danger d-none mt-2 small';
        if (btnReservar) btnReservar.parentNode.insertBefore(alertaError, btnReservar);
    }

    function chequearDisponibilidad() {
        const fecha = fechaInput.value;
        const hora = horaInput.value;

        if (fecha && hora && planId) {
            fetch(`/reservas/validar-disponibilidad?planId=${planId}&fecha=${fecha}&hora=${hora}`)
                .then(response => response.json())
                .then(data => {
                    if (!data.disponible) {
                        alertaError.textContent = "❌ Este horario ya está ocupado. Por favor elige otro.";
                        alertaError.classList.remove('d-none');
                        btnReservar.disabled = true;
                    } else {
                        alertaError.classList.add('d-none');
                        btnReservar.disabled = false;
                    }
                });
        }
    }

    if (fechaInput && horaInput) {
        fechaInput.addEventListener('change', chequearDisponibilidad);
        horaInput.addEventListener('change', chequearDisponibilidad);
    }
});