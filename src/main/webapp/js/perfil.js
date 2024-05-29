// Agregar script para ocultar el contenedor después de 10 segundos
function cerrarSesion() {
    console.log("se entro al metodo cerrar sesion de perfil")
    let boton = document.querySelector('.boton-cerrar-sesion3');
    boton.click();

}


function mostrarRecetasUsuario(event) {
    event.preventDefault();
    let botonRecetas = document.querySelector('.recetas-del-usuario');

    if (botonRecetas.style.display === "none" || botonRecetas.style.display === "") {
        botonRecetas.style.display = "block";
    } else {
        botonRecetas.style.display = "none";
    }
}