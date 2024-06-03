// Agregar script para ocultar el contenedor después de 10 segundos
function cerrarSesion() {
    console.log("se entro al metodo cerrar sesion de perfil")
    let boton = document.querySelector('.boton-cerrar-sesion3');
    boton.click();

}



function mostrarRecetasUsuario(event) {
    event.preventDefault();
    let botonRecetas = document.querySelector('.recetas-del-usuario');
    if (botonRecetas.style.display === 'none' || botonRecetas.style.display === '') {
        botonRecetas.style.display = 'block';
    } else {
        botonRecetas.style.display = 'none';
    }
}



function previewImage(event) {
    const fileInput = event.target;
    const preview = document.getElementById('imagePreview');
    const file = fileInput.files[0];
    const reader = new FileReader();

    reader.onloadend = function () {
        preview.src = reader.result;
    }

    if (file) {
        reader.readAsDataURL(file);
    } else {
        preview.src = "#{usuarioBacking.usuario.imagenPerfil}";
    }

    // Habilitar el botón de guardar imagen
    document.querySelector('.boton-guardar-imagen').classList.add('visible');
}

