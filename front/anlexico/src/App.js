import React, { useState } from 'react';
import './App.css';
import SeleccionarArchivo from './components/SeleccionarArchivo';

function App() {
  const [codigo, setCodigo] = useState('');
  const [resultado, setResultado] = useState([]);
  const [errores, setErrores] = useState([]); // Nuevo estado para los errores
  const [archivo, setArchivo] = useState(null);
  const [esArchivo, setEsArchivo] = useState(false);

  const analizarCodigo = () => {
    if (esArchivo && archivo) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const contenido = e.target.result;
        enviarCodigo(contenido);
      };
      reader.readAsText(archivo);
    } else {
      enviarCodigo(codigo);
    }
  };

  const enviarCodigo = (codigo) => {
    fetch('http://localhost:8080/analizador/analizar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ codigo }),
    })
      .then((res) => res.json())
      .then((data) => {
        setResultado(data.tokens || []); // Guardamos los tokens
        setErrores(data.errors || []);  // Guardamos los errores
      })
      .catch((err) => console.error('Error:', err));
  };

  const handleCodigoChange = (e) => {
    setCodigo(e.target.value);
    setEsArchivo(false);
  };

  const handleArchivoChange = (e) => {
    setArchivo(e.target.files[0]);
    setEsArchivo(true);
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
            accept=".txt"
          />
        </div>

        <button onClick={analizarCodigo}>Analizar</button>

        {/* Sección para mostrar los errores */}
        {errores.length > 0 && (
          <div className="errores-container">
            <h3>Errores Léxicos:</h3>
            <ul className="errores-lista">
              {errores.map((error, index) => (
                <li key={index}>{error}</li>
              ))}
            </ul>
          </div>
        )}

        {/* Sección para mostrar los tokens */}
        {resultado.length > 0 && (
          <div>
            <h3>Resultado:</h3>
            <table className="resultado-tabla">
              <thead>
                <tr>
                  <th>Token</th>
                  <th>Tipo</th>
                </tr>
              </thead>
              <tbody>
                {resultado.map((token, index) => {
                  const [valor, tipo] = token.split(' -> ');
                  return (
                    <tr key={index}>
                      <td>{valor}</td>
                      <td>{tipo}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </header>
    </div>
  );
}

export default App;
