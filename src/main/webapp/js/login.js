// Agregar script para ocultar el contenedor después de 10 segundos
function ocultarMensajeDeError() {
    setTimeout(function () {
        document.getElementById('error-container').style.display = 'none';
    }, 10000) // 10 segs
}


function cerrarSesion() {
// Llama al método en el backing bean para cerrar sesión y redirigir
    var boton = document.querySelector("[id$='btnCerrarSesion']");
    boton.click();

}

