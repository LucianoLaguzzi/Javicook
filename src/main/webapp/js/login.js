// Agregar script para ocultar el contenedor después de 10 segundos

function ocultarMensajeDeError() {
    setTimeout(function () {
        document.getElementById('error-container').style.display = 'none';
    }, 10000) // 10 segs
}

// agregar que se limpien los campos de texto despues de error con la misma tecnica.