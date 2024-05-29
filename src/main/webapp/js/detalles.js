function cerrarSesion() {
    console.log("se entro al metodo cerrar sesion de detallesJS")
    let boton = document.querySelector('.boton-cerrar-sesion2');
    console.log(boton)
    boton.click();

}


function mostrarMensajeConfirmacion() {
    // Mostrar el div de mensaje de confirmación
    document.querySelector(".mensaje-confirmacion").style.display = 'block';

    // Ocultar el mensaje después de segundos
    setTimeout(function() {
        document.querySelector(".mensaje-confirmacion").style.display = 'none';
    }, 10000); //
}




