package cl.sergiocarocca.cita_ideal_cl.entity;

import java.math.BigDecimal;

/**
 * Clase de apoyo que representa un elemento individual dentro del carrito de compras.
 * No es una entidad persistente en la base de datos; su propósito es encapsular 
 * la relación entre un {@link Plan} y la cantidad seleccionada durante la sesión del usuario.
 * * @author Sergio Carocca
 * @version 1.0
 */
public class ItemCarrito {

    /** El plan de servicio seleccionado por el usuario. */
    private Plan plan;

    /** La cantidad de veces que se ha agregado este plan al carrito. */
    private int cantidad;

    /**
     * Constructor por defecto para la creación de instancias vacías.
     */
    public ItemCarrito() {
        super();
    }

    /**
     * Constructor con parámetros para inicializar un ítem del carrito.
     * * @param plan El plan de servicio asociado.
     * @param cantidad Cantidad inicial del servicio.
     */
    public ItemCarrito(Plan plan, int cantidad) {
        super();
        this.plan = plan;
        this.cantidad = cantidad;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Calcula el monto parcial de este ítem multiplicando el precio base del plan
     * por la cantidad seleccionada.
     * * @return Un {@link BigDecimal} que representa el subtotal del ítem, 
     * manteniendo la precisión decimal para transacciones financieras.
     */
    public BigDecimal getSubtotal() {
        // Se asume que plan.getPrecioBase() no es nulo
        return plan.getPrecioBase().multiply(new BigDecimal(cantidad));
    }
}