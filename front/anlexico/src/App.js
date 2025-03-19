import React, { useState } from 'react';
import './App.css';
import SeleccionarArchivo from './components/SeleccionarArchivo'; // Componente para selección de archivo

function App() {
  const [codigo, setCodigo] = useState('');  // Guardar el texto ingresado
  const [resultado, setResultado] = useState('');  // Guardar el resultado del análisis
  const [archivo, setArchivo] = useState(null);  // Guardar el archivo cargado
  const [esArchivo, setEsArchivo] = useState(false);  // Determinar si se envía un archivo o un texto

  // Función que maneja la solicitud POST al backend
  const analizarCodigo = () => {
    // Si se está enviando un archivo
    if (esArchivo && archivo) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const contenido = e.target.result;
        enviarCodigo(contenido);  // Enviar el contenido del archivo
      };
      reader.readAsText(archivo);  // Leer el archivo como texto
    } else {
      enviarCodigo(codigo);  // Enviar el código como texto
    }
  };

  // Función para enviar el código al backend
  const enviarCodigo = (codigo) => {
    fetch('http://localhost:8080/analizador/analizar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',  // Indicar que enviamos JSON
      },
      body: JSON.stringify({ codigo }),  // Enviar el código como JSON
    })
      .then((res) => res.json())  // Esperar que la respuesta sea un JSON
      .then((data) => {
        setResultado(JSON.stringify(data.tokens, null, 2));  // Mostrar los tokens
      })
      .catch((err) => console.error('Error:', err));  // Capturar cualquier error
  };

  // Función para manejar la entrada de texto
  const handleCodigoChange = (e) => {
    setCodigo(e.target.value);  // Actualizar el código ingresado
    setEsArchivo(false);  // Si el texto cambia, desactivar la opción de archivo
  };

  // Función para manejar el cambio en el archivo seleccionado
  const handleArchivoChange = (e) => {
    setArchivo(e.target.files[0]);  // Guardar el archivo
    setEsArchivo(true);  // Activar la opción de archivo
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Analizador Léxico</h1>

        <div>
          <textarea
            value={codigo}
            onChange={handleCodigoChange}
            placeholder="Ingresa tu código aquí"
            rows="10"
            cols="50"
          />
        </div>

        <div>
          <input
            type="file"
            onChange={handleArchivoChange}
            accept=".txt"  // Permitir solo archivos de texto
          />
        </div>

        <button onClick={analizarCodigo}>Analizar</button>

        {resultado && (
          <div>
            <h3>Resultado:</h3>
            <pre>{resultado}</pre>  {/* Mostrar el resultado del análisis */}
          </div>
        )}
      </header>
    </div>
  );
}

export default App;